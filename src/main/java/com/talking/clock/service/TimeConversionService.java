package com.talking.clock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TimeConversionService {

    private final String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

    public String humanFriendlyTime(Optional<String> time) {
        String timeInWords = null;

        if (time.isPresent() && !time.get().isEmpty()) {
            if(!time.get().matches(timeRegex)) {
                log.error("invalid time format or character found. {}", time.get());
                throw new IllegalArgumentException("invalid time format or character found");
            }
            //return time by converting

        } else {
            //return current time

        }
        return timeInWords;
    }
}
