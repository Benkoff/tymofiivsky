package io.github.benkoff.tymofiivsky.model.response;

import io.github.benkoff.tymofiivsky.model.Links;

public class ReservableRoomResponse {

    private Long id;
    private String roomNumber;
    private Integer price;
    private Links links;

    public ReservableRoomResponse() {
    }

    public ReservableRoomResponse(String roomNumber, Integer price) {
        this.roomNumber = roomNumber;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "ReservableRoomResponse{" +
                "id=" + id +
                ", roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", links=" + links +
                '}';
    }
}
