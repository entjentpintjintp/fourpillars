package com.kolloseum.fourpillars.interfaces.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class NoticeWrapper {
    private List<NoticeListResponse> notices;
}
