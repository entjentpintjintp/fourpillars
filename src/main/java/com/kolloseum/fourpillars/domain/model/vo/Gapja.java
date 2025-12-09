package com.kolloseum.fourpillars.domain.model.vo;

import lombok.*;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "name")
@ToString
public class Gapja {

    public static final Gapja GAPJA_01 = new Gapja("갑자", 1, 20, 18, 18);
    public static final Gapja EULCHUK_02 = new Gapja("을축", 2, 20, 16, 19);
    public static final Gapja BYEONGIN_03 = new Gapja("병인", 3, 17, 14, 15);
    public static final Gapja JEONGMYO_04 = new Gapja("정묘", 4, 16, 12, 14);
    public static final Gapja MUJIN_05 = new Gapja("무진", 5, 18, 10, 16);
    public static final Gapja GISA_06 = new Gapja("기사", 6, 18, 13, 16);
    public static final Gapja GYEONGO_07 = new Gapja("경오", 7, 17, 17, 15);
    public static final Gapja SINMI_08 = new Gapja("신미", 8, 20, 15, 18);
    public static final Gapja IMSIN_09 = new Gapja("임신", 9, 18, 13, 16);
    public static final Gapja GYEYU_10 = new Gapja("계유", 10, 17, 11, 15);

    public static final Gapja GAPSUL_11 = new Gapja("갑술", 11, 22, 14, 20);
    public static final Gapja EULHAE_12 = new Gapja("을해", 12, 19, 12, 17);
    public static final Gapja BYEONGJA_13 = new Gapja("병자", 13, 18, 16, 16);
    public static final Gapja JEONGCHUK_14 = new Gapja("정축", 14, 19, 14, 17);
    public static final Gapja MUIN_15 = new Gapja("무인", 15, 15, 12, 13);
    public static final Gapja GIMYO_16 = new Gapja("기묘", 16, 19, 15, 17);
    public static final Gapja GYEONGJIN_17 = new Gapja("경진", 17, 21, 13, 19);
    public static final Gapja SINSA_18 = new Gapja("신사", 18, 16, 11, 14);
    public static final Gapja IMO_19 = new Gapja("임오", 19, 15, 15, 13);
    public static final Gapja GYEMI_20 = new Gapja("계미", 20, 18, 13, 16);

    public static final Gapja GAPSIN_21 = new Gapja("갑신", 21, 21, 16, 19);
    public static final Gapja EULYU_22 = new Gapja("을유", 22, 20, 14, 18);
    public static final Gapja BYEONGSUL_23 = new Gapja("병술", 23, 20, 12, 18);
    public static final Gapja JEONGHAE_24 = new Gapja("정해", 24, 17, 10, 15);
    public static final Gapja MUJA_25 = new Gapja("무자", 25, 16, 14, 14);
    public static final Gapja GICHUK_26 = new Gapja("기축", 26, 22, 17, 20);
    public static final Gapja GYEONGIN_27 = new Gapja("경인", 27, 18, 15, 16);
    public static final Gapja SINMYO_28 = new Gapja("신묘", 28, 17, 13, 15);
    public static final Gapja IMJIN_29 = new Gapja("임진", 29, 19, 11, 17);
    public static final Gapja GYESA_30 = new Gapja("계사", 30, 14, 9, 12);

    public static final Gapja GAPO_31 = new Gapja("갑오", 31, 18, 18, 16);
    public static final Gapja EULMI_32 = new Gapja("을미", 32, 21, 16, 19);
    public static final Gapja BYEONGSIN_33 = new Gapja("병신", 33, 19, 14, 17);
    public static final Gapja JEONGYU_34 = new Gapja("정유", 34, 18, 12, 16);
    public static final Gapja MUSUL_35 = new Gapja("무술", 35, 18, 10, 16);
    public static final Gapja GIHAE_36 = new Gapja("기해", 36, 20, 13, 18);
    public static final Gapja GYEONGJA_37 = new Gapja("경자", 37, 19, 17, 17);
    public static final Gapja SINCHUK_38 = new Gapja("신축", 38, 20, 15, 18);
    public static final Gapja IMIN_39 = new Gapja("임인", 39, 16, 13, 14);
    public static final Gapja GYEMYO_40 = new Gapja("계묘", 40, 15, 11, 13);

