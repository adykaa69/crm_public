package hu.bhr.crm.repository;

import hu.bhr.crm.model.TaskStatus;
import hu.bhr.crm.repository.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("""
           UPDATE TaskEntity t
           SET t.completedAt = CURRENT_TIMESTAMP
           WHERE t.status = :status AND t.id = :id""")
    void setCompletedAtIfCompleted(@Param("id") UUID id, @Param("status") TaskStatus status);

    List<TaskEntity> findAllByCustomerId(UUID customerId);
}
