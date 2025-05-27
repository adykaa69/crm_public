package hu.bhr.crm.repository;

import hu.bhr.crm.repository.entity.ResidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResidenceRepository extends JpaRepository<ResidenceEntity, UUID> {
}
