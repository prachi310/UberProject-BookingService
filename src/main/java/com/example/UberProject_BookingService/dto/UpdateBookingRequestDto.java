package com.example.UberProject_BookingService.dto;

import com.example.UberProject_EntityService.models.BookingStatus;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingRequestDto {

    private String status;

    private Optional<Long> driverId;
}
