package com.kolloseum.fourpillars.interfaces.controller;

import com.kolloseum.fourpillars.application.service.AdminTermsService;
import com.kolloseum.fourpillars.application.service.TotpService;
import com.kolloseum.fourpillars.domain.model.entity.User;
import com.kolloseum.fourpillars.domain.model.enums.Provider;
import com.kolloseum.fourpillars.domain.model.enums.TermsType;
import com.kolloseum.fourpillars.domain.model.vo.OAuth;
import com.kolloseum.fourpillars.domain.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final AdminTermsService adminTermsService;
    private final TotpService totpService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/totp")
    public String totpPage(org.springframework.security.core.Authentication authentication,
            HttpSession session,
            Model model,
            @RequestParam(required = false) String reset,
            @RequestParam(required = false) String forceSetup) {

        // Note: Manual check removed; SecurityConfig sends unauth users to /login here
        // anyway.

        if (authentication == null) {
            return "redirect:/admin/login";
        }

        // 1. Local Admin Bypass
        if (authentication.getName().equals("admin")) {
            session.setAttribute("TOTP_VERIFIED", true);
            return "redirect:/admin/terms";
        }

        // 2. OAuth Admin (Generic Handling)
        try {
            String socialId = authentication.getName();
            System.out.println("DEBUG: Authenticated Social ID = " + socialId);

            OAuth oAuth = OAuth.of(socialId, Provider.GOOGLE);
            User user = userRepository.findByOAuth(oAuth)
                    .orElseThrow(() -> new RuntimeException("User not found for ID: " + socialId));

            // CRITICAL: Check ROLE
            if (user.getRole() != com.kolloseum.fourpillars.domain.model.enums.Role.ADMIN) {
                System.out.println("WARNING: Access Attempt by Non-Admin: " + socialId);
                return "redirect:/admin/login?error=not_admin";
            }

            // RESET Logic (For debugging)
            if ("true".equals(reset)) {
                System.out.println("DEBUG: Executing TOTP RESET");
                user = user.disableTotp();
                userRepository.save(user);
                session.removeAttribute("TOTP_VERIFIED");
                session.removeAttribute("TEMP_TOTP_SECRET");
            }

            // Already verified?
            if (session.getAttribute("TOTP_VERIFIED") != null) {
                return "redirect:/admin/terms";
            }

            // STRICT MODE: Determine if Setup is REQUIRED
            boolean setupRequired = (user.getTotpSecret() == null || user.getTotpSecret().trim().isEmpty());

            // Force Setup Override
            if ("true".equals(forceSetup)) {
                setupRequired = true;
            }

            System.out.println("DEBUG: setupRequired = " + setupRequired);

            // Pass the strict flag to the frontend
            model.addAttribute("setupRequired", setupRequired);

            if (setupRequired) {
                String tempSecret = (String) session.getAttribute("TEMP_TOTP_SECRET");
                if (tempSecret == null) {
                    tempSecret = totpService.generateSecret();
                    session.setAttribute("TEMP_TOTP_SECRET", tempSecret);
                }

                try {
                    String qrCodeUrl = totpService.getQrCodeUrl(tempSecret, "KolloseumAdmin");
                    model.addAttribute("qrCodeUrl", qrCodeUrl);
                    model.addAttribute("secret", tempSecret);
                    model.addAttribute("isSetup", true); // Legacy flag for compatibility
                } catch (Exception e) {
                    System.err.println("CRITICAL: Failed to generate QR Code URL: " + e.getMessage());
                    // Frontend will handle the missing qrCodeUrl in Strict Mode
                }
            } else {
                model.addAttribute("isSetup", false);
            }

        } catch (Exception e) {
            System.err.println("ERROR in TOTP Page: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/admin/login?error=user_lookup_failed";
        }

        return "admin/totp";
    }

    @PostMapping("/totp/verify")
    public String verifyTotp(@RequestParam Integer code,
            org.springframework.security.core.Authentication authentication,
            HttpSession session,
            Model model) {

        if (authentication == null)
            return "redirect:/admin/login";

        // 1. Local Admin
        if (authentication.getName().equals("admin")) {
            session.setAttribute("TOTP_VERIFIED", true);
            return "redirect:/admin/terms";
        }

        // 2. OAuth Admin
        try {
            String socialId = authentication.getName();
            OAuth oAuth = OAuth.of(socialId, Provider.GOOGLE);
            User user = userRepository.findByOAuth(oAuth)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // CRITICAL: Check ROLE
            if (user.getRole() != com.kolloseum.fourpillars.domain.model.enums.Role.ADMIN) {
                return "redirect:/admin/login?error=not_admin";
            }

            String secretToVerify;
            boolean isSetup = false;

            // Strict determination of secret source
            if (user.getTotpSecret() != null) {
                secretToVerify = user.getTotpSecret();
            } else {
                secretToVerify = (String) session.getAttribute("TEMP_TOTP_SECRET");
                isSetup = true;
            }

            if (secretToVerify != null && totpService.verify(secretToVerify, code)) {
                if (isSetup) {
                    user = user.enableTotp(secretToVerify);
                    userRepository.save(user);
                    session.removeAttribute("TEMP_TOTP_SECRET");
                }
                session.setAttribute("TOTP_VERIFIED", true);
                return "redirect:/admin/terms";
            } else {
                model.addAttribute("error", "Invalid TOTP code");
                model.addAttribute("setupRequired", isSetup); // Pass strict flag back

                if (isSetup) {
                    String qrCodeUrl = totpService.getQrCodeUrl(secretToVerify, "KolloseumAdmin");
                    model.addAttribute("qrCodeUrl", qrCodeUrl);
                    model.addAttribute("secret", secretToVerify);
                    model.addAttribute("isSetup", true);
                }
                return "admin/totp";
            }
        } catch (Exception e) {
            return "redirect:/admin/login?error=verify_failed";
        }
    }

    @GetMapping("/terms")
    public String termsPage(Model model, HttpSession session) {
        // Interceptor checks security now.
        model.addAttribute("termsList", adminTermsService.getAllTerms());
        return "admin/terms";
    }

    @PostMapping("/terms")
    public String createTerms(@RequestParam TermsType type,
            @RequestParam String version,
            @RequestParam String content,
            HttpSession session) {

        // Interceptor checks security now.
        adminTermsService.createTerms(type, version, content);
        return "redirect:/admin/terms";
    }
}
