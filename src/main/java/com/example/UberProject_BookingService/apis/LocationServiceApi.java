package com.example.UberProject_BookingService.apis;

import com.example.UberProject_BookingService.dto.DriverLocationDto;
import com.example.UberProject_BookingService.dto.NearByDriverRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LocationServiceApi {

    @POST("/api/location/nearByDriver")
    Call<DriverLocationDto[]> getNearByDriver(@Body NearByDriverRequestDto driverRequestDto);
}
