package io.github.benkoff.tymofiivsky.model.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class ReservationRequest {

    private Long id;
    private Long roomId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate checkin;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) private LocalDate checkout;

    public ReservationRequest() {
    }

    public ReservationRequest(final Long id, final Long roomId, final LocalDate checkin, final LocalDate checkout) {
        this.id = id;
        this.roomId = roomId;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(final Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(LocalDate checkout) {
        this.checkout = checkout;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "id=" + id +
                ", roomId=" + roomId +
                ", checkin=" + checkin +
                ", checkout=" + checkout +
                '}';
    }
}
