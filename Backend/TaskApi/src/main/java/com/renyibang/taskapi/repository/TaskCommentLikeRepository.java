package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.TaskComment;
import com.renyibang.taskapi.entity.TaskCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCommentLikeRepository extends JpaRepository<TaskCommentLike, Long> {
    boolean existsByLikerIdAndTaskComment(long likerId, TaskComment taskComment);

    TaskCommentLike findByLikerIdAndTaskComment(long unlikerId, TaskComment taskComment);
}
