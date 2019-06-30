package org.fitzeng.zzchat.aty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.fitzeng.zzchat.Data;
import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.adapter.MyAdapter;
import org.fitzeng.zzchat.util.Utils;
import org.fitzeng.zzchat.view.FloatDragView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ChooseDiary extends AppCompatActivity implements View.OnClickListener{

    private ListView list_one;
    private TextView txt_empty;
    private FloatingActionButton btn_add;
    private MyAdapter mAdapter = null;
    private List<Data> mData = null;
    private Context mContext = null;
    private int flag = 1;
    // private Data mData_5 = null;   //用来临时放对象的
    public static boolean hassent=false;
    int EXIST_DIARY=1;
    int NEW_DIARY=0;
    @Override
    @RequiresApi(24)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_diary);
        mContext = ChooseDiary.this;
        mData = new LinkedList<Data>();
        initializeData();
        mAdapter = new MyAdapter((LinkedList<Data>) mData,mContext);
        FloatDragView.addFloatDragView(ChooseDiary.this, (RelativeLayout) findViewById(R.id.relativelay), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击事件
            }
        });

        bindViews();
    }
    @RequiresApi(24)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片
        //
        if(data.getStringExtra("result").equals("done")) {
            mAdapter.update(data.getStringExtra("tempfilename"), 0);
            mAdapter.update(data.getStringExtra("filename"), 1);
            System.out.println(data.getStringExtra("filename"));
        }
    }
    private void bindViews(){
        list_one = (ListView) findViewById(R.id.list_one);
        btn_add =  findViewById(R.id.add);
        list_one.setAdapter(mAdapter);
        list_one.setEmptyView(txt_empty);
        list_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int position, long id) {
                String filename=mData.get(position).getContent()+".html";
                Intent intent=new Intent(ChooseDiary.this, MyEditor.class);
                intent.putExtra("filename",filename);
                hassent=true;
                startActivityForResult(intent,EXIST_DIARY);
                overridePendingTransition(1,1);
            }
        });
        list_one.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder=new AlertDialog.Builder(ChooseDiary.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    @RequiresApi(24)
                    public void onClick(DialogInterface dialog, int which) {
                        String filename=mData.get(position).getContent()+".html";
                        String path=ChooseDiary.this.getDataDir().getAbsolutePath(); //应用路径
                        if(mData.remove(position)!=null){
                            System.out.println("success");
                        }else {
                            System.out.println("failed");
                        }
                        Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.notifyDataSetChanged();
                        File file =new File(path,filename);
                        file.delete();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "取消删除", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        btn_add.setOnClickListener(this);


    }

    private void updateListItem(int postion,Data mData){
        int visiblePosition = list_one.getFirstVisiblePosition();
        View v = list_one.getChildAt(postion - visiblePosition);
        ImageView img =  v.findViewById(R.id.img_icon);
        TextView tv =  v.findViewById(R.id.txt_content);
        img.setImageResource(mData.getImgId());
        tv.setText(mData.getContent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
            {
                hassent=false;
                Intent intent=new Intent(ChooseDiary.this,MyEditor.class);
                startActivityForResult(intent,NEW_DIARY);
            }
            break;
        }
    }
    @RequiresApi(24)
    public void initializeData(){
        String path=ChooseDiary.this.getDataDir().getAbsolutePath(); //应用路径
        ArrayList<String> arr= Utils.getFileName(path,".html");
        for(String str:arr) {
            System.out.println("ChooseDiary" + str);
            if(!str.equals("editor.html")){
                Data data=new Data(R.drawable.add,str.substring(0,str.length()-5));
                mData.add(data);
                Collections.sort(mData,new Data.DataComparator());
            }
        }


    }
}