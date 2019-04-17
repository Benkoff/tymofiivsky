package io.github.benkoff.tymofiivsky.repository;

import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {
}
