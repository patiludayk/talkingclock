package com.talking.clock.controller;

import com.talking.clock.dto.HumanFriendlyTime;
import com.talking.clock.service.TimeConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ReadTimeController {

    @Autowired
    TimeConversionService timeConversionService;

    @GetMapping(value = {"/timeinwords", "/timeinwords/{time}"})
    public ResponseEntity<HumanFriendlyTime> getTime(Optional<String> time) {
        try {
            return new ResponseEntity<>(HumanFriendlyTime.builder().value(timeConversionService.humanFriendlyTime(time)).build(), HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>(HumanFriendlyTime.builder().error("invalid character in input time").build(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity<>(HumanFriendlyTime.builder().error("something went wrong").build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
