package com.application.WorkManagement.repositories;


import com.application.WorkManagement.entities.Card;
import com.application.WorkManagement.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    List<Comment> findCommentsByCardOrderByCreatedAtDesc(Card card);

}
