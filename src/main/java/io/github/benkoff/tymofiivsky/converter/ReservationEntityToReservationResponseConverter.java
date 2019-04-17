package io.github.benkoff.tymofiivsky.converter;

import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import io.github.benkoff.tymofiivsky.model.response.ReservationResponse;
import org.springframework.core.convert.converter.Converter;

public class ReservationEntityToReservationResponseConverter implements Converter<ReservationEntity, ReservationResponse> {
    @Override
    public ReservationResponse convert(final ReservationEntity source) {
        return new ReservationResponse(source.getId(), source.getCheckin(), source.getCheckout());
    }
}
