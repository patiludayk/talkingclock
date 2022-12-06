package com.talking.clock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TimeConversionService {

    private final String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

    @Autowired
    private ScriptRunner scriptRunner;

    public List<String> humanFriendlyTime(Optional<String> time) {

        if (time.isPresent() && !time.get().matches(timeRegex)) {
            log.error("invalid time format or character found. {}", time.get());
            throw new IllegalArgumentException("invalid time format or character found");
        }
        return scriptRunner.getTime(time);
    }
}
