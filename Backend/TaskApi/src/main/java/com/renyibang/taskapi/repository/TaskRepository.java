package com.renyibang.taskapi.repository;

import com.renyibang.taskapi.entity.Task;
import com.renyibang.taskapi.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Override
    Page<Task> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM task t WHERE " +
            "(MATCH(t.title, t.description) AGAINST (:searchText IN BOOLEAN MODE)) AND " +
            "t.price BETWEEN :priceLow AND :priceHigh AND " +
            "t.created_at BETWEEN :beginDateTime AND :endDateTime AND " +
            "t.status != :status",
            nativeQuery = true)
    Page<Task> searchTasks(@Param("searchText") String searchText,
                           @Param("priceLow") long priceLow,
                           @Param("priceHigh") long priceHigh,
                           @Param("beginDateTime") LocalDateTime beginDateTime,
                           @Param("endDateTime") LocalDateTime endDateTime,
                           @Param("status") TaskStatus status,
                           Pageable pageable);



    Page<Task> findByPriceBetweenAndCreatedAtBetweenAndStatusNot(@Param("priceLow") long priceLow,
                                                     @Param("priceHigh") long priceHigh,
                                                     @Param("beginDateTime") LocalDateTime beginDateTime,
                                                     @Param("endDateTime") LocalDateTime endDateTime,
                                                     @Param("status") TaskStatus status,
                                                     Pageable pageable);

    Page<Task> findByOwnerId(long userId, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.taskId IN " +
            "(SELECT ta.task.taskId FROM TaskAccess ta WHERE ta.accessorId = :userId)")
    Page<Task> findByAccessorId(@Param("userId") long userId, Pageable pageable);
}