    public static final Gapja GAPJIN_41 = new Gapja("갑진", 41, 22, 14, 20);
    public static final Gapja EULSA_42 = new Gapja("을사", 42, 17, 12, 15);
    public static final Gapja BYEONGO_43 = new Gapja("병오", 43, 16, 16, 14);
    public static final Gapja JEONGMI_44 = new Gapja("정미", 44, 19, 14, 17);
    public static final Gapja MUSIN_45 = new Gapja("무신", 45, 17, 12, 15);
    public static final Gapja GIYU_46 = new Gapja("기유", 46, 21, 15, 19);
    public static final Gapja GYEONGSUL_47 = new Gapja("경술", 47, 21, 13, 19);
    public static final Gapja SINHAE_48 = new Gapja("신해", 48, 18, 11, 16);
    public static final Gapja IMJA_49 = new Gapja("임자", 49, 17, 15, 15);
    public static final Gapja GYECHUK_50 = new Gapja("계축", 50, 18, 13, 16);

    public static final Gapja GAPIN_51 = new Gapja("갑인", 51, 19, 16, 17);
    public static final Gapja EULMYO_52 = new Gapja("을묘", 52, 18, 14, 16);
    public static final Gapja BYEONGJIN_53 = new Gapja("병진", 53, 20, 12, 18);
    public static final Gapja JEONGSA_54 = new Gapja("정사", 54, 15, 10, 13);
    public static final Gapja MUGO_55 = new Gapja("무오", 55, 14, 14, 12);
    public static final Gapja GIMI_56 = new Gapja("기미", 56, 22, 17, 20);
    public static final Gapja GYEONGSIN_57 = new Gapja("경신", 57, 20, 15, 18);
    public static final Gapja SINYU_58 = new Gapja("신유", 58, 19, 13, 17);
    public static final Gapja IMSUL_59 = new Gapja("임술", 59, 19, 11, 17);
    public static final Gapja GYEHAE_60 = new Gapja("계해", 60, 16, 9, 14);

    private final String name;        // 갑자명
    private final int order;          // 1~60 순서
    private final int yearScore;      // 년주 점수
    private final int monthScore;     // 월주 점수
    private final int dayScore;       // 일주 점수

    // HashMap으로 빠른 검색 지원
    private static final Map<String, Gapja> NAME_TO_GAPJA;
    private static final Map<Integer, Gapja> ORDER_TO_GAPJA;

    static {
        Gapja[] allGapja = values();

        NAME_TO_GAPJA = Arrays.stream(allGapja)
                .collect(Collectors.toMap(Gapja::getName, Function.identity()));

        ORDER_TO_GAPJA = Arrays.stream(allGapja)
                .collect(Collectors.toMap(Gapja::getOrder, Function.identity()));
    }

    // 모든 갑자 배열
    private static Gapja[] values() {
        return new Gapja[]{
            GAPJA_01, EULCHUK_02, BYEONGIN_03, JEONGMYO_04, MUJIN_05,
            GISA_06, GYEONGO_07, SINMI_08, IMSIN_09, GYEYU_10,
            GAPSUL_11, EULHAE_12, BYEONGJA_13, JEONGCHUK_14, MUIN_15,
            GIMYO_16, GYEONGJIN_17, SINSA_18, IMO_19, GYEMI_20,
            GAPSIN_21, EULYU_22, BYEONGSUL_23, JEONGHAE_24, MUJA_25,
            GICHUK_26, GYEONGIN_27, SINMYO_28, IMJIN_29, GYESA_30,
            GAPO_31, EULMI_32, BYEONGSIN_33, JEONGYU_34, MUSUL_35,
            GIHAE_36, GYEONGJA_37, SINCHUK_38, IMIN_39, GYEMYO_40,
            GAPJIN_41, EULSA_42, BYEONGO_43, JEONGMI_44, MUSIN_45,
            GIYU_46, GYEONGSUL_47, SINHAE_48, IMJA_49, GYECHUK_50,
            GAPIN_51, EULMYO_52, BYEONGJIN_53, JEONGSA_54, MUGO_55,
            GIMI_56, GYEONGSIN_57, SINYU_58, IMSUL_59, GYEHAE_60
        };
    }

    // 팩토리 메서드들
    public static Gapja fromName(String name) {
        Gapja gapja = NAME_TO_GAPJA.get(name);
        if (gapja == null) {
            throw new IllegalArgumentException("Unknown gapja name: " + name);
        }
        return gapja;
    }

    public static Gapja fromOrder(int order) {
        if (order < 1 || order > 60) {
            throw new IllegalArgumentException("Gapja order must be between 1 and 60");
        }
        return ORDER_TO_GAPJA.get(order);
    }

}
