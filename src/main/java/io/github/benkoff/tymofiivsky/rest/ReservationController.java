package io.github.benkoff.tymofiivsky.rest;

import io.github.benkoff.tymofiivsky.converter.RoomEntityToReservableRoomResponseConverter;
import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import io.github.benkoff.tymofiivsky.model.request.ReservationRequest;
import io.github.benkoff.tymofiivsky.model.response.ReservableRoomResponse;
import io.github.benkoff.tymofiivsky.repository.PageableRoomRepository;
import io.github.benkoff.tymofiivsky.repository.ReservationRepository;
import io.github.benkoff.tymofiivsky.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(ResourceConstants.ROOM_RESERVATION_V1)
public class ReservationController {
    private final PageableRoomRepository pageableRoomRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ConversionService conversionService;

    @Autowired
    public ReservationController(final PageableRoomRepository pageableRoomRepository,
                                 final RoomRepository roomRepository,
                                 final ReservationRepository reservationRepository,
                                 final ConversionService conversionService) {
        this.pageableRoomRepository = pageableRoomRepository;
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.conversionService = conversionService;
    }

    @RequestMapping(path = "/rooms", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<ReservableRoomResponse> getAvailableRooms(
            @RequestParam(value = "checkin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate checkin,
            @RequestParam(value = "checkout")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate checkout,
            Pageable pageable) {
        Page<RoomEntity> roomsList = pageableRoomRepository.findAll(pageable);

        return roomsList.map(source -> new RoomEntityToReservableRoomResponseConverter().convert(source));
    }

    @RequestMapping(
            path = "/rooms/{roomId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RoomEntity> getRoomById(@PathVariable Long roomId) {

        return roomRepository.findById(roomId)
                .map(roomEntity -> new ResponseEntity<>(roomEntity, HttpStatus.OK))
                .orElse(new ResponseEntity<>(new RoomEntity(), HttpStatus.NOT_FOUND));
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity createReservation(@RequestBody ReservationRequest reservationRequest) {
        ResponseEntity responseEntity = new ResponseEntity<>(new ReservableRoomResponse(), HttpStatus.BAD_REQUEST);
        if (reservationRequest.getRoomId() == null) {
            return responseEntity;
        }
        Optional<RoomEntity> optional = roomRepository.findById(reservationRequest.getRoomId());

        //TODO Add logic to check the room availability
        if (optional.isPresent()) {
            ReservationEntity reservationEntity = reservationRepository.save(
                    conversionService.convert(reservationRequest, ReservationEntity.class));
            RoomEntity room = optional.get();
            room.addReservation(reservationEntity);
            roomRepository.save(room);
            responseEntity = new ResponseEntity<>(
                    conversionService.convert(room, ReservableRoomResponse.class),
                    HttpStatus.CREATED);
        }

        return responseEntity;
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservableRoomResponse> updateReservation(@RequestBody ReservationRequest reservationRequest) {

        return new ResponseEntity<>(new ReservableRoomResponse(), HttpStatus.OK);
    }

    @RequestMapping(path = "/{reservationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteReservation(@PathVariable long reservationId) {

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
