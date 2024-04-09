package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Account;
import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.CardMember;
import com.application.WorkManagement.entities.CompositePrimaryKeys.CardCompositeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardMemberRepository extends JpaRepository<CardMember, CardCompositeId> {

    Boolean existsCardMemberByAccountAndCard(Account account, Card card);

    List<CardMember> findCardMembersByCard(Card card);

    Optional<CardMember> findCardMemberByAccountAndCard(Account account, Card card);

    void deleteCardMemberByAccountAndCard(Account account, Card card);

}
