package io.github.benkoff.tymofiivsky.converter;

import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import io.github.benkoff.tymofiivsky.model.request.ReservationRequest;
import org.springframework.core.convert.converter.Converter;

import java.util.Optional;

public class ReservationRequestToReservationEntityConverter implements Converter<ReservationRequest, ReservationEntity> {
    @Override
    public ReservationEntity convert(final ReservationRequest source) {
        ReservationEntity reservationEntity = new ReservationEntity(source.getCheckin(), source.getCheckout());
        Optional.ofNullable(source.getId()).ifPresent(reservationEntity::setId);

        return reservationEntity;
    }
}
