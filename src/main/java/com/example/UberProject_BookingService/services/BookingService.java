package com.example.UberProject_BookingService.services;

import com.example.UberProject_BookingService.dto.CreateBookingResponseDto;
import com.example.UberProject_BookingService.dto.CreatingBookingDto;
import com.example.UberProject_BookingService.dto.UpdateBookingRequestDto;
import com.example.UberProject_BookingService.dto.UpdateBookingResponseDto;
import com.example.UberProject_EntityService.models.Booking;
import org.springframework.stereotype.Service;


public interface BookingService {

    public CreateBookingResponseDto createBooking(CreatingBookingDto booking);

    public UpdateBookingResponseDto updateBooking(UpdateBookingRequestDto requestDto,Long bookingId);
}
