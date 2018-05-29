package com.example.draw2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.draw2.R;
import com.example.draw2.model.Shape;
import com.example.draw2.view.MainDrawView;

import java.util.List;

import static com.example.draw2.adapter.ShapeAdapter.ViewHolder.DRAW_ARROW;
import static com.example.draw2.adapter.ShapeAdapter.ViewHolder.DRAW_CIRCLE;
import static com.example.draw2.adapter.ShapeAdapter.ViewHolder.DRAW_RECTANGLE;
import static com.example.draw2.adapter.ShapeAdapter.ViewHolder.DRAW_TRIANGLE;

/**
 * Created by Administrator on 2017/8/18.
 */

public class ShapeAdapter extends RecyclerView.Adapter<ShapeAdapter.ViewHolder>{
    private List<Shape> mShapeList;
    MainDrawView tuyaView;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View shapeView;
        ImageView shapeImage;
        static final int DRAW_PATH = 0;
        static final int DRAW_CIRCLE = 1;
        static final int DRAW_RECTANGLE = 2;
        static final int DRAW_ARROW = 3;
        static final int DRAW_TRIANGLE = 4;

        public ViewHolder(View view){
            //这个view通常是RecyclerView子项的最外层布局
            super(view);
            shapeView=view;
            shapeImage=(ImageView) view.findViewById(R.id.shape_image);
        }
    }
    public ShapeAdapter(List<Shape> shapeList,MainDrawView view){
        mShapeList=shapeList;
        tuyaView=view;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.shape_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.shapeView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                Shape shape=mShapeList.get(position);
                switch (shape.getShapeId()){
                    case 1:
                        //circle
                        tuyaView.drawGraphics(DRAW_CIRCLE);
                        break;
                    case 2:
                        //rectangle
                        tuyaView.drawGraphics(DRAW_RECTANGLE);
                        break;
                    case 3:
                        //arrow
                        tuyaView.drawGraphics(DRAW_ARROW);
                        break;
                    case 4:
                        tuyaView.drawGraphics(DRAW_TRIANGLE);
                    default:
                        break;
                }
            }
        });

        return holder;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        Shape shape=mShapeList.get(position);
        holder.shapeImage.setImageResource(shape.getShapeImage());
    }
    //用于返回一共有多少子项
    public int getItemCount(){
        return mShapeList.size();
    }

}
