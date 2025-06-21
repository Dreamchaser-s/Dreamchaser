package com.boardtest.dreamchaser.category;

import lombok.Getter;

@Getter
public enum Category {

    SMARTPHONE("스마트폰", "모바일"),
    SMARTWATCH("스마트워치", "모바일"),
    TABLET("태블릿 PC", "모바일"),
    EARBUDS_HEADPHONES("무선 이어폰/헤드폰", "모바일"),
    MOBILE_ACCESSORIES("모바일 액세서리", "모바일"),


    LAPTOP("노트북", "컴퓨터"),
    DESKTOP("데스크톱", "컴퓨터"),
    MONITOR("모니터", "컴퓨터"),
    PERIPHERALS("PC 주변기기", "컴퓨터"),
    COMPONENTS("PC 부품", "컴퓨터"),


    TV("TV", "가전"),
    REFRIGERATOR("냉장고/김치냉장고", "가전"),
    WASHING_MACHINE("세탁기/건조기", "가전"),
    VACUUM_CLEANER("청소기", "가전"),
    KITCHEN_APPLIANCES("주방가전", "가전"),
    SEASONAL_APPLIANCES("계절가전", "가전"),
    SMART_HOME("기타 스마트홈 기기", "가전"),


    CONSOLE("콘솔 게임기", "게이밍"),
    GAMING_GEAR("게이밍 기어", "게이밍"),
    GAMING_PERIPHERALS("게이밍 주변기기", "게이밍"),


    CAMERA("카메라", "카메라/음향"),
    LENS_ACCESSORIES("렌즈 및 액세서리", "카메라/음향"),
    SPEAKER("스피커", "카메라/음향"),
    MICROPHONE_RECORDING("마이크/녹음 장비", "카메라/음향");

    private final String displayName;
    private final String mainCategory;

    Category(String displayName, String mainCategory) {
        this.displayName = displayName;
        this.mainCategory = mainCategory;
    }
}