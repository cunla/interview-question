package com.daniyal.forum.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository
        extends JpaRepository<ReplyEntity, Long> {
}
