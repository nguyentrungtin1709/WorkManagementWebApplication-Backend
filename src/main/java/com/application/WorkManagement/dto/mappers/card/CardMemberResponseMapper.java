package com.application.WorkManagement.dto.mappers.card;

import com.application.WorkManagement.dto.responses.card.CardMemberResponse;
import com.application.WorkManagement.entities.CardMember;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CardMemberResponseMapper implements Function<CardMember, CardMemberResponse> {

    @Override
    public CardMemberResponse apply(CardMember cardMember) {
        return CardMemberResponse
                .builder()
                .id(cardMember.getAccount().getUuid())
                .name(cardMember.getAccount().getName())
                .email(cardMember.getAccount().getEmail())
                .avatar(cardMember.getAccount().getAvatar())
                .createdAt(cardMember.getCreatedAt())
                .build();
    }
}
