package com.kolloseum.fourpillars.interfaces.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeRequest {
    private String title;
    private String content;

    public NoticeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
