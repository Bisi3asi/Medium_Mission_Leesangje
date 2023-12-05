package com.example.medium.global.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RsData<T> {
    private final String resultCode;
    private final String msg;
    private final T data;

    public static <T> RsData<T> of(String resultCode, String msg, T data){
        return new RsData<>(resultCode, msg, data);
    }
}
