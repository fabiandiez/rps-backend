package tech.fdiez.rps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.fdiez.rps.model.entity.MatchEntity;


public interface MatchRepository extends JpaRepository<MatchEntity, Long> {
}
