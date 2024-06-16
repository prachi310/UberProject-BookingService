package com.example.UberProject_BookingService.services;

import com.example.UberProject_BookingService.apis.LocationServiceApi;
import com.example.UberProject_BookingService.apis.UberSocketApi;
import com.example.UberProject_BookingService.dto.*;
import com.example.UberProject_BookingService.repositories.BookingRepository;
import com.example.UberProject_BookingService.repositories.DriverRepository;
import com.example.UberProject_BookingService.repositories.PassengerRepository;
import com.example.UberProject_EntityService.models.Booking;
import com.example.UberProject_EntityService.models.BookingStatus;
import com.example.UberProject_EntityService.models.Driver;
import com.example.UberProject_EntityService.models.Passenger;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private  final DriverRepository driverRepository;
    private final RestTemplate restTemplate;

    private final LocationServiceApi locationServiceApi;
    private final UberSocketApi uberSocketApi;

    //private static final String LOCATOR_SERVICE = "http://localhost:7777";

    public BookingServiceImpl(PassengerRepository passengerRepository,
                              BookingRepository bookingRepository,
                              LocationServiceApi locationServiceApi,
                              DriverRepository driverRepository,
                              UberSocketApi uberSocketApi){
        this.passengerRepository=passengerRepository;
        this.bookingRepository=bookingRepository;
        this.restTemplate = new RestTemplate();
        this.locationServiceApi=locationServiceApi;
        this.driverRepository=driverRepository;
        this.uberSocketApi=uberSocketApi;
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

        processNearByDriversAsync(request,bookingDto.getPassengerId());
//  Async communication between services via RestTemplate
        //        ResponseEntity<DriverLocationDto[]> result = restTemplate.postForEntity(LOCATOR_SERVICE +
//                "/api/location/nearByDriver", request, DriverLocationDto[].class);
//
//       List<DriverLocationDto> driverLocations = Arrays.asList(Objects.requireNonNull(result.getBody()));
//
//       if(result.getStatusCode().is2xxSuccessful()) {
//           driverLocations.forEach(driverLocationDto -> {
//               System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() +
//                       "long: " + driverLocationDto.getLongitude());
//           });
//       }

        return CreateBookingResponseDto.builder()
                .bookingId(newBooking.getId())
                .bookingstatus(newBooking.getBookingStatus().toString())
                .build();
    }

    @Override
    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto requestDto, Long bookingId) {

        Optional<Driver> driver = driverRepository.findById(requestDto.getDriverId().get());
        bookingRepository.updateBookingStatusAndDriverById
                (bookingId, BookingStatus.SCHEDULED,driver.get());
        Optional<Booking> booking = bookingRepository.findById(bookingId);
        return UpdateBookingResponseDto.builder()
                .bookingId(bookingId)
                .bookingStatus(booking.get().getBookingStatus())
                .driver(Optional.ofNullable(booking.get().getDriver()))
                .build();
    }

    //Async communication between services with the help of retrofit and eureka service discovery
    private void processNearByDriversAsync( NearByDriverRequestDto nearByDriverRequestDto,Long passengerId) {
        Call<DriverLocationDto[]> call= locationServiceApi.getNearByDriver(nearByDriverRequestDto);
        call.enqueue(new Callback<DriverLocationDto[]>() {
            @Override
            public void onResponse(Call<DriverLocationDto[]> call, Response<DriverLocationDto[]> response) {
                if(response.isSuccessful() && response.body() !=null) {
                    List<DriverLocationDto> driverLocations = Arrays.asList(Objects.requireNonNull(response.body()));
                    driverLocations.forEach(driverLocationDto -> {
                        System.out.println(driverLocationDto.getDriverId() + " " + "lat: " + driverLocationDto.getLatitude() +
                       "long: " + driverLocationDto.getLongitude());
           });
                    raiseRideRequest(RideRequestDto.builder()
                            .passengerId(passengerId).build());

                }else {
                    System.out.println("Request Failed " + response.message());
                }
            }

            @Override
            public void onFailure(Call<DriverLocationDto[]> call, Throwable throwable) {
                throwable.printStackTrace();

            }
        });
    }

    private void raiseRideRequest(RideRequestDto rideRequestDto){
        Call<Boolean> call = uberSocketApi.gerNearByDrivers(rideRequestDto);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() !=null) {
                    Boolean result=response.body();
                    System.out.println("Driver Response is : "+ result.toString());
                }else {
                    System.out.println("Request Failed " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
