package com.talking.clock.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HumanFriendlyTime {
    private String value;
    private String error;
}
