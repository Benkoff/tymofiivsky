package io.github.benkoff.tymofiivsky.rest;

import io.github.benkoff.tymofiivsky.converter.RoomEntityToReservableRoomResponseConverter;
import io.github.benkoff.tymofiivsky.entity.ReservationEntity;
import io.github.benkoff.tymofiivsky.entity.RoomEntity;
import io.github.benkoff.tymofiivsky.model.request.ReservationRequest;
import io.github.benkoff.tymofiivsky.model.response.ReservableRoomResponse;
import io.github.benkoff.tymofiivsky.model.response.ReservationResponse;
import io.github.benkoff.tymofiivsky.repository.PageableRoomRepository;
import io.github.benkoff.tymofiivsky.repository.ReservationRepository;
import io.github.benkoff.tymofiivsky.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(ResourceConstants.ROOM_RESERVATION_V1)
public class ReservationController {
    private final PageableRoomRepository pageableRoomRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ConversionService conversionService;
    private final Logger log = LoggerFactory.getLogger(getClass());

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

        //TODO Add available rooms selection logic
        log.info(" Available Rooms from:{} to:{}", checkin, checkout);
        roomsList.map(source -> new RoomEntityToReservableRoomResponseConverter().convert(source))
                .forEach(room ->  log.info("  Response: {}", room));

        return roomsList.map(source -> new RoomEntityToReservableRoomResponseConverter().convert(source));
    }

    @RequestMapping(
            path = "/rooms/{roomId}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservableRoomResponse> getRoomById(@PathVariable Long roomId) {

        return roomRepository.findById(roomId)
                .map(r -> makeResponse(new RoomEntityToReservableRoomResponseConverter().convert(r), HttpStatus.OK))
                .orElseGet(() -> makeResponse(new ReservableRoomResponse(), HttpStatus.NOT_FOUND));
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservableRoomResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        Optional<RoomEntity> optional =
                Optional.ofNullable(reservationRequest.getRoomId()).flatMap(roomRepository::findById);
        if (optional.isPresent()
                && reservationRequest.getCheckin() != null
                && reservationRequest.getCheckout() != null) {
            RoomEntity room = optional.get();
            List<LocalDate> reservedDates = new ArrayList<>();
            room.getReservations().forEach(reservation -> reservedDates.addAll(getDates(reservation)));

            List<LocalDate> requestedDates =
                    Optional.ofNullable(conversionService.convert(reservationRequest, ReservationEntity.class))
                            .map(this::getDates)
                            .orElse(new ArrayList<>());

            if (reservedDates.stream().noneMatch(requestedDates::contains)) {
                ReservationEntity reservationEntity =
                        reservationRepository.save(
                                conversionService.convert(reservationRequest, ReservationEntity.class));
                room.addReservation(reservationEntity);
                roomRepository.save(room);

                return makeResponse(conversionService.convert(room, ReservableRoomResponse.class), HttpStatus.CREATED);
            }

            return makeResponse(new ReservableRoomResponse(), HttpStatus.PRECONDITION_FAILED);
        }

        return makeResponse(new ReservableRoomResponse(), HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(
            path = "",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservableRoomResponse> updateReservation(@RequestBody ReservationRequest reservationRequest) {
        return Optional.ofNullable(reservationRequest.getId())
                .flatMap(reservationRepository::findById)
                .map(entity -> Optional.ofNullable(reservationRequest.getRoomId())
                        .flatMap(roomRepository::findById)
                        .map(room -> {
                            entity.setCheckin(reservationRequest.getCheckin());
                            entity.setCheckout(reservationRequest.getCheckout());
                            roomRepository.save(room);

                            return makeResponse(
                                    conversionService.convert(room, ReservableRoomResponse.class),
                                    HttpStatus.OK);
                        })
                        .orElse(makeResponse(new ReservableRoomResponse(), HttpStatus.NO_CONTENT)))
                .orElse(makeResponse(new ReservableRoomResponse(), HttpStatus.BAD_REQUEST));
    }

    @RequestMapping(path = "/{reservationId}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ReservationResponse> deleteReservation(@PathVariable long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(entity -> {
                    entity.getRoom().removeReservation(entity);
                    reservationRepository.delete(entity);

                    return makeResponse(new ReservationResponse(), HttpStatus.NO_CONTENT);
                })
                .orElse(makeResponse(new ReservationResponse(), HttpStatus.BAD_REQUEST));
    }

    /**
     * Returns list of dates between checkin and checkout, excluding checkout day
     * @param reservation entity with checkin and checkout dates
     * @return list of dates starting checkin date, and ending the day before checkout
     */
    private List<LocalDate> getDates(ReservationEntity reservation) {
        return Stream.iterate(reservation.getCheckin(), date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(reservation.getCheckin(), reservation.getCheckout()))
                .collect(Collectors.toList());
    }

    private <T> ResponseEntity<T> makeResponse(T response, HttpStatus status) {
        log.info("  Response: {} {}", response, status);
        return new ResponseEntity<>(response, status);
    }
}
