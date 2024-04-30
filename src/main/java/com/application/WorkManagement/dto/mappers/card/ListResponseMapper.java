package com.application.WorkManagement.dto.mappers.card;

import com.application.WorkManagement.dto.responses.card.ListResponse;
import com.application.WorkManagement.entities.ListEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ListResponseMapper implements Function<ListEntity, ListResponse> {

    @Override
    public ListResponse apply(ListEntity listEntity) {
        return ListResponse
                .builder()
                .id(listEntity.getUuid())
                .name(listEntity.getName())
                .cardId(listEntity.getCard().getUuid())
                .createdAt(listEntity.getCreatedAt())
                .build();
    }

}
