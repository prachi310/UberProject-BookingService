package com.example.UberProject_BookingService.controllers;

import com.example.UberProject_BookingService.dto.CreateBookingResponseDto;
import com.example.UberProject_BookingService.dto.CreatingBookingDto;
import com.example.UberProject_BookingService.dto.UpdateBookingResponseDto;
import com.example.UberProject_BookingService.dto.UpdateBookingRequestDto;
import com.example.UberProject_BookingService.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

   @PostMapping("/{bookingId}")
    public ResponseEntity<UpdateBookingResponseDto> updateBooking
            (@RequestBody UpdateBookingRequestDto requestDto,
             @PathVariable Long bookingId){
        return new ResponseEntity<>(bookingService.updateBooking(requestDto,bookingId),HttpStatus.OK);

    }


}
