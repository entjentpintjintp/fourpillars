package com.kolloseum.fourpillars.interfaces.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeRequest {
    private String title;
    private String content;

}
