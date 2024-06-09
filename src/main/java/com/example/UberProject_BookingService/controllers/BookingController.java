package com.example.UberProject_BookingService.controllers;

import com.example.UberProject_BookingService.dto.CreateBookingResponseDto;
import com.example.UberProject_BookingService.dto.CreatingBookingDto;
import com.example.UberProject_BookingService.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping
    public ResponseEntity<CreateBookingResponseDto> createBooking
            (@RequestBody CreatingBookingDto bookingDto){

        return new ResponseEntity<>(bookingService.createBooking(bookingDto), HttpStatus.CREATED);

    }


}
