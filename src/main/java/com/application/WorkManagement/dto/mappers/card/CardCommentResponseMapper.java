package com.application.WorkManagement.dto.mappers.card;

import com.application.WorkManagement.dto.responses.card.comment.CardCommentResponse;
import com.application.WorkManagement.dto.responses.card.comment.CardMemberData;
import com.application.WorkManagement.entities.Comment;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CardCommentResponseMapper implements Function<Comment, CardCommentResponse> {

    @Override
    public CardCommentResponse apply(Comment comment) {
        return CardCommentResponse
                .builder()
                .id(comment.getUuid())
                .member(
                    CardMemberData
                            .builder()
                            .id(comment.getAccount().getUuid())
                            .name(comment.getAccount().getName())
                            .email(comment.getAccount().getEmail())
                            .avatar(comment.getAccount().getAvatar())
                            .build()
                )
                .comment(comment.getComment())
                .update(comment.getUpdate())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
