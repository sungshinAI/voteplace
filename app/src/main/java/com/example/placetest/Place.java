package com.example.placetest;

public class Place {
    private String name;

    public Place() {
        // 기본 생성자 필요 (Firebase에서 객체로 변환하기 위함)
    }

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}