package com.example.UberProject_BookingService.dto;

import com.example.UberProject_EntityService.models.ExactLocation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatingBookingDto {

    private Long passengerId;
    private ExactLocation startLocation;
    private ExactLocation endLocation;
}
