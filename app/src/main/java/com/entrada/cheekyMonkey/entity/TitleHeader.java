package com.entrada.cheekyMonkey.entity;

public class TitleHeader {

    private boolean position;
    private String titleHeader;

    public TitleHeader() {
    }

    public TitleHeader(boolean position, String titleHeader) {
        this.position = position;
        this.titleHeader = titleHeader;

    }

    public boolean isPosition() {
        return position;
    }

    public void setPosition(boolean position) {
        this.position = position;
    }

    public String getTitleHeader() {
        return titleHeader;
    }

    public void setTitleHeader(String titleHeader) {
        this.titleHeader = titleHeader;
    }

}
