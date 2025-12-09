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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    public String totpPage(java.security.Principal principal,
            HttpSession session,
            Model model,
            @RequestParam(required = false) Boolean reset) {

        // 1. Local Admin Bypass
        if (principal != null && principal.getName().equals("admin")) {
            session.setAttribute("TOTP_VERIFIED", true);
            return "redirect:/admin/terms";
        }

        // 2. OAuth Admin
        if (principal instanceof OAuth2User oauth2User) {
            String socialId = oauth2User.getName();
            OAuth oAuth = OAuth.of(socialId, Provider.GOOGLE);

            User user = userRepository.findByOAuth(oAuth)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Check ROLE
            if (user.getRole() != com.kolloseum.fourpillars.domain.model.enums.Role.ADMIN) {
                return "redirect:/admin/login?error=not_admin";
            }

            // EMERGENCY RESET (via URL param)
            if (Boolean.TRUE.equals(reset)) {
                user = user.disableTotp();
                userRepository.save(user); // Force clear secret in DB
                session.removeAttribute("TOTP_VERIFIED");
                session.removeAttribute("TEMP_TOTP_SECRET");
            }

            // Already verified?
            if (session.getAttribute("TOTP_VERIFIED") != null) {
                return "redirect:/admin/terms";
            }

            // Setup needed? (If secret is null OR empty)
            if (user.getTotpSecret() == null || user.getTotpSecret().trim().isEmpty()) {
                String tempSecret = (String) session.getAttribute("TEMP_TOTP_SECRET");
                if (tempSecret == null) {
                    tempSecret = totpService.generateSecret();
                    session.setAttribute("TEMP_TOTP_SECRET", tempSecret);
                }
                String qrCodeUrl = totpService.getQrCodeUrl(tempSecret, "KolloseumAdmin");
                model.addAttribute("qrCodeUrl", qrCodeUrl);
                model.addAttribute("secret", tempSecret);
                model.addAttribute("isSetup", true);
            }
        }

        return "admin/totp";
    }

    @PostMapping("/totp/verify")
    public String verifyTotp(@RequestParam Integer code,
            java.security.Principal principal,
            HttpSession session,
            Model model) {

        // 1. Local Admin Bypass
        if (principal != null && principal.getName().equals("admin")) {
            session.setAttribute("TOTP_VERIFIED", true);
            return "redirect:/admin/terms";
        }

        // 2. OAuth Admin
        if (principal instanceof OAuth2User oauth2User) {
            String socialId = oauth2User.getName();
            OAuth oAuth = OAuth.of(socialId, Provider.GOOGLE);

            User user = userRepository.findByOAuth(oAuth)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String secretToVerify;
            boolean isSetup = false;

            if (user.getTotpSecret() != null) {
                // Normal verification
                secretToVerify = user.getTotpSecret();
            } else {
                // Setup verification (use temp secret)
                secretToVerify = (String) session.getAttribute("TEMP_TOTP_SECRET");
                isSetup = true;
            }

            if (secretToVerify != null && totpService.verify(secretToVerify, code)) {
                if (isSetup) {
                    // Save new secret to DB
                    User updatedUser = user.enableTotp(secretToVerify);
                    userRepository.save(updatedUser);
                    session.removeAttribute("TEMP_TOTP_SECRET");
                }
                session.setAttribute("TOTP_VERIFIED", true);
                return "redirect:/admin/terms";
            } else {
                model.addAttribute("error", "Invalid TOTP code");
                // If setup failed, we need to show QR code again
                if (isSetup) {
                    String qrCodeUrl = totpService.getQrCodeUrl(secretToVerify, "KolloseumAdmin");
                    model.addAttribute("qrCodeUrl", qrCodeUrl);
                    model.addAttribute("secret", secretToVerify);
                    model.addAttribute("isSetup", true);
                }
                return "admin/totp";
            }
        }

        return "redirect:/admin/login?error";
    }

    @GetMapping("/terms")
    public String termsPage(Model model, HttpSession session) {
        if (session.getAttribute("TOTP_VERIFIED") == null) {
            return "redirect:/admin/totp";
        }
        model.addAttribute("termsList", adminTermsService.getAllTerms());
        return "admin/terms";
    }

    @PostMapping("/terms")
    public String createTerms(@RequestParam TermsType type,
            @RequestParam String version,
            @RequestParam String content,
            HttpSession session) {

        if (session.getAttribute("TOTP_VERIFIED") == null) {
            return "redirect:/admin/totp";
        }

        adminTermsService.createTerms(type, version, content);
        return "redirect:/admin/terms";
    }
}
