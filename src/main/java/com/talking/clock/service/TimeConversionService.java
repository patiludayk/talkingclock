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

    private ScriptRunner scriptRunner;

    @Autowired
    public TimeConversionService(ScriptRunner scriptRunner) {
        this.scriptRunner = scriptRunner;
    }

    /**
     * returns given time in words if present or else current time in words
     * @param time
     * @return string time in word
     */
    public String readTime(Optional<String> time) {

        if (time.isPresent() && !time.get().matches(timeRegex)) {
            log.error("invalid time format or character found. {}", time.get());
            throw new IllegalArgumentException("invalid time format or character found");
        }
        final String timeInWord = scriptRunner.getTime(time).get(0);
        return timeInWord.substring(0, 1).toUpperCase() + timeInWord.substring(1).toLowerCase();
    }
}
