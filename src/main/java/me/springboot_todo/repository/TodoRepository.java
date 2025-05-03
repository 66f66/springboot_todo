package me.springboot_todo.repository;

import jakarta.persistence.LockModeType;
import me.springboot_todo.entity.Todo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Todo t WHERE t.id = :id AND t.user.id = :userId")
    Optional<Todo> findByIdAndUserIdForUpdate(Long id, Long userId);

    @Query("SELECT MAX(t.orderNumber) FROM Todo t WHERE t.user.id = :userId")
    Optional<Integer> findMaxOrderNumberByUserId(@Param("userId") Long userId);

    List<Todo> findByUserIdOrderByOrderNumber(Long userId);
}
