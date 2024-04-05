package com.application.WorkManagement.services.card;

import com.application.WorkManagement.dto.mappers.card.CardListResponseMapper;
import com.application.WorkManagement.dto.mappers.card.CardResponseMapper;
import com.application.WorkManagement.dto.requests.card.BasicUpdateRequest;
import com.application.WorkManagement.dto.requests.card.PositionUpdateRequest;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Category;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.InvalidPositionException;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.CardService;
import com.application.WorkManagement.services.table.TablePermissionChecker;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CardServiceImplement implements CardService {

    private final AccountRepository accountRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final TableRepository tableRepository;

    private final CategoryRepository categoryRepository;

    private final CardRepository cardRepository;

    private final CardListResponseMapper cardListResponseMapper;

    private final CardResponseMapper cardResponseMapper;

    private final CardFollowRepository cardFollowRepository;

    private final CardMemberRepository cardMemberRepository;

    private final CommentRepository commentRepository;

    private final DeadlineRepository deadlineRepository;

    public CardServiceImplement(
            AccountRepository accountRepository,
            TablePermissionChecker tablePermissionChecker,
            TableRepository tableRepository,
            CategoryRepository categoryRepository,
            CardRepository cardRepository,
            CardListResponseMapper cardListResponseMapper,
            CardResponseMapper cardResponseMapper,
            CardFollowRepository cardFollowRepository,
            CardMemberRepository cardMemberRepository,
            CommentRepository commentRepository,
            DeadlineRepository deadlineRepository
    ) {
        this.accountRepository = accountRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.tableRepository = tableRepository;
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
        this.cardListResponseMapper = cardListResponseMapper;
        this.cardResponseMapper = cardResponseMapper;
        this.cardFollowRepository = cardFollowRepository;
        this.cardMemberRepository = cardMemberRepository;
        this.commentRepository = commentRepository;
        this.deadlineRepository = deadlineRepository;
    }

    @Override
    public CardResponse readCardById(
            String accountId,
            UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkReadCardPermission(account, card);
        Boolean isFollow = cardFollowRepository.existsCardFollowByAccountAndCard(account, card);
        return cardResponseMapper.apply(card, isFollow);
    }

    @Override
    public CardResponse updateCard(
            String accountId,
            UUID cardId,
            BasicUpdateRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        if (request.getName() != null && !request.getName().isBlank()) {
            card.setName(request.getName());
        }
        if (request.getDescription() != null) {
            card.setDescription(request.getDescription());
        }
        if (request.getProgress() != null && !request.getProgress().equals(card.getProgress())) {
            card.setProgress(request.getProgress());
        }
        Boolean isFollow = cardFollowRepository.existsCardFollowByAccountAndCard(account, card);
        return cardResponseMapper.apply(
                cardRepository.save(card),
                isFollow
        );
    }

    @Override
    public CardResponse updatePositionOfCard(String accountId, UUID cardId, PositionUpdateRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        Category category = getCategoryById(request.getCategoryId());
        if (card.getCategory().getUuid().equals(category.getUuid())){
            if (request.getLocation() > category.getNumberOfCards()) {
                throw new InvalidPositionException("Vị trí không hợp lệ");
            }
            if (card.getLocation() > request.getLocation()) {
                Integer location = card.getLocation();
                card.setLocation(request.getLocation());
                card = cardRepository.save(card);
                cardRepository
                        .findCardsByCategory(card.getCategory())
                        .forEach(item -> {
                            if (item.getLocation() >= request.getLocation() && item.getLocation() < location && !item.getUuid().equals(cardId)) {
                                item.setLocation(item.getLocation() + 1);
                                cardRepository.save(item);
                            }
                        });
            }
            else if (card.getLocation() < request.getLocation()) {
                Integer location = card.getLocation();
                card.setLocation(request.getLocation());
                card = cardRepository.save(card);
                cardRepository
                        .findCardsByCategory(card.getCategory())
                        .forEach(item -> {
                            if (item.getLocation() > location && item.getLocation() <= request.getLocation() && !item.getUuid().equals(cardId)){
                                item.setLocation(item.getLocation() - 1);
                                cardRepository.save(item);
                            }
                        });
            }
        }
        else {
            if (request.getLocation() > category.getNumberOfCards() + 1) {
                throw new InvalidPositionException("Vị trí không hợp lệ");
            }
            Category oldCategory = card.getCategory();
            Integer oldLocation = card.getLocation();
            card.setLocation(request.getLocation());
            card.setCategory(category);
            card = cardRepository.save(card);
            oldCategory.setNumberOfCards(oldCategory.getNumberOfCards() - 1);
            categoryRepository.save(oldCategory);
            cardRepository
                    .findCardsByCategory(oldCategory)
                    .forEach(item -> {
                        if (item.getLocation() > oldLocation){
                            item.setLocation(item.getLocation() - 1);
                            cardRepository.save(item);
                        }
                    });
            category.setNumberOfCards(category.getNumberOfCards() + 1);
            category = categoryRepository.save(category);
            cardRepository
                    .findCardsByCategory(category)
                    .forEach(item -> {
                        if (item.getLocation() >= request.getLocation() && !item.getUuid().equals(cardId)){
                            item.setLocation(item.getLocation() + 1);
                            cardRepository.save(item);
                        }
                    });
        }
        Boolean isFollow = cardFollowRepository.existsCardFollowByAccountAndCard(account, card);
        return cardResponseMapper.apply(card, isFollow);
    }

    @Override
    public void deleteCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        Category category = card.getCategory();
        checkManageCardPermission(account, card);
        cardRepository.deleteById(card.getUuid());
        category.setNumberOfCards(category.getNumberOfCards() - 1);
        category = categoryRepository.save(category);
        cardRepository
                .findCardsByCategory(category)
                .forEach(item -> {
                    if (item.getLocation() > card.getLocation()){
                        item.setLocation(item.getLocation() - 1);
                        cardRepository.save(item);
                    }
                });
    }

    private Card getCardFromId(UUID cardId) throws DataNotFoundException {
        return cardRepository
                .findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy thẻ công việc"));
    }

    private Account getAccountFromAuthenticationName(String accountId) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }

    private Category getCategoryById(UUID categoryId) throws DataNotFoundException {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy danh mục"));
    }

    private void checkManageCardPermission(Account account, Card card) throws CustomAccessDeniedException {
        tablePermissionChecker.checkManageComponentsInTablePermission(account, card.getCategory().getTable());
    }

    private void checkReadCardPermission(Account account, Card card) throws CustomAccessDeniedException {
        tablePermissionChecker.checkReadPermission(account, card.getCategory().getTable());
    }
}
