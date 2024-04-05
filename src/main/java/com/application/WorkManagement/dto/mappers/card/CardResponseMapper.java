package com.application.WorkManagement.dto.mappers.card;

import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.dto.responses.card.DeadlineResponse;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Deadline;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.function.BiFunction;

@Service
public class CardResponseMapper implements BiFunction<Card, Boolean, CardResponse> {


    @Override
    public CardResponse apply(Card card, Boolean isFollow) {
        Deadline deadline = card.getDeadline();
        DeadlineResponse deadlineResponse = null;
        if (Objects.nonNull(deadline)){
            deadlineResponse = DeadlineResponse
                    .builder()
                    .id(deadline.getUuid())
                    .deadline(deadline.getDeadline())
                    .reminderDate(deadline.getReminderDate())
                    .complete(deadline.getComplete())
                    .build();
        }
        return CardResponse
                .builder()
                .id(card.getUuid())
                .name(card.getName())
                .description(card.getDescription())
                .progress(card.getProgress())
                .location(card.getLocation())
                .createdAt(card.getCreatedAt())
                .categoryId(card.getCategory().getUuid())
                .isFollow(isFollow)
                .deadline(deadlineResponse)
                .build();
    }
}
