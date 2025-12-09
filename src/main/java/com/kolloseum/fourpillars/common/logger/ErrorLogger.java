package com.kolloseum.fourpillars.common.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorLogger {

    public void logError(String code, String time, String message){
        log.error("code: {}, time: {}, message: {}",
                code != null ? code : "Unknown Error",
                time,
                message != null ? message : "No message provided");
    }

}
