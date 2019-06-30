package org.fitzeng.zzchat.aty;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.server.ServerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class AtyRegist extends AppCompatActivity implements View.OnClickListener{
    private EditText regist_user;
    private EditText regist_password;
    private EditText regist_passwordConfirm;
    private Button regist_finish;
    private Button regist_back;
    private String userName,psw,pswCfm,name;
    private ServerManager serverManager = ServerManager.getServerManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aty_regist);

        regist_finish = (Button) findViewById(R.id.regist_finish);
        regist_finish.setOnClickListener(this);
        regist_finish.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        regist_back = (Button) findViewById(R.id.regist_back);
        regist_back.setOnClickListener(this);
        regist_back.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        regist_user = (EditText) findViewById(R.id.regist_user);
        regist_password = (EditText) findViewById(R.id.regist_password);
        regist_passwordConfirm = (EditText) findViewById(R.id.regist_passwordConfirm);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.regist_finish:
                getEditString();

                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(AtyRegist.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(AtyRegist.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(TextUtils.isEmpty(pswCfm)){
                    Toast.makeText(AtyRegist.this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!psw.equals(pswCfm)){
                    Toast.makeText(AtyRegist.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(!regist(userName,psw)) {
                    Toast.makeText(AtyRegist.this, "此账户名已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    Toast.makeText(AtyRegist.this, "注册成功", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("user_name", userName);
                    setResult(RESULT_OK, data);
                    AtyRegist.this.finish();
                }
                break;
            case R.id.regist_back:
                AtyRegist.this.finish();
        }
    }

    private void getEditString(){
        userName=regist_user.getText().toString();
        psw=regist_password.getText().toString();
        pswCfm=regist_passwordConfirm.getText().toString();
    }

   private boolean regist(String username, String password){
        String msg = "[REGIST]:[" + username + ", " + password + "]";
        serverManager.sendMessage(this,msg);
        String ack = serverManager.getMessage();
        if(ack == null){
            return false;
        }
        serverManager.setMessage(null);
        String p = "\\[ACKREGIST\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        return matcher.find() && matcher.group(1).equals("1");
    }
}
