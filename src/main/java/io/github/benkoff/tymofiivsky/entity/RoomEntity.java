package io.github.benkoff.tymofiivsky.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Room")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String roomNumber;

    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<ReservationEntity> reservations = new HashSet<>();

    public RoomEntity() {
    }

    public RoomEntity(@NotNull String roomNumber, @NotNull Integer price) {
        this.roomNumber = roomNumber;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<ReservationEntity> getReservations() {
        return reservations;
    }

    public RoomEntity addReservation(final ReservationEntity reservationEntity) {
        reservations.add(reservationEntity);
        reservationEntity.setRoom(this);

        return this;
    }

    public void removeReservation(final ReservationEntity reservationEntity) {
        if (reservationEntity != null) {
            reservations.remove(reservationEntity);
            reservationEntity.setRoom(null);
        }
    }

    public Set<LocalDate> getDates() {
        return this.reservations.stream()
                .map(ReservationEntity::getDates)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomEntity that = (RoomEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(roomNumber, that.roomNumber) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, price);
    }

    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                '}';
    }
}
