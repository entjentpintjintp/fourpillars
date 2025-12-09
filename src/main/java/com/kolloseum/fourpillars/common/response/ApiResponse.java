package com.kolloseum.fourpillars.common.response;


import com.kolloseum.fourpillars.common.utils.TimeUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    private String code;
    private String time;
    private T data;

    private ApiResponse(String code, T data) {
        this.code = code;
        this.data = data;
        this.time = TimeUtils.getCurrentTimeFormatted();

    }

    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>("SUCCESS", data);
    }

    public static ApiResponse<Void> success(){
        return new ApiResponse<>("SUCCESS", null);
    }

    public static <T> ApiResponse<T> error(String code){
        return new ApiResponse<>(code, null);
    }
}
