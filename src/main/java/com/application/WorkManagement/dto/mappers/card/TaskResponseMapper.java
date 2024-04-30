package com.application.WorkManagement.dto.mappers.card;

import com.application.WorkManagement.dto.responses.card.TaskResponse;
import com.application.WorkManagement.entities.TaskEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TaskResponseMapper implements Function<TaskEntity, TaskResponse> {

    @Override
    public TaskResponse apply(TaskEntity taskEntity) {
        return TaskResponse
                .builder()
                .id(taskEntity.getUuid())
                .name(taskEntity.getName())
                .complete(taskEntity.getComplete())
                .listId(taskEntity.getListEntity().getUuid())
                .cardId(taskEntity.getListEntity().getCard().getUuid())
                .createdAt(taskEntity.getCreatedAt())
                .build();
    }

}
