package com.example.draw2.activities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.draw2.R;
import com.example.draw2.popWindows.popWindows;
import com.example.draw2.view.MainDrawView;
import com.example.draw2.view.StickerView;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView picture,back,undo,redo,recover,next,pencil_color,pencil,eraser;
    ImageButton bg,text,shape,add;
    public static FrameLayout frameLayout;
    MainDrawView tuyaView;
    int screenWidth,screenHeight;
    com.example.draw2.popWindows.popWindows popWindows;
    public static final int CHOOSE_PHOTO = 2;
    //当前处于编辑状态的贴纸
    private StickerView mCurrentView;
    //存储贴纸列表
    private ArrayList<View> mViews;

    private RelativeLayout mContentRootView;

    public void setFrameLayout(FrameLayout frameLayout) {
        this.frameLayout = frameLayout;
    }

    public FrameLayout getFrameLayout() {
        return frameLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        //initToolbar();
        initView();
        initData();
        initListener();



    }
    private void initView(){
        frameLayout = (FrameLayout) findViewById(R.id.draw_frame);
        pencil_color=(ImageView) findViewById(R.id.color_change);
        pencil=(ImageView) findViewById(R.id.pencil);
        eraser=(ImageView) findViewById(R.id.eraser);
        bg=(ImageButton) findViewById(R.id.bg_color);
        text=(ImageButton) findViewById(R.id.text);
        shape=(ImageButton) findViewById(R.id.shape);
        add=(ImageButton) findViewById(R.id.add);
        picture=(ImageView) findViewById(R.id.picture);

        mContentRootView=(RelativeLayout) findViewById(R.id.rl_content_root);
        back=(ImageView) findViewById(R.id.back);
        undo=(ImageView) findViewById(R.id.undo);
        redo=(ImageView) findViewById(R.id.redo);
        recover=(ImageView) findViewById(R.id.recover);
        next=(ImageView) findViewById(R.id.next);

        pencil.setColorFilter(R.color.white);
    }
    private void initData() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = defaultDisplay.getWidth();
        screenHeight = defaultDisplay.getHeight();
     //   realHeight = (int) (screenHeight - getResources().getDimension(R.dimen.DIMEN_100PX) - getResources().getDimension(R.dimen.DIMEN_100PX));
        tuyaView = new MainDrawView(this,screenWidth,screenHeight);
        frameLayout.addView(tuyaView);
        //frameLayout.addView(mContentRootView);
        mViews = new ArrayList<>();
        mViews.add(tuyaView);
        tuyaView.requestFocus();
    }
    private void initListener(){
        pencil_color.setOnClickListener(this);
        pencil.setOnClickListener(this);
        eraser.setOnClickListener(this);
        bg.setOnClickListener(this);
        text.setOnClickListener(this);
        shape.setOnClickListener(this);
        add.setOnClickListener(this);

        back.setOnClickListener(this);
        undo.setOnClickListener(this);
        redo.setOnClickListener(this);
        undo.setOnClickListener(this);
        recover.setOnClickListener(this);
        next.setOnClickListener(this);

    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.undo:
                tuyaView.undo();
                break;
            case R.id.redo:
                tuyaView.redo();
                break;
            case R.id.recover:
                tuyaView.recover();
                break;
            case R.id.next:
                tuyaView.saveToSDCard();
                break;
            case R.id.color_change:
                showPaintColorDialog(v);
                break;
            case R.id.bg_color:
                showBGColorDialog(v);
                break;
            case R.id.pencil:
                //切换到画笔和橡皮擦时画线条
                tuyaView.drawGraphics(0);
                pencil.setColorFilter(R.color.white);
                eraser.setColorFilter(null);
                if(tuyaView.isPencil()){
                    popWin();
                    window=popWindows.showPencilStyle(this);
                    initWin();
                    popWindows.showPencil(v);
                }else{
                    tuyaView.selectPencilStyle(1,0,0);
                    tuyaView.setPencil(true);
                }

                break;
            case R.id.eraser:
                //切换到画笔和橡皮擦时画线条
                tuyaView.drawGraphics(0);
                pencil.setColorFilter(null);
                eraser.setColorFilter(R.color.white);
                if(!tuyaView.isPencil()){
                    popWin();
                    window=popWindows.showEraserStyle(this);
                    initWin();
                    popWindows.showEraser(v);
                }else{
                    tuyaView.selectEraserStyle(1,0);
                    tuyaView.setPencil(false);
                }
                break;
            case R.id.shape:
                popWin();
                window=popWindows.showShapeStyle(this);
                initWin();
                popWindows.showShape(v);
                break;
            case R.id.add:
                //图片选择器
                Intent picIntent = new Intent("android.intent.action.GET_CONTENT");
                picIntent.setType("image/*");
               // picIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(picIntent, CHOOSE_PHOTO);
                break;
            case R.id.text:
                break;
            default:
                break;
        }
    }

    //图片选择后的回传
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.e("TAG","回传方法");
        switch (requestCode){
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        //4.4及以上系统使用这个方法处理图片
                        Log.e("TAG","4.4及以上系统使用这个方法处理图片");
                        handleImageOnKitKat(data);
                    }else{
                        //4.4以下系统使用这个方法处理图片
                        Log.e("TAG","4.4以下系统使用这个方法处理图片");
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                Log.e("TAG","default");
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.
                        EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //content类型的uri,使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath=uri.getPath();
        }
        displayImage(imagePath);
    }
    private void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){
        if(imagePath!=null){
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            tuyaView.setContentBitmap(bitmap);
            addStickerView(bitmap);

        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private int select_paint_style_paint = 0;
    private int select_paint_style_eraser = 1;


   // View winContentView;
    PopupWindow window;
    public void popWin(){
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight=metrics.heightPixels;
        screenWidth=metrics.widthPixels;
        popWindows=new popWindows(this,tuyaView,screenHeight,screenWidth);
    }
    public void initWin(){
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        //window.setAnimationStyle(R.style.translate);
        window.setOnDismissListener(new PopupWindow.OnDismissListener(){
            public void onDismiss(){
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);

            }
        });
    }

    //添加表情
    private void addStickerView(Bitmap bitmap) {
        final StickerView stickerView = new StickerView(this);
        stickerView.setBitmap(bitmap);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                tuyaView.setmCurrentView(null);
                mViews.remove(stickerView);
                frameLayout.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {

                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        frameLayout.addView(stickerView,lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
        tuyaView.setmCurrentView(stickerView);
    }
    /**
     * 设置当前处于编辑模式的贴纸
     */
    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }
        mCurrentView = stickerView;
        stickerView.setInEdit(true);
    }

    private int select_paint_color_index = 0;
    public void showPaintColorDialog(View parent){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔颜色：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_paint_color_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_color_index = which;
                tuyaView.selectPaintColor(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    private int select_BG_color_index = 5;
    public void showBGColorDialog(View parent){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画布颜色：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_BG_color_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_BG_color_index = which;
                int BG_color=tuyaView.getColor(which);
                frameLayout.setBackgroundColor(BG_color);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }



}
