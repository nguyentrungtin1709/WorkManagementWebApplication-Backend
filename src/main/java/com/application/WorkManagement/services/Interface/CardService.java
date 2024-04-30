package com.application.WorkManagement.services.Interface;

import com.application.WorkManagement.dto.requests.card.*;
import com.application.WorkManagement.dto.responses.card.CardListResponse;
import com.application.WorkManagement.dto.responses.card.CardMemberResponse;
import com.application.WorkManagement.dto.responses.card.CardResponse;
import com.application.WorkManagement.dto.responses.card.ListResponse;
import com.application.WorkManagement.dto.responses.card.comment.CardCommentResponse;
import com.application.WorkManagement.dto.responses.table.TableMemberResponse;
import com.application.WorkManagement.exceptions.custom.*;

import java.util.List;
import java.util.UUID;

public interface CardService {

    CardResponse readCardById(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    CardResponse updateCard(String accountId, UUID cardId, BasicUpdateRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    CardResponse updatePositionOfCard(String accountId, UUID cardId, PositionUpdateRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidPositionException;

    void deleteCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    CardMemberResponse addMemberToCard(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    CardMemberResponse joinInCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    List<TableMemberResponse> readMemberListInTableButNotInCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    List<CardMemberResponse> readMembersListOfCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    CardMemberResponse readMemberById(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteMemberOfCard(String accountId, UUID cardId, UUID memberId) throws DataNotFoundException, CustomAccessDeniedException;

    void leaveMembersList(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    void followCard(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException, CustomDuplicateException;

    void unfollowCard(String accountId, UUID cardId) throws CustomAccessDeniedException, DataNotFoundException;

    void setDeadline(String accountId, UUID cardId, DeadlineRequest request) throws DataNotFoundException, CustomAccessDeniedException, InvalidDeadlineException, CustomDuplicateException;

    void removeDeadline(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    void updateCompleteFieldOfDeadline(String accountId, UUID cardId, CompleteDeadlineRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    CardCommentResponse createComment(String accountId, UUID cardId, CommentRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    List<CardCommentResponse> readCommentsList(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException;

    CardCommentResponse readComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException;

    CardCommentResponse revokeComment(String accountId, UUID cardId, UUID commentId) throws DataNotFoundException, CustomAccessDeniedException;

    ListResponse createToDoList(String accountId, UUID cardId, ListRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    List<ListResponse> readToDoLists(String accountId, UUID cardId) throws DataNotFoundException, CustomAccessDeniedException;

    ListResponse readToDoListById(String accountId, UUID cardId, UUID listId) throws DataNotFoundException, CustomAccessDeniedException;

    ListResponse updateToDoList(String accountId, UUID cardId, UUID listId, ListRequest request) throws DataNotFoundException, CustomAccessDeniedException;

    void deleteToDoList(String accountId, UUID cardId, UUID listId) throws DataNotFoundException, CustomAccessDeniedException;
}
