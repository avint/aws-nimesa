package com.nimesa.aws.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseStatus {
    private String code;
    private String status;
    private String message;
    private LocalDateTime time;
}
