package io.github.benkoff.tymofiivsky.repository;

import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends CrudRepository<RoomEntity, Long> {
    Optional<RoomEntity> findById(Long id);

    List<RoomEntity> findAll();
}
