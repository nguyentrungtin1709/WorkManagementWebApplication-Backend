package com.application.WorkManagement.services.notification;

import com.application.WorkManagement.dto.mappers.table.TableActivityMapper;
import com.application.WorkManagement.dto.responses.table.TableActivityResponse;
import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.CardFollow;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.repositories.AccountRepository;
import com.application.WorkManagement.repositories.ActivityRepository;
import com.application.WorkManagement.repositories.CardFollowRepository;
import com.application.WorkManagement.services.Interface.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final AccountRepository accountRepository;

    private final ActivityRepository activityRepository;

    private final CardFollowRepository cardFollowRepository;

    private final TableActivityMapper tableActivityMapper;

    public NotificationServiceImpl(
            AccountRepository accountRepository,
            ActivityRepository activityRepository,
            CardFollowRepository cardFollowRepository,
            TableActivityMapper tableActivityMapper
    ) {
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.cardFollowRepository = cardFollowRepository;
        this.tableActivityMapper = tableActivityMapper;
    }

    @Override
    public List<TableActivityResponse> readNotifications(String accountId) throws DataNotFoundException {
        Account account = getAccountFromAuthenticationName(accountId);
        List<Card> cards = cardFollowRepository
                .findCardFollowsByAccount(account)
                .stream()
                .map(CardFollow::getCard)
                .toList();
        return activityRepository
                .findActivitiesByCardIn(cards)
                .stream()
                .filter(activity -> activity.getCreatedAt().isAfter(account.getNotification()) && activity.getCreatedAt().isBefore(LocalDateTime.now()))
                .map(tableActivityMapper)
                .toList();
    }

    private Account getAccountFromAuthenticationName(String accountId) throws DataNotFoundException {
        return accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy tài khoản"));
    }
}
