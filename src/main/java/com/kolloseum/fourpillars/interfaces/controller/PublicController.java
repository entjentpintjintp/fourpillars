package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fourpillars")
@RequiredArgsConstructor
public class PublicController {

    private final AuthorizationService authorizationService;

    @GetMapping(value = "/terms/{type}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getTerms(@PathVariable String type) {
        String content = authorizationService.getPublicTermsContent(type);
        // Wrap with HTML to ensure proper display
        String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body { font-family: sans-serif; line-height: 1.6; padding: 20px; white-space: pre-wrap; word-wrap: break-word; }
                    </style>
                </head>
                <body>
                %s
                </body>
                </html>
                """
                .formatted(content != null ? content : "");
        return ResponseEntity.ok(htmlContent);
    }
}
