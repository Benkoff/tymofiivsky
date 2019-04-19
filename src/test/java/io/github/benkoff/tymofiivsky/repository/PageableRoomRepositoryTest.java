package io.github.benkoff.tymofiivsky.repository;

import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class PageableRoomRepositoryTest {
    @Autowired private PageableRoomRepository repository;

    private List<RoomEntity> rooms;

    @Before
    public void setUp() {
        rooms = Arrays.asList(
                repository.save(new RoomEntity("101", 60)
                        .addReservation(new ReservationEntity(
                                LocalDate.of(2019, 4, 20),
                                LocalDate.of(2019, 4, 21)))),
                repository.save(new RoomEntity("102", 60)
                        .addReservation(new ReservationEntity(
                                LocalDate.of(2019, 4, 22),
                                LocalDate.of(2019, 4, 23)))),
                repository.save(new RoomEntity("103", 30)));
    }

    @Test
    public void shouldReturnSingletonList_whenFindAll() {
        Set<LocalDate> reservedDates = new HashSet<>();
        rooms.forEach(room -> reservedDates.addAll(room.getReservations().stream()
                .map(ReservationEntity::getDates)
                .flatMap(Set::stream)
                .collect(Collectors.toSet())));

        List<RoomEntity> filteredRooms = repository.findAll().stream()
                .filter(roomEntity -> roomEntity.getDates().stream().noneMatch(reservedDates::contains))
                .collect(Collectors.toList());

        assertThat(filteredRooms)
                .containsExactly(rooms.get(2));
    }

    @Test
    public void shouldReturnList_whenFindAll() {
        Set<LocalDate> reservedDates = rooms.get(0).getReservations().stream()
                .map(ReservationEntity::getDates)
                .flatMap(Set::stream).collect(Collectors.toSet());

        List<RoomEntity> filteredRooms = repository.findAll().stream()
                .filter(roomEntity -> roomEntity.getDates().stream().noneMatch(reservedDates::contains))
                .collect(Collectors.toList());

        assertThat(filteredRooms)
                .containsExactlyInAnyOrder(rooms.get(1), rooms.get(2));
    }

    @Test
    public void shouldReturnPage_whenFindAll() {
        Set<LocalDate> reservedDates = new HashSet<>();
        rooms.forEach(room -> reservedDates.addAll(room.getReservations().stream()
                .map(ReservationEntity::getDates)
                .flatMap(Set::stream)
                .collect(Collectors.toSet())));

        List<RoomEntity> filteredRooms = repository.findAll().stream()
                .filter(roomEntity -> roomEntity.getDates().stream().noneMatch(reservedDates::contains))
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(0, 5);
        Page<RoomEntity> page = new PageImpl<>(filteredRooms, pageable, filteredRooms.size());

        assertThat(page)
                .isInstanceOf(Page.class)
                .hasSize(1);
    }

    @After
    public void tearDown() {
        repository.deleteAll();
    }
}
