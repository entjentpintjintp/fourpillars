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

        // 1. 줄바꿈 문자를 <br> 태그로 변환 (Jsoup이 무시하지 않도록)
        // 주의: HTML 태그 사이에 있는 줄바꿈도 변환될 수 있으나, P 태그가 아닌 텍스트 입력 위주로 가정함
        String converted = content.replace("\r\n", "<br>").replace("\n", "<br>");

        // 2. Jsoup Clean (WhiteList 기반)
        // 기본적으로 허용할 태그 설정 (p, br, b, strong, i, em, u, ul, ol, li, h1~h6 등)
        Safelist safelist = Safelist.basicWithImages();

        // 추가적으로 허용할 태그나 속성이 있다면 여기서 설정
        // 예: safelist.addTags("div", "span");
        // 예: safelist.addAttributes("div", "class", "style");

        return Jsoup.clean(converted, safelist);
    }
}
