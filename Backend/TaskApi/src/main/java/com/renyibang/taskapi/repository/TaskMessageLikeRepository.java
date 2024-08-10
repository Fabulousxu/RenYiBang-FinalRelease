package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.TaskMessage;
import com.renyibang.taskapi.entity.TaskMessageLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMessageLikeRepository extends JpaRepository<TaskMessageLike, Long> {

    boolean existsByLikerIdAndTaskMessage(long likerId, TaskMessage taskMessage);

    TaskMessageLike findByLikerIdAndTaskMessage(long unlikerId, TaskMessage taskMessage);
}
