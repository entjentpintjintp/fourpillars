package com.kolloseum.fourpillars.common.exception;

import com.kolloseum.fourpillars.interfaces.controller.AdminWebController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = AdminWebController.class)
public class AdminWebExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        log.error("Admin Web Error", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "admin/error";
    }
}
