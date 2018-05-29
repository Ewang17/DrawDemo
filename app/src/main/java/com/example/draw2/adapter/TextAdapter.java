package com.example.draw2.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.draw2.R;
import com.example.draw2.model.Text;
import com.example.draw2.view.MainDrawView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/18.
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder>{
    private List<Text> mTextList;
    MainDrawView tuyaView;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View textView;
        ImageView textImage;

        public ViewHolder(View view){
            //这个view通常是RecyclerView子项的最外层布局
            super(view);
            textView=view;
            textImage=(ImageView) view.findViewById(R.id.text_image);
        }
    }
    public TextAdapter(List<Text> textList, MainDrawView view){
        mTextList=textList;
        tuyaView=view;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.text_style_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.textView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                Text text=mTextList.get(position);
                switch (text.gettextId()){
                    case 1:
                        //circle
                        break;
                    case 2:
                        //rectangle
                        break;
                    case 3:
                        //arrow
                        break;
                    case 4:
                    default:
                        break;
                }
            }
        });

        return holder;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        Text text=mTextList.get(position);
        holder.textImage.setImageResource(text.gettextImage());
    }
    //用于返回一共有多少子项
    public int getItemCount(){
        return mTextList.size();
    }

}
