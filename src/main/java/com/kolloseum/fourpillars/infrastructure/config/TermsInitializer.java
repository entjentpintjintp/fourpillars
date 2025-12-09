//package com.kolloseum.fourpillars.infrastructure.config;
//
//import com.kolloseum.fourpillars.domain.model.enums.TermsType;
//import com.kolloseum.fourpillars.infrastructure.persistence.entity.TermsEntity;
//import com.kolloseum.fourpillars.infrastructure.persistence.repository.TermsJpaRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class TermsInitializer implements CommandLineRunner {
//
//    private final TermsJpaRepository termsJpaRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) throws Exception {
//        initializeTerms(TermsType.SERVICE, "0.0.1", "서비스 이용 약관");
//        initializeTerms(TermsType.PRIVACY, "0.0.1", "개인정보 이용 동의");
//    }
//
//    private void initializeTerms(TermsType type, String version, String content) {
//        if (termsJpaRepository.findByTermsTypeAndTermsVersion(type, version).isEmpty()) {
//            log.info("Initializing {} terms version {}", type, version);
//            TermsEntity terms = new TermsEntity();
//            terms.setTermsType(type);
//            terms.setTermsVersion(version);
//            terms.setTermsContent(content);
//            terms.setCreatedAt(LocalDateTime.now());
//            termsJpaRepository.save(terms);
//        } else {
//            log.info("{} terms version {} already exists.", type, version);
//        }
//    }
//}
