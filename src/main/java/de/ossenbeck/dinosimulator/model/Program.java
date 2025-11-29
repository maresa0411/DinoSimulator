package de.ossenbeck.dinosimulator.model;

public class Program {
    private String title;
    private String code;

    public Program(String title, String code){
        this.title = title;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public String getCode() {
        return code;
    }
}
