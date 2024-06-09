package com.example.UberProject_BookingService.services;

import com.example.UberProject_BookingService.dto.CreateBookingResponseDto;
import com.example.UberProject_BookingService.dto.CreatingBookingDto;
import com.example.UberProject_BookingService.dto.DriverLocationDto;
import com.example.UberProject_BookingService.dto.NearByDriverRequestDto;
import com.example.UberProject_BookingService.repositories.BookingRepository;
import com.example.UberProject_BookingService.repositories.PassengerRepository;
import com.example.UberProject_EntityService.models.Booking;
import com.example.UberProject_EntityService.models.BookingStatus;
import com.example.UberProject_EntityService.models.Passenger;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private final RestTemplate restTemplate;

    private static final String LOCATOR_SERVICE = "http://localhost:7777";

    public BookingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository){
        this.passengerRepository=passengerRepository;
        this.bookingRepository=bookingRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public CreateBookingResponseDto createBooking(CreatingBookingDto bookingDto) {
        Optional<Passenger> passenger = passengerRepository.findById(bookingDto.getPassengerId());
        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.ASSIGNING_DRIVER)
                .startLocation(bookingDto.getStartLocation())
                .endLocation(bookingDto.getEndLocation())
                .passenger(passenger.get())
                .build();
        Booking newBooking =bookingRepository.save(booking);

        //make API call to location service to fetch nearBy driver
        NearByDriverRequestDto request = NearByDriverRequestDto.builder()
                .latitude(bookingDto.getStartLocation().getLatitude())
                .longitude(bookingDto.getStartLocation().getLongitude()).build();

        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATOR_SERVICE +
                "/api/location/nearByDriver", request, DriverLocationDto[].class);

       List<DriverLocationDto> driverLocations = Arrays.asList(Objects.requireNonNull(result.getBody()));

       if(result.getStatusCode().is2xxSuccessful()) {
           driverLocations.forEach(driverLocationDto -> {
               System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() +
                       "long: " + driverLocationDto.getLongitude());
           });
       }

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingstatus(newBooking.getBookingStatus().toString())
                .build();
    }
}
