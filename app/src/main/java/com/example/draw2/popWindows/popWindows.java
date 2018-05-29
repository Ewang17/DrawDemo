package com.example.draw2.popWindows;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.draw2.R;
import com.example.draw2.adapter.ShapeAdapter;
import com.example.draw2.adapter.TextAdapter;
import com.example.draw2.model.Shape;
import com.example.draw2.model.Text;
import com.example.draw2.view.MainDrawView;

import java.util.ArrayList;
import java.util.List;

import static com.example.draw2.R.id.text_alpha;
import static com.example.draw2.R.id.text_size;

/**
 * Created by Administrator on 2017/8/17.
 */

public class popWindows implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{

    Context mContext;
    View winContentView;
    MainDrawView tuyaView;
    int screenWidth,screenHeight;
    PopupWindow pencil_window,eraser_window,shape_window,text_window;
    private List<Shape> shapeList=new ArrayList<>();
    private List<Text> textList=new ArrayList<>();


    ImageView popSure_pencil,popCancel_pencil,plus_pencil,minus_pencil,plus2_pencil,minus2_pencil,plus3_pencil,minus3_pencil;
    SeekBar seekBar1_pencil,seekBar2_pencil,seekBar3_pencil;
    Button reset_pencil;
    TextView text_size_pencil,text_alpha_pencil,text_hardness_pencil;

    ImageView popSure_eraser,popCancel_eraser,plus_eraser,minus_eraser,plus2_eraser,minus2_eraser,plus3_eraser,minus3_eraser;
    SeekBar seekBar1_eraser,seekBar2_eraser,seekBar3_eraser;
    Button reset_eraser;
    TextView text_size_eraser,text_alpha_eraser,text_hardness_eraser;

    ImageView popSure_shape,popCancel_shape,plus_shape,minus_shape,plus2_shape,minus2_shape;
    SeekBar seekBar1_shape,seekBar2_shape;
    TextView text_size_shape,text_alpha_shape;
    RadioGroup radioGroup_shape;

    public popWindows(Context context,MainDrawView v,int screenHeight,int screenWidth){
        mContext=context;
        tuyaView=v;
        this.screenHeight=screenHeight;
        this.screenWidth=screenWidth;
    }

    public PopupWindow showPencilStyle(Context context){


        winContentView= LayoutInflater.from(context).inflate(
                R.layout.activity_pencil_style, null);

        pencil_window=new PopupWindow(winContentView, (int) (screenWidth*0.8), (int) (screenHeight*0.6),true);
        return pencil_window;

    }
    public PopupWindow showEraserStyle(Context context){


        winContentView= LayoutInflater.from(context).inflate(
                R.layout.activity_eraser_style, null);

        eraser_window=new PopupWindow(winContentView, (int) (screenWidth*0.8), (int) (screenHeight*0.6),true);
        return eraser_window;

    }
    public PopupWindow showShapeStyle(Context context){


        winContentView= LayoutInflater.from(context).inflate(
                R.layout.activity_shape_style, null);

        shape_window=new PopupWindow(winContentView, (int) (screenWidth*0.8), (int) (screenHeight*0.6),true);
        return shape_window;

    }
    public PopupWindow showTextStyle(Context context){


        winContentView= LayoutInflater.from(context).inflate(
                R.layout.activity_text_style, null);

        text_window=new PopupWindow(winContentView, (int) (screenWidth*0.8), (int) (screenHeight*0.6),true);
        return text_window;

    }

