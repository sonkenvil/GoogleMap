package com.something.kteam.googlemap.model;

/**
 * Created by Nguyen Hung Son on 4/18/2017.
 */

public class Duration {
    private String text;
    private String value;

    public Duration(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public Duration() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
