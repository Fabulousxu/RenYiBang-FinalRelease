package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.entity.TaskCollect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskCollectRepository extends JpaRepository<TaskCollect, Long> {
    boolean existsByCollectorIdAndAndTask(long collectorId, Task task);

    TaskCollect findByTaskAndCollectorId(Task task, long collectorId);

    Page<TaskCollect> findByCollectorId(long userId, Pageable pageable);
}
