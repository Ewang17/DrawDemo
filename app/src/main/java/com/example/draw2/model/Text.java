package com.example.draw2.model;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Text {
    private int textId;
    private String name;
    private int textImage;
    public Text(int textId, String name, int textImage){
        this.name=name;
        this.textId=textId;
        this.textImage=textImage;
    }
    public String getName(){
        return name;
    }
    public int gettextId(){
        return textId;
    }
    public int gettextImage(){
        return textImage;
    }
}
