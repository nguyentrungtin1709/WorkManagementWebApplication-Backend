package com.application.WorkManagement.repositories;

import com.application.WorkManagement.entities.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, UUID> {

}
