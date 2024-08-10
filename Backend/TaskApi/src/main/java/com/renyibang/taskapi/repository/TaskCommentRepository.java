package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    Page<TaskComment> findByTaskTaskId(long taskId, Pageable pageable);

    boolean existsByTaskAndCommenterId(Task task, long userId);
}
