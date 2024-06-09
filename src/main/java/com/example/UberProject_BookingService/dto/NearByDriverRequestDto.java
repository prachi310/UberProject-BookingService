package com.example.UberProject_BookingService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearByDriverRequestDto {

    Double latitude;
    Double longitude;
}
