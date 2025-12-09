package com.kolloseum.fourpillars.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Component;

@Component
public class HtmlSanitizer {

    public String sanitize(String content) {
        if (content == null) {
            return null;
        }

        // 기본적으로 허용할 태그 설정 (p, br, b, strong, i, em, u, ul, ol, li, h1~h6 등)
        Safelist safelist = Safelist.basicWithImages();

        // 추가적으로 허용할 태그나 속성이 있다면 여기서 설정
        // 예: safelist.addTags("div", "span");
        // 예: safelist.addAttributes("div", "class", "style");

        return Jsoup.clean(content, safelist);
    }
}
