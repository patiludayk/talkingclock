package com.talking.clock.controller;

import com.talking.clock.dto.HumanFriendlyTime;
import com.talking.clock.service.TimeConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ReadTimeController {

    private TimeConversionService timeConversionService;

    @Autowired
    public ReadTimeController(TimeConversionService timeConversionService) {
        this.timeConversionService = timeConversionService;
    }

    @GetMapping(value = {"timeinwords", "timeinwords/{time}"})
    public ResponseEntity<HumanFriendlyTime> getTime(@PathVariable("time") Optional<String> time) {
        try {
            return new ResponseEntity<>(HumanFriendlyTime.builder().value(timeConversionService.humanFriendlyTime(time).get(0)).build(), HttpStatus.OK);
        } catch (IllegalArgumentException illegalArgumentException) {
            return new ResponseEntity<>(HumanFriendlyTime.builder().error("invalid input.").build(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HumanFriendlyTime.builder().error(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
