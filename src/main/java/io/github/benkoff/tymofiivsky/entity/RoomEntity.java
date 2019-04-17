package io.github.benkoff.tymofiivsky.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @OneToMany
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

    public Set<ReservationEntity> addReservation(final ReservationEntity reservationEntity) {
        reservations.add(reservationEntity);

        return reservations;
    }

    public Set<ReservationEntity> removeReservation(final ReservationEntity reservationEntity) {
        reservations.remove(reservationEntity);

        return reservations;
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
