package com.example.draw2.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.draw2.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button drawBegin;
    private Button edit;
    private Button draw;
    View winContentView;
    PopupWindow window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawBegin=(Button) findViewById(R.id.btn_draw);
        drawBegin.setOnClickListener(this);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }


    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void initWindow(){
        // get the height of screen
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight=metrics.heightPixels;
        winContentView= LayoutInflater.from(this).inflate(
                R.layout.activity_win, null);
        window=new PopupWindow(winContentView, ViewGroup.LayoutParams.MATCH_PARENT, (int) (screenHeight*0.2),true);


        edit=(Button)winContentView.findViewById(R.id.edit_pic);
        draw=(Button)winContentView.findViewById(R.id.draw_new);

        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        window.setAnimationStyle(R.style.translate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener(){
            public void onDismiss(){
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);

            }
        });

        window.showAtLocation(findViewById(R.id.activity_main), Gravity.BOTTOM, 0, 0);

        edit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "敬请期待，嘻嘻~", Toast.LENGTH_SHORT).show();
                //open file and edit picture
            }
        });

        draw.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,DrawActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_draw:
                initWindow();
                break;
            default:
                break;
        }
    }

}
