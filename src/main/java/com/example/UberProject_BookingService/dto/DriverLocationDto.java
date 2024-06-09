package com.example.UberProject_BookingService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverLocationDto {

    String driverId;
    Double latitude;
    Double longitude;
}
