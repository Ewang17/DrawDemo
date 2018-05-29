package com.example.draw2.model;

/**
 * Created by Administrator on 2017/8/18.
 */

public class Shape {
    private int shapeId;
    private String name;
    private int shapeImage;
    public Shape(int shapeId,String name,int shapeImage){
        this.name=name;
        this.shapeId=shapeId;
        this.shapeImage=shapeImage;
    }
    public String getName(){
        return name;
    }
    public int getShapeId(){
        return shapeId;
    }
    public int getShapeImage(){
        return shapeImage;
    }
}
