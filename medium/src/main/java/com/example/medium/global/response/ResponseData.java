package com.example.medium.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseData<T> {
    private String resultCode;
    private String msg;
    private T data;

    public static <T> ResponseData<T> of(String resultCode, String msg, T data) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setResultCode(resultCode);
        responseData.setMsg(msg);
        responseData.setData(data);

        return responseData;
    }

    public static <T> ResponseData<T> of(String resultCode, String msg) {
        ResponseData<T> responseData = new ResponseData<>();
        responseData.setResultCode(resultCode);
        responseData.setMsg(msg);

        return responseData;
    }
}
