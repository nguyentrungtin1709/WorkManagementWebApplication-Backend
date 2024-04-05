package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.card.BasicUpdateRequest;
import com.application.WorkManagement.dto.requests.card.PositionUpdateRequest;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.exceptions.custom.CustomAccessDeniedException;
import com.application.WorkManagement.exceptions.custom.DataNotFoundException;
import com.application.WorkManagement.exceptions.custom.InvalidPositionException;

import java.util.UUID;

public interface CardService {

    CardResponse readCardById(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    CardResponse updateCard(String accountId, UUID cardId, BasicUpdateRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    CardResponse updatePositionOfCard(String accountId, UUID cardId, PositionUpdateRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException;

    void deleteCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;
}
