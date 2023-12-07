package com.example.medium.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseDto<T> {
    private final String resultCode;
    private final String msg;
    private final T data;

    public static <T> ResponseDto<T> of(String resultCode, String msg, T data){
        return new ResponseDto<>(resultCode, msg, data);
    }
}
