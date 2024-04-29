package com.example.webchatrealtime.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseResponse<T> {
    private String message;
    private Integer statusCode;
    private T data;

    public BaseResponse(String message, Integer statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }

    public BaseResponse(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
