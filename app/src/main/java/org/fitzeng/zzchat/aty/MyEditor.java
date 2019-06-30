package org.fitzeng.zzchat.aty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.util.PathUtils;
import org.fitzeng.zzchat.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
public class MyEditor extends AppCompatActivity {
    private RichEditor mEditor;
    private TextView mPreview;                  //暂时停用html预览
    private Uri imageUri;
    private View mTouchOutsideView;
    private OnTouchOutsideViewListener mOnTouchOutsideViewListener;  //点击组件外部时组件消失
    private int redoTimes=0;    //可撤销次数
    private int undoTimes=0;     //可重做次数
    private int noCheck=0;   //设置不检测TextChange
    private int[] clicktime=new int[20]; //各个按钮的点击次数
    private String savePath=new String();
    private String filename=new String();
    int isBgOrText=0;
    private Intent intent=new Intent();       //回传给choosediary的
    public  String temp=new String();          //choosediary传入
    @Override
    @RequiresApi(19)
    public boolean onKeyDown(int keyCode, KeyEvent event)  {          //监听物理按键back
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something on back.
            showAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        // 获取图片
        try {
            //该uri是上一个Activity返回的
            imageUri = data.getData();
            String imagePath= PathUtils.getRealPathFromURI_API19(MyEditor.this,imageUri);
            mEditor.insertImage(imagePath,"image");
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @RequiresApi(24)
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for(int a:clicktime)       //初始化点击次数数组
            a=0;

        Intent chooseintent=getIntent();
        temp=chooseintent.getStringExtra("filename");
        savePath=MyEditor.this.getDataDir().getAbsolutePath();
        filename= Utils.getTime()+".html";                  //存储路径
        setContentView(R.layout.activity_myeditor);
        mEditor = findViewById(R.id.editor);
        copyRunnable(MyEditor.this,"",savePath);         //将js文件复制 否则加载
        mEditor.getSettings().setBlockNetworkImage(false);
        mEditor.getSettings().setJavaScriptEnabled(true);
        hasIntentIn();
        mEditor.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        mEditor.setEditorHeight(10000);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //  mEditor.TType();
        //mEditor.loadCSS("msyh.css");
        mEditor.setWebChromeClient(new WebChromeClient());



        mEditor.setPadding(10, 10, 10, 10);
        //
        mEditor.setPlaceholder("记录你的今天~");
        mPreview = findViewById(R.id.preview);
        mPreview.setVisibility(View.GONE);
        String[] buttonname={"undo","redo","bold","italic","subscript","superscript","strikethrough","underline","indent",
                "outdent","align_left","align_center","align_right","insert_bullets","insert_numbers"};
        final ImageButton undo=(ImageButton)findViewById(R.id.action_undo);
        final ImageButton redo=(ImageButton)findViewById(R.id.action_redo);
        final ImageButton bold=findViewById(R.id.action_bold);
        final ImageButton italic=findViewById(R.id.action_italic);
        final ImageButton subscript=findViewById(R.id.action_subscript);
        final ImageButton superscript=findViewById(R.id.action_superscript);
        final ImageButton strikethrough=findViewById(R.id.action_strikethrough);
        final ImageButton underline=findViewById(R.id.action_underline);
        final ImageButton indent=findViewById(R.id.action_indent);
        final ImageButton outdent=findViewById(R.id.action_outdent);
        final ImageButton align_left=findViewById(R.id.action_align_left);
        final ImageButton align_center=findViewById(R.id.action_align_center);
        final ImageButton align_right=findViewById(R.id.action_align_right);
        final ImageButton insert_bullets=findViewById(R.id.action_insert_bullets);
        final ImageButton insert_numbers=findViewById(R.id.action_insert_numbers);

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override

            public void onTextChange(String text) {
                mPreview.setText(text);
                if(noCheck==0) {
                    undoTimes++;
                    redoTimes = 0;
                    undo.setImageResource(R.drawable.undo1);
                    redo.setImageResource(R.drawable.redo);
                }else
                    noCheck=0;
            }
        });

        undo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.undo();

