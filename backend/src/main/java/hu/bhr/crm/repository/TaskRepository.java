package hu.bhr.crm.repository;

import hu.bhr.crm.repository.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Query(
        value = """
            UPDATE task
            SET completed_at = CURRENT_TIMESTAMP
            WHERE id = :id AND status = CAST(:status AS status)
            RETURNING completed_at""",
    nativeQuery = true)
    Instant setCompletedAtIfCompleted(@Param("id") UUID id, @Param("status") String status);

    List<TaskEntity> findAllByCustomerId(UUID customerId);
}
