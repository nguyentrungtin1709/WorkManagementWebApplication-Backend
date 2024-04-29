package com.application.WorkManagement.services.card;

import com.application.WorkManagement.dto.mappers.card.CardCommentResponseMapper;
import com.application.WorkManagement.dto.mappers.card.CardListResponseMapper;
import com.application.WorkManagement.dto.mappers.card.CardMemberResponseMapper;
import com.application.WorkManagement.dto.mappers.card.CardResponseMapper;
import com.application.WorkManagement.dto.mappers.table.TableMemberMapper;
import com.application.WorkManagement.dto.requests.card.*;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardMemberResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.dto.responses.card.comment.CardCommentResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.entities.*;
import com.application.WorkManagement.entities.CompositePrimaryKeys.CardCompositeId;
import com.application.WorkManagement.enums.ActivityType;
import com.application.WorkManagement.exceptions.custom.*;
import com.application.WorkManagement.repositories.*;
import com.application.WorkManagement.services.Interface.CardService;
import com.application.WorkManagement.services.table.TablePermissionChecker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {

    private final AccountRepository accountRepository;

    private final TablePermissionChecker tablePermissionChecker;

    private final TableMemberRepository tableMemberRepository;

    private final TableMemberMapper tableMemberMapper;

    private final CategoryRepository categoryRepository;

    private final CardRepository cardRepository;

    private final CardResponseMapper cardResponseMapper;

    private final CardFollowRepository cardFollowRepository;

    private final CardMemberRepository cardMemberRepository;

    private final CardMemberResponseMapper cardMemberResponseMapper;

    private final CardCommentResponseMapper cardCommentResponseMapper;

    private final CommentRepository commentRepository;

    private final DeadlineRepository deadlineRepository;

    private final ActivityRepository activityRepository;

    public CardServiceImplement(
            AccountRepository accountRepository,
            TablePermissionChecker tablePermissionChecker,
            TableMemberRepository tableMemberRepository,
            TableMemberMapper tableMemberMapper,
            CategoryRepository categoryRepository,
            CardRepository cardRepository,
            CardResponseMapper cardResponseMapper,
            CardFollowRepository cardFollowRepository,
            CardMemberRepository cardMemberRepository,
            CardMemberResponseMapper cardMemberResponseMapper,
            CardCommentResponseMapper cardCommentResponseMapper,
            CommentRepository commentRepository,
            DeadlineRepository deadlineRepository,
            ActivityRepository activityRepository
    ) {
        this.accountRepository = accountRepository;
        this.tablePermissionChecker = tablePermissionChecker;
        this.tableMemberRepository = tableMemberRepository;
        this.tableMemberMapper = tableMemberMapper;
        this.categoryRepository = categoryRepository;
        this.cardRepository = cardRepository;
        this.cardResponseMapper = cardResponseMapper;
        this.cardFollowRepository = cardFollowRepository;
        this.cardMemberRepository = cardMemberRepository;
        this.cardMemberResponseMapper = cardMemberResponseMapper;
        this.cardCommentResponseMapper = cardCommentResponseMapper;
        this.commentRepository = commentRepository;
        this.deadlineRepository = deadlineRepository;
        this.activityRepository = activityRepository;
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
        activityRepository.save(Activity
                .builder()
                .activityType(ActivityType.DELETE_CARD)
                .account(account)
                .table(category.getTable())
                .category(category)
                .createdAt(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public CardMemberResponse addMemberToCard(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        Account member = getAccountFromId(memberId);
        checkHasAnyRoleInTable(member, card);
        checkAccountHasBeenMemberOfCard(member, card);
        CardMember cardMember = cardMemberRepository.save(CardMember
                .builder()
                .account(member)
                .card(card)
                .build()
        );
        if (!accountHasFollowedCard(member, card)) {
            cardFollowRepository.save(CardFollow
                    .builder()
                    .account(member)
                    .card(card)
                    .build()
            );
        }
        return cardMemberResponseMapper.apply(cardMember);
    }

    @Override
    public CardMemberResponse joinInCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        checkAccountHasBeenMemberOfCard(account, card);
        CardMember cardMember = cardMemberRepository.save(
                CardMember
                    .builder()
                    .account(account)
                    .card(card)
                    .build()
        );
        if (!accountHasFollowedCard(account, card)) {
            cardFollowRepository.save(
                    CardFollow
                        .builder()
                        .account(account)
                        .card(card)
                        .build()
            );
        }
        return cardMemberResponseMapper.apply(cardMember);
    }

    @Override
    public List<TableMemberResponse> readMemberListInTableButNotInCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        List<Account> membersOfCard = cardMemberRepository
                .findCardMembersByCard(card)
                .stream()
                .map(CardMember::getAccount)
                .toList();
        return tableMemberRepository
                .findTableMembersByTableAndAccountNotIn(
                    card.getCategory().getTable(),
                    membersOfCard
                )
                .stream()
                .map(tableMemberMapper)
                .toList();
    }

    @Override
    public List<CardMemberResponse> readMembersListOfCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkReadCardPermission(account, card);
        return cardMemberRepository
                .findCardMembersByCard(card)
                .stream()
                .map(cardMemberResponseMapper)
                .toList();
    }

    @Override
    public CardMemberResponse readMemberById(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkReadCardPermission(account, card);
        Account member = getAccountFromId(memberId);
        return cardMemberResponseMapper
                .apply(
                        cardMemberRepository
                                .findCardMemberByAccountAndCard(member, card)
                                .orElseThrow(() -> new DataNotFoundException("Tài khoản không là thành viên của thẻ"))
                );
    }

    @Override
    @Transactional
    public void deleteMemberOfCard(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        Account member = getAccountFromId(memberId);
        cardMemberRepository.deleteCardMemberByAccountAndCard(member, card);
    }

    @Override
    @Transactional
    public void leaveMembersList(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkHasAnyRoleInTable(account, card);
        cardMemberRepository.deleteCardMemberByAccountAndCard(account, card);
        cardFollowRepository.deleteCardFollowByAccountAndCard(account, card);
    }

    @Override
    public void followCard(
            String accountId,
            UUID cardId
    ) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkHasAnyRoleInTable(account, card);
        checkAccountHasFollowCard(account, card);
        cardFollowRepository.save(
                CardFollow
                        .builder()
                        .account(account)
                        .card(card)
                        .build()
        );
    }

    @Override
    @Transactional
    public void unfollowCard(String accountId, UUID cardId) throws CustomAccessDeniedException, DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkHasAnyRoleInTable(account, card);
        cardFollowRepository.deleteCardFollowByAccountAndCard(account, card);
    }

    @Override
    public void setDeadline(String accountId, UUID cardId, DeadlineRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidDeadlineException, CustomDuplicateException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        if (deadlineRepository.existsDeadlineByCard(card)) {
            throw new CustomDuplicateException("Thẻ đã có ngày hết hạn");
        }
        LocalDateTime deadline = Instant
                .ofEpochMilli(request.getDeadline())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new InvalidDeadlineException("Ngày hết hạn không hợp lệ");
        }
        LocalDateTime reminder = deadline.minusDays(request.getReminder());
        if (reminder.isBefore(LocalDateTime.now())) {
            throw new InvalidDeadlineException("Ngày nhắc nhở không hợp lệ");
        }
        deadlineRepository.save(
                Deadline.builder()
                        .deadline(deadline)
                        .reminderDate(reminder)
                        .complete(false)
                        .account(account)
                        .card(card)
                        .build()
        );
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.NOTIFICATION_DEADLINE)
                        .createdAt(deadline)
                        .account(account)
                        .table(card.getCategory().getTable())
                        .category(card.getCategory())
                        .card(card)
                        .build()
        );
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.REMINDER_DEADLINE)
                        .createdAt(reminder)
                        .account(account)
                        .table(card.getCategory().getTable())
                        .category(card.getCategory())
                        .card(card)
                        .build()
        );
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.SET_DEADLINE)
                        .createdAt(LocalDateTime.now())
                        .account(account)
                        .table(card.getCategory().getTable())
                        .category(card.getCategory())
                        .card(card)
                        .build()
        );
    }

    @Override
    @Transactional
    public void removeDeadline(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        deadlineRepository.deleteDeadlineByCard(card);
        activityRepository.save(
                Activity
                        .builder()
                        .activityType(ActivityType.DELETE_DEADLINE)
                        .createdAt(LocalDateTime.now())
                        .account(account)
                        .table(card.getCategory().getTable())
                        .category(card.getCategory())
                        .card(card)
                        .build()
        );
    }

    @Override
    public void updateCompleteFieldOfDeadline(
            String accountId,
            UUID cardId,
            CompleteDeadlineRequest request
    ) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkManageCardPermission(account, card);
        Deadline deadline = deadlineRepository.findDeadlineByCard(card);
        if (deadline == null) {
            throw new DataNotFoundException("Thẻ chưa được đặt hạn chót");
        } else {
            deadline.setComplete(request.getComplete());
            deadlineRepository.save(deadline);
        }
    }

    @Override
    public CardCommentResponse createComment(String accountId, UUID cardId, CommentRequest request) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkHasAnyRoleInTable(account, card);
        return cardCommentResponseMapper.apply(
                commentRepository.save(
                        Comment
                            .builder()
                                .comment(request.getComment())
                                .update(false)
                                .account(account)
                                .card(card)
                                .build()

                )
        );
    }

    @Override
    public List<CardCommentResponse> readCommentsList(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkReadCardPermission(account, card);
        return commentRepository
                .findCommentsByCardOrderByCreatedAtDesc(card)
                .stream()
                .map(cardCommentResponseMapper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        Comment comment = getCommentById(commentId);
        if (comment.getAccount().getUuid().equals(account.getUuid())){
            checkHasAnyRoleInTable(account, card);
        } else {
            checkAdminPermissionInTable(account, card);
        }
        commentRepository.deleteById(comment.getUuid());
    }

    @Override
    public CardCommentResponse readComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        checkReadCardPermission(account, card);
        return cardCommentResponseMapper.apply(
                getCommentById(commentId)
        );
    }

    @Override
    public CardCommentResponse revokeComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException {
        Account account = getAccountFromAuthenticationName(accountId);
        Card card = getCardFromId(cardId);
        Comment comment = getCommentById(commentId);
        if (comment.getAccount().getUuid().equals(account.getUuid())){
            checkHasAnyRoleInTable(account, card);
        } else {
            checkAdminPermissionInTable(account, card);
        }
        comment.setComment("");
        comment.setUpdate(true);
        return cardCommentResponseMapper
                .apply(
                        commentRepository.save(comment)
                );
    }

    private Card getCardFromId(UUID cardId) throws DataNotFoundException {
        return cardRepository
                .findById(cardId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy thẻ công việc"));
    }

    private Comment getCommentById(UUID commentId) throws DataNotFoundException {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy bình luận"));
    }

    private Account getAccountFromId(UUID accountId) throws DataNotFoundException {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
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

    private void checkAdminPermissionInTable(Account account, Card card) throws CustomAccessDeniedException {
        tablePermissionChecker.checkManagePermission(account, card.getCategory().getTable());
    }

    private void checkHasAnyRoleInTable(Account account, Card card) throws CustomAccessDeniedException {
        tablePermissionChecker.checkMemberPermission(account, card.getCategory().getTable());
    }

    private void checkReadCardPermission(Account account, Card card) throws CustomAccessDeniedException {
        tablePermissionChecker.checkReadPermission(account, card.getCategory().getTable());
    }

    private void checkAccountHasBeenMemberOfCard(Account account, Card card) throws CustomDuplicateException {
        if (cardMemberRepository.existsCardMemberByAccountAndCard(account, card)){
            throw new CustomDuplicateException("Tài khoản đã là thành viên của thẻ");
        }
    }

    private void checkAccountHasFollowCard(Account account, Card card) throws CustomDuplicateException {
        if (cardFollowRepository.existsCardFollowByAccountAndCard(account, card)) {
            throw new CustomDuplicateException("Tài khoản đã theo dõi thẻ");
        }
    }

    private Boolean accountHasFollowedCard(Account account, Card card) {
        return cardFollowRepository.existsCardFollowByAccountAndCard(account, card);
    }
}