                if(undoTimes!=0) {
                    noCheck=1;
                    undoTimes--;
                    redoTimes++;
                    redo.setImageResource(R.drawable.redo1);
                    if(undoTimes==0) {
                        undo.setImageResource(R.drawable.undo);
                    }
                    if(redoTimes==0){
                        redo.setImageResource(R.drawable.redo);
                    }
                }
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.redo();

                if(redoTimes!=0){
                    noCheck=1;
                    undo.setImageResource(R.drawable.undo1);
                    undoTimes++;
                    redoTimes--;
                    if(undoTimes==0) {
                        undo.setImageResource(R.drawable.undo);
                    }
                    if(redoTimes==0){
                        redo.setImageResource(R.drawable.redo);
                    }
                }
            }
        });

        bold.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBold();
                if(clicktime[0]==0){
                    clicktime[0]++;
                    bold.setImageResource(R.drawable.bold1);
                }else{
                    clicktime[0]--;
                    bold.setImageResource(R.drawable.bold);
                }
            }
        });

        italic.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setItalic();
                if(clicktime[1]==0){
                    clicktime[1]++;
                    italic.setImageResource(R.drawable.italic1);
                }else{
                    clicktime[1]--;
                    italic.setImageResource(R.drawable.italic);
                }
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSubscript();
                if(clicktime[2]==0){
                    clicktime[2]++;
                    subscript.setImageResource(R.drawable.subscript1);
                }else{
                    clicktime[2]--;
                    subscript.setImageResource(R.drawable.subscript);
                }
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setSuperscript();
                if(clicktime[3]==0){
                    clicktime[3]++;
                    superscript.setImageResource(R.drawable.superscript1);
                }else{
                    clicktime[3]--;
                    superscript.setImageResource(R.drawable.superscript);
                }
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setStrikeThrough();
                if(clicktime[4]==0){
                    clicktime[4]++;
                    strikethrough.setImageResource(R.drawable.strikethrough1);
                }else{
                    clicktime[4]--;
                    strikethrough.setImageResource(R.drawable.strikethrough);
                }
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setUnderline();
                if(clicktime[5]==0){
                    clicktime[5]++;
                    underline.setImageResource(R.drawable.underline1);
                }else{
                    clicktime[5]--;
                    underline.setImageResource(R.drawable.underline);
                }
            }
        });

        Spinner spin1=findViewById(R.id.spinner1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String select=parent.getItemAtPosition(position).toString();
                switch (select){
                    case "微软雅黑":
                        mEditor.setTextType("msyh",1);
                        break;
                    case "华文细黑":
                        mEditor.setTextType("hwxh",1);
                        break;
                    case "Serif":
                        mEditor.setTextType("serif",1);
                        break;
                    case "Sans":
                        mEditor.setTextType("sans",1);
                        break;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Spinner spin2=findViewById(R.id.spinner2);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mEditor.setFontSize(Integer.parseInt(parent.getItemAtPosition(position).toString()));//设置字体
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        findViewById(R.id.action_black).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(Color.BLACK);
                else
                    mEditor.setTextBackgroundColor(Color.BLACK);
            }
        });
        findViewById(R.id.action_white).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(Color.WHITE);
                else
                    mEditor.setTextBackgroundColor(Color.WHITE);
            }
        });
        findViewById(R.id.action_red).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(Color.RED);
                else
                    mEditor.setTextBackgroundColor(Color.RED);
            }
        });
        findViewById(R.id.action_orange).setOnClickListener(new View.OnClickListener(){
            int color=getResources().getColor(R.color.color_orange);
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(color);
                else
                    mEditor.setTextBackgroundColor(color);
            }
        });
        findViewById(R.id.action_yellow).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(Color.YELLOW);
                else
                    mEditor.setTextBackgroundColor(Color.YELLOW);
            }
        });
        findViewById(R.id.action_green).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(Color.GREEN);
                else
                    mEditor.setTextBackgroundColor(Color.GREEN);
            }
        });
        findViewById(R.id.action_lightgreen).setOnClickListener(new View.OnClickListener(){

            int color=getResources().getColor(R.color.color_lightgreen);
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(color);
                else
                    mEditor.setTextBackgroundColor(color);
            }
        });
        findViewById(R.id.action_purple).setOnClickListener(new View.OnClickListener(){
            int color=getResources().getColor(R.color.color_purple);
            @Override
            public void onClick(View v){
                if(isBgOrText==0)
                    mEditor.setTextColor(color);
                else
                    mEditor.setTextBackgroundColor(color);
            }
        });
        setOnTouchOutsideViewListener(findViewById(R.id.colorplate), new OnTouchOutsideViewListener() {
            @Override
            public void onTouchOutside(View view, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                    view.setVisibility(View.GONE);
            }
        });


        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                findViewById(R.id.colorplate).setVisibility(View.VISIBLE);
                isBgOrText=0;

            }

        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                findViewById(R.id.colorplate).setVisibility(View.VISIBLE);
                isBgOrText=1;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setIndent();
                if(clicktime[6]==0){
                    clicktime[6]++;
                    indent.setImageResource(R.drawable.indent1);
                }else{
                    clicktime[6]--;
                    indent.setImageResource(R.drawable.indent);
                }
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setOutdent();
                if(clicktime[7]==0){
                    clicktime[7]++;
                    outdent.setImageResource(R.drawable.outdent1);
                }else{
                    clicktime[7]--;
                    outdent.setImageResource(R.drawable.outdent);
                }
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignLeft();
                if(clicktime[8]==0){
                    clicktime[8]++;
                    align_left.setImageResource(R.drawable.justify_left1);
                }else{
                    clicktime[8]--;
                    align_left.setImageResource(R.drawable.justify_left);
                }
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignCenter();
                if(clicktime[9]==0){
                    clicktime[9]++;
                    align_center.setImageResource(R.drawable.justify_center1);
                }else{
                    clicktime[9]--;
                    align_center.setImageResource(R.drawable.justify_center);
                }
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setAlignRight();
                if(clicktime[10]==0){
                    clicktime[10]++;
                    align_right.setImageResource(R.drawable.justify_right1);
                }else{
                    clicktime[10]--;
                    align_right.setImageResource(R.drawable.justify_right);
                }
            }
        });

   /*     findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });*/

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setBullets();
                if(clicktime[11]==0){
                    clicktime[11]++;
                    insert_bullets.setImageResource(R.drawable.bullets1);
                }else{
                    clicktime[11]--;
                    insert_bullets.setImageResource(R.drawable.bullets);
                }
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                mEditor.setNumbers();
                if(clicktime[12]==0){
                    clicktime[12]++;
                    insert_numbers.setImageResource(R.drawable.numbers1);
                }else{
                    clicktime[12]--;
                    insert_numbers.setImageResource(R.drawable.numbers);
                }
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v) {
               /* mEditor.insertImage("file:///storage/emulated/0/Download/timg.jpeg",
                        "dachshund");*/
                final int REQUEST_CODE_SELECT_PHOTO=1;
                Intent intent = new Intent();
                intent.setType("image/*");// 开启Pictures画面Type设定为image
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);



                // Uri uri=onActivityResult(1,1,intent);
                //  mEditor.insertImage(uri,"image");
                //  onActivityResult(0,0,intent);
                //Uri uri=intent.getData();

                //  mEditor.insertImage("file:///storage/emulated/0/Downloads/timg.jpeg","hello");
            }
        });

        findViewById(R.id.action_save).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
                //saveHTML();
                showAlertDialog();
            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println(imageUri);
                String jjj=PathUtils.getRealPathFromURI_API19(MyEditor.this,imageUri);
                mEditor.insertImage(jjj,"image");
                System.out.println("jjj:"+jjj);
            }
        });

    }
    public void setOnTouchOutsideViewListener(View view, OnTouchOutsideViewListener onTouchOutsideViewListener) {
        mTouchOutsideView = view;
        mOnTouchOutsideViewListener = onTouchOutsideViewListener;
    }

    public OnTouchOutsideViewListener getOnTouchOutsideViewListener() {
        return mOnTouchOutsideViewListener;
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            // Notify touch outside listener if user tapped outside a given view
            if (mOnTouchOutsideViewListener != null && mTouchOutsideView != null
                    && mTouchOutsideView.getVisibility() == View.VISIBLE) {
                Rect viewRect = new Rect();
                mTouchOutsideView.getGlobalVisibleRect(viewRect);
                if (!viewRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    mOnTouchOutsideViewListener.onTouchOutside(mTouchOutsideView, ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Interface definition for a callback to be invoked when a touch event has occurred outside a formerly specified
     * view. See {@link #setOnTouchOutsideViewListener(View, OnTouchOutsideViewListener).}
     */
    public interface OnTouchOutsideViewListener {

        /**
         * Called when a touch event has occurred outside a given view.
         *
         * @param view  The view that has not been touched.
         * @param event The MotionEvent object containing full information about the event.
         */
        void onTouchOutside(View view, MotionEvent event);
    }

    @RequiresApi(19)
    public void evaluate_Javascript(){
        mEditor.evaluateJavascript(
                "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();",
                new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String rawhtml) {
                        String html1=rawhtml.replace("\\u003C","<");//传入的部分字符以Unicode格式，需要转码
                        String html2=html1.replace("\\n","\n");
                        String html3=html2.replace("\\","");
                        String html=html3.substring(1,html3.length()-1);
                        Log.i("1281149304",html);
                        // code here
                        try {
                            byte[] bytes = html.getBytes("UTF-8");
                            Utils.saveFile(bytes,savePath, filename);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }
    @RequiresApi(19)
    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyEditor.this);
        builder.setTitle("给今天的日记取一个标题吧");    //设置对话框标题
        builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
        final EditText edit = new EditText(MyEditor.this);
        edit.setHint("直接确认将会以默认名字保存");
        edit.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(edit);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyEditor.this, "已保存", Toast.LENGTH_SHORT).show();
                Utils.deleteFile(savePath,filename);
                intent.putExtra("tempfilename",filename.substring(0,filename.length()-5));
                if(edit.getText().length()!=0) {
                    filename = edit.getText() + ".html";
                }
                evaluate_Javascript();
                returnIntent("done");
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MyEditor.this, "取消保存", Toast.LENGTH_SHORT).show();
                returnIntent("cancel");
            }
        });
        builder.setNeutralButton("继续写", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MyEditor.this,"继续写吧",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();  //创建对话框
        dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.show();
    }
    public void hasIntentIn(){
        System.out.println(filename);
        if(ChooseDiary.hassent&&temp!=null){
            filename=temp;
            mEditor.loadUrl("file://"+savePath+"/"+filename);
        }
    }
    public void returnIntent(String result){
        intent.putExtra("filename",filename.substring(0,filename.length()-5));
        intent.putExtra("result",result);
        setResult(1,intent);
        ChooseDiary.hassent=false;
        temp=null;
        MyEditor.this.finish();
    }
    public void copyRunnable(final Context context,final String str,final String path){
        Runnable rn=new Runnable() {
            @Override
            public void run() {
                copyAssets(context,str,path);

            }
        };
        rn.run();
    }
    public void copyAssets(Context context, String assetDir, String dir) {

        String[] files;
        try {
            files = context.getResources().getAssets().list(assetDir);
        } catch (IOException e1) {
            return;
        }
        File mWorkingPath = new File(dir);
        // if this directory does not exists, make one.
        if (!mWorkingPath.exists()) {
            if (!mWorkingPath.mkdirs()) {
            }
        }
        for (int i = 0; i < files.length; i++) {
            try {
                String fileName = files[i];
                // we make sure file name not contains '.' to be a folder.
                if (!fileName.contains(".")) {
                    if (0 == assetDir.length()) {
                        copyAssets(context, fileName, dir + fileName + "/");
                    } else {
                        copyAssets(context, assetDir + "/" + fileName, dir+ fileName + "/");
                    }
                    continue;
                }
                File outFile = new File(mWorkingPath, fileName);
                if (outFile.exists())
                    outFile.delete();
                InputStream in = null;
                if (0 != assetDir.length())
                    in = context.getAssets().open(assetDir + "/" + fileName);
                else
                    in = context.getAssets().open(fileName);
                OutputStream out = new FileOutputStream(outFile);

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



