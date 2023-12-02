package org.psu.java.example.presentation.entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для сущности ResponseHistory
 */
@Repository
public interface ResponseHistoryRepository extends CrudRepository<ResponseHistory, Long> {
}
