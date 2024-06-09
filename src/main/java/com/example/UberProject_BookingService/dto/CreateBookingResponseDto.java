package com.example.UberProject_BookingService.dto;

import com.example.UberProject_EntityService.models.Driver;
import lombok.*;


import java.util.Optional;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingResponseDto {

    private Long bookingId;
    private String bookingstatus;
    private Optional<Driver> driver;
}
