package io.github.benkoff.tymofiivsky.repository;

import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PageableRoomRepository extends PagingAndSortingRepository<RoomEntity, Long> {
    Page<RoomEntity> findById(Long id, Pageable pageable);

    List<RoomEntity> findAll();
}
