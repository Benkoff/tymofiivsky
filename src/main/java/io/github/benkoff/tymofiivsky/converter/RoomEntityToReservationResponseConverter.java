package io.github.benkoff.tymofiivsky.converter;

import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import io.github.benkoff.tymofiivsky.model.Links;
import io.github.benkoff.tymofiivsky.model.Self;
import io.github.benkoff.tymofiivsky.model.response.ReservationResponse;
import io.github.benkoff.tymofiivsky.rest.ResourceConstants;
import org.springframework.core.convert.converter.Converter;

public class RoomEntityToReservationResponseConverter implements Converter<RoomEntity, ReservationResponse> {
    @Override
    public ReservationResponse convert(RoomEntity source) {
        ReservationResponse response = new ReservationResponse();
        response.setId(source.getId());
        response.setRoomNumber(source.getRoomNumber());
        response.setPrice(source.getPrice());
        Links links = new Links();
        Self self = new Self();
        self.setRef(ResourceConstants.ROOM_RESERVATION_V1 + "/" + source.getId());
        links.setSelf(self);
        response.setLinks(links);

        return response;
    }
}
