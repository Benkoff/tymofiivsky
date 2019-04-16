package io.github.benkoff.tymofiivsky.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    public RoomEntity() {
    }

    public RoomEntity(@NotNull String roomNumber, @NotNull Integer price) {
        this.roomNumber = roomNumber;
        this.price = price;
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
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                '}';
    }
}
