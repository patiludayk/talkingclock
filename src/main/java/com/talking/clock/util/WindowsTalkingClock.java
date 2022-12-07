package com.talking.clock.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class WindowsTalkingClock {

    private static final String twenty = "twenty";

    private static final String[] numNames = {"", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"};

    public static List<String> getTime(String... command) {

        if (command.length == 3) {
            //convert given time
            final String hour = command[1].split(":")[0];
            final String minute = command[1].split(":")[1];
            return talkingClockUsingJava(hour, minute);
        } else {
            //return current time
            LocalTime time = LocalTime.now();
            String t = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            return talkingClockUsingJava(t.split(":")[0], t.split(":")[1]);
        }
    }

    private static List<String> talkingClockUsingJava(String hour, String minute) {
        int hrs = Integer.MIN_VALUE;
        int min = Integer.MIN_VALUE;

        String verb = "past ", fixed = "";
        if (Integer.parseInt(minute) > 30) {
            verb = " to";
            hrs = Integer.parseInt(hour) + 1;
            min = 60 - Integer.parseInt(minute);
        } else if (Integer.parseInt(minute) == 30) {
            verb = "Half past";
            min = 0;
        } else if (Integer.parseInt(minute) == 0) {
            verb = "";
            fixed = "o' clock";
            min = 0;
        }

        if (hrs == 0 || hrs == 12 || hrs == 24) {
            hrs = 12;
        } else if (hrs > 12) {
            hrs = hrs - 12;
        }

        return Collections.singletonList(convert(min) + verb + convert(hrs) + fixed);
    }

    private static String convert(int number) {
        String soFar;

        if (number < 20) {
            soFar = numNames[number];
        } else if (number == 20) {
            soFar = twenty;
        } else {
            soFar = twenty + numNames[number % 10];
        }
        if (number == 0) return soFar;
        return soFar;
    }

    public static void main(String[] args) {
        System.out.println(getTime("", "12:36", ""));
    }
}
