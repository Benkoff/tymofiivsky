package io.github.benkoff.tymofiivsky.model.response;

import java.time.LocalDate;

public class ReservationResponse {

    private Long id;
    private LocalDate checkin;
    private LocalDate checkout;

    public ReservationResponse() {
    }

    public ReservationResponse(final Long id, final LocalDate checkin, final LocalDate checkout) {
        this.id = id;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getCheckin() {
        return checkin;
    }

    public void setCheckin(final LocalDate checkin) {
        this.checkin = checkin;
    }

    public LocalDate getCheckout() {
        return checkout;
    }

    public void setCheckout(final LocalDate checkout) {
        this.checkout = checkout;
    }

    @Override
    public String toString() {
        return "ReservationResponse{" +
                "id=" + id +
                ", checkin=" + checkin +
                ", checkout=" + checkout +
                '}';
    }
}