    public void showPencil(View parent){
        initPencilPopWinListener();
        pencil_window.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    public void showEraser(View parent){
        initEraserPopWinListener();
        eraser_window.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    public void showShape(View parent){
        initShape();
        RecyclerView recyclerView=(RecyclerView) winContentView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        ShapeAdapter adapter=new ShapeAdapter(shapeList,tuyaView);
        recyclerView.setAdapter(adapter);
        initShapePopWinListener();
        shape_window.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
    public void showText(View parent){
        initText();
        RecyclerView text_recyclerView=(RecyclerView) winContentView.findViewById(R.id.recyclerView_text_style);
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        text_recyclerView.setLayoutManager(layoutManager);
        TextAdapter adapter=new TextAdapter(textList,tuyaView);
        text_recyclerView.setAdapter(adapter);
        initTextPopWinListener();
        text_window.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    private void initPencilPopWinListener(){


        reset_pencil=(Button) winContentView.findViewById(R.id.reset);
        popSure_pencil=(ImageView) winContentView.findViewById(R.id.popbtn_sure);
        popCancel_pencil=(ImageView) winContentView.findViewById(R.id.popbtn_cancel);
        plus_pencil=(ImageView) winContentView.findViewById(R.id.plus);
        minus_pencil=(ImageView) winContentView.findViewById(R.id.minus);
        plus2_pencil=(ImageView) winContentView.findViewById(R.id.plus2);
        minus2_pencil=(ImageView) winContentView.findViewById(R.id.minus2);
        plus3_pencil=(ImageView) winContentView.findViewById(R.id.plus3);
        minus3_pencil=(ImageView) winContentView.findViewById(R.id.minus3);
        seekBar1_pencil=(SeekBar) winContentView.findViewById(R.id.size_seekbar);
        seekBar2_pencil=(SeekBar) winContentView.findViewById(R.id.size_seekbar2);
        seekBar3_pencil=(SeekBar) winContentView.findViewById(R.id.size_seekbar3);
        text_size_pencil=(TextView) winContentView.findViewById(text_size);
        text_alpha_pencil=(TextView) winContentView.findViewById(text_alpha);
        text_hardness_pencil=(TextView) winContentView.findViewById(R.id.text_hardness);

        seekBar1_pencil.setProgress(tuyaView.getCurrentSize()/2);
        seekBar2_pencil.setProgress(tuyaView.getCurrentAlpha()*100/255);

        text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());
        text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");

        reset_pencil.setOnClickListener(this);
        popSure_pencil.setOnClickListener(this);
        popCancel_pencil.setOnClickListener(this);
        plus_pencil.setOnClickListener(this);
        minus_pencil.setOnClickListener(this);
        plus2_pencil.setOnClickListener(this);
        minus2_pencil.setOnClickListener(this);
        plus3_pencil.setOnClickListener(this);
        minus3_pencil.setOnClickListener(this);
        seekBar1_pencil.setOnSeekBarChangeListener(this);
        seekBar2_pencil.setOnSeekBarChangeListener(this);
        seekBar3_pencil.setOnSeekBarChangeListener(this);

    }


    private void initEraserPopWinListener(){


        reset_eraser=(Button) winContentView.findViewById(R.id.reset_eraser);
        popSure_eraser=(ImageView) winContentView.findViewById(R.id.popbtn_sure_eraser);
        popCancel_eraser=(ImageView) winContentView.findViewById(R.id.popbtn_cancel_eraser);
        plus_eraser=(ImageView) winContentView.findViewById(R.id.plus_eraser);
        minus_eraser=(ImageView) winContentView.findViewById(R.id.minus_eraser);
        plus2_eraser=(ImageView) winContentView.findViewById(R.id.plus2_eraser);
        minus2_eraser=(ImageView) winContentView.findViewById(R.id.minus2_eraser);
        plus3_eraser=(ImageView) winContentView.findViewById(R.id.plus3_eraser);
        minus3_eraser=(ImageView) winContentView.findViewById(R.id.minus3_eraser);
        seekBar1_eraser=(SeekBar) winContentView.findViewById(R.id.size_seekbar_eraser);
        seekBar2_eraser=(SeekBar) winContentView.findViewById(R.id.size_seekbar2_eraser);
        seekBar3_eraser=(SeekBar) winContentView.findViewById(R.id.size_seekbar3_eraser);
        text_size_eraser=(TextView) winContentView.findViewById(R.id.text_size_eraser);
        text_alpha_eraser=(TextView) winContentView.findViewById(R.id.text_alpha_eraser);
        text_hardness_eraser=(TextView) winContentView.findViewById(R.id.text_hardness_eraser);
        seekBar2_eraser.setEnabled(false);

        seekBar1_eraser.setProgress((int)(tuyaView.getEraserCurrentSize()/2));

        text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
        text_alpha_eraser.setText("透明度："+seekBar2_eraser.getProgress()+"%");

        reset_eraser.setOnClickListener(this);
        popSure_eraser.setOnClickListener(this);
        popCancel_eraser.setOnClickListener(this);
        plus_eraser.setOnClickListener(this);
        minus_eraser.setOnClickListener(this);
        plus2_eraser.setOnClickListener(this);
        minus2_eraser.setOnClickListener(this);
        plus3_eraser.setOnClickListener(this);
        minus3_eraser.setOnClickListener(this);
        seekBar1_eraser.setOnSeekBarChangeListener(this);
        seekBar3_eraser.setOnSeekBarChangeListener(this);

    }
    private void initShapePopWinListener(){


        radioGroup_shape= (RadioGroup) winContentView.findViewById(R.id.radioGroup);
        popSure_shape=(ImageView) winContentView.findViewById(R.id.popbtn_sure_shape);
        popCancel_shape=(ImageView) winContentView.findViewById(R.id.popbtn_cancel_shape);
        plus_shape=(ImageView) winContentView.findViewById(R.id.plus_shape);
        minus_shape=(ImageView) winContentView.findViewById(R.id.minus_shape);
        plus2_shape=(ImageView) winContentView.findViewById(R.id.plus2_shape);
        minus2_shape=(ImageView) winContentView.findViewById(R.id.minus2_shape);
        seekBar1_shape=(SeekBar) winContentView.findViewById(R.id.size_seekbar_shape);
        seekBar2_shape=(SeekBar) winContentView.findViewById(R.id.size_seekbar2_shape);
        text_size_shape=(TextView) winContentView.findViewById(R.id.text_size_shape);
        text_alpha_shape=(TextView) winContentView.findViewById(R.id.text_alpha_shape);


        seekBar1_shape.setProgress(tuyaView.getCurrentSize()/2);
        seekBar2_shape.setProgress(tuyaView.getCurrentAlpha()*100/255);

        text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
        text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");

        popSure_shape.setOnClickListener(this);
        popCancel_shape.setOnClickListener(this);
        plus_shape.setOnClickListener(this);
        minus_shape.setOnClickListener(this);
        plus2_shape.setOnClickListener(this);
        minus2_shape.setOnClickListener(this);
        seekBar1_shape.setOnSeekBarChangeListener(this);
        seekBar2_shape.setOnSeekBarChangeListener(this);

        radioGroup_shape.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radiobtn=(RadioButton) winContentView.findViewById(checkedId);
                if(radiobtn.getId()==R.id.fill_shape){
                    tuyaView.isFill(true);
                    seekBar1_shape.setEnabled(false);
                }else if(radiobtn.getId()==R.id.stroke_shape){
                    tuyaView.isFill(false);
                    seekBar1_shape.setEnabled(true);
                }
            }
        });


    }
    private void initTextPopWinListener(){

    }


    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.size_seekbar:
                text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());
                break;
            case R.id.size_seekbar2:
                text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");
                break;
            case R.id.size_seekbar3:
                break;
            case R.id.size_seekbar_eraser:
                text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
                break;
            case R.id.size_seekbar2_eraser:
                break;
            case R.id.size_seekbar3_eraser:
                break;
            case R.id.size_seekbar_shape:
                text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
                break;
            case R.id.size_seekbar2_shape:
                text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()){
            case R.id.size_seekbar:
                text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());
                break;
            case R.id.size_seekbar2:
                text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");
                break;
            case R.id.size_seekbar3:
                break;
            case R.id.size_seekbar_eraser:
                text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
                break;
            case R.id.size_seekbar2_eraser:
                break;
            case R.id.size_seekbar3_eraser:
                break;
            case R.id.size_seekbar_shape:
                text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
                break;
            case R.id.size_seekbar2_shape:
                text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");
                break;
            default:
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()){
            case R.id.size_seekbar:
                text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());
                break;
            case R.id.size_seekbar2:
                text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");
                break;
            case R.id.size_seekbar3:
                break;
            case R.id.size_seekbar_eraser:
                text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
                break;
            case R.id.size_seekbar3_eraser:
                break;
            case R.id.size_seekbar_shape:
                text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
                break;
            case R.id.size_seekbar2_shape:
                text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");
                break;
            default:
                break;
        }
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.reset:
                tuyaView.selectPencilStyle(1,0,0);
                break;
            case R.id.popbtn_sure:
                tuyaView.selectPencilStyle(0,seekBar1_pencil.getProgress()*2,
                        (int) (seekBar2_pencil.getProgress()*255/100));
                pencil_window.dismiss();
                break;
            case R.id.popbtn_cancel:
                tuyaView.selectPencilStyle(1,0,0);
                pencil_window.dismiss();
                break;
            case R.id.plus:
                seekBar1_pencil.setProgress(seekBar1_pencil.getProgress()+1);
                text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());
                break;
            case R.id.minus:
                seekBar1_pencil.setProgress(seekBar1_pencil.getProgress()-1);
                text_size_pencil.setText("尺寸："+seekBar1_pencil.getProgress());

                break;
            case R.id.plus2:
                seekBar2_pencil.setProgress(seekBar2_pencil.getProgress()+1);
                text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");
                break;
            case R.id.minus2:
                seekBar2_pencil.setProgress(seekBar2_pencil.getProgress()-1);
                text_alpha_pencil.setText("透明度："+seekBar2_pencil.getProgress()+"%");
                break;
            case R.id.plus3:
                break;
            case R.id.minus3:
                break;
            case R.id.reset_eraser:
                tuyaView.selectEraserStyle(1,0);
                break;
            case R.id.popbtn_sure_eraser:
                tuyaView.selectEraserStyle(0,seekBar1_eraser.getProgress()*2);
                eraser_window.dismiss();
                break;
            case R.id.popbtn_cancel_eraser:
                tuyaView.selectEraserStyle(1,0);
                eraser_window.dismiss();
                break;
            case R.id.plus_eraser:
                seekBar1_eraser.setProgress(seekBar1_eraser.getProgress()+1);
                text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
                break;
            case R.id.minus_eraser:
                seekBar1_eraser.setProgress(seekBar1_eraser.getProgress()-1);
                text_size_eraser.setText("尺寸："+seekBar1_eraser.getProgress());
                break;
            case R.id.plus2_eraser:
                break;
            case R.id.minus2_eraser:
                break;
            case R.id.plus3_eraser:
                break;
            case R.id.minus3_eraser:
                break;
            case R.id.popbtn_sure_shape:
                tuyaView.selectPaintStyle(0,seekBar1_shape.getProgress()*2,
                        (int) (seekBar2_shape.getProgress()*255/100));
                shape_window.dismiss();
                break;
            case R.id.popbtn_cancel_shape:
                tuyaView.selectPaintStyle(1,0,0);
                shape_window.dismiss();
                break;
            case R.id.plus_shape:
                seekBar1_shape.setProgress(seekBar1_shape.getProgress()+1);
                text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
                break;
            case R.id.minus_shape:
                seekBar1_shape.setProgress(seekBar1_shape.getProgress()-1);
                text_size_shape.setText("粗细："+seekBar1_shape.getProgress());
                break;
            case R.id.plus2_shape:
                seekBar2_shape.setProgress(seekBar2_shape.getProgress()+1);
                text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");
                break;
            case R.id.minus2_shape:
                seekBar2_shape.setProgress(seekBar2_shape.getProgress()-1);
                text_alpha_shape.setText("透明度："+seekBar2_shape.getProgress()+"%");
                break;
        }


    }


    public void initShape(){
        Shape circle=new Shape(1,"circle", R.drawable.i_clipart_circle);
        Shape rectangle=new Shape(2,"rectangle",R.drawable.i_clipart_rectangle);
        Shape arrow=new Shape(3,"arrow",R.drawable.arrow_right);
        Shape triangle=new Shape(4,"triangle", R.drawable.i_clipart_triangle);
        Shape circle2=new Shape(5,"circle", R.drawable.color);
        Shape circle3=new Shape(6,"circle", R.drawable.color);
        Shape circle4=new Shape(7,"circle", R.drawable.color);
        Shape circle5=new Shape(8,"circle", R.drawable.color);

        shapeList.add(circle);
        shapeList.add(rectangle);
        shapeList.add(arrow);
        shapeList.add(triangle);
        shapeList.add(circle2);
        shapeList.add(circle3);
        shapeList.add(circle4);
        shapeList.add(circle5);
    }
    public void initText(){
        //添加字体样式
    }
}
