package org.fitzeng.zzchat.aty;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.server.ServerManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AtyLogin extends AppCompatActivity implements View.OnClickListener{
    private TextView password_forget;
    private EditText login_user;
    private EditText login_password;
    private Button button_login;
    private Button button_regist;
    private String user_name,psw,spPsw;
    private ServerManager serverManager = ServerManager.getServerManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_aty_login);

        password_forget = (TextView) findViewById(R.id.password_forget);
        password_forget.setOnClickListener(this);
        password_forget.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        login_user = (EditText) findViewById(R.id.login_user);

        login_password = (EditText) findViewById(R.id.login_password);

        button_login = (Button) findViewById(R.id.button_login);
        button_login.setOnClickListener(this);
        button_login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        button_regist = (Button) findViewById(R.id.button_regist);
        button_regist.setOnClickListener(this);
        button_regist.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        serverManager.startThread();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_regist:
                login_user.setText("");
                login_password.setText("");
                Intent intent_regist = new Intent(AtyLogin.this, AtyRegist.class);
                startActivityForResult(intent_regist,1);
                break;
            /*case R.id.password_forget:
                login_user.setText("");
                login_password.setText("");
                Intent intent_forget = new Intent(AtyLogin.this, AtypasswordFind.class);
                startActivity(intent_forget);
                break;*/
            case R.id.button_login:
                user_name=login_user.getText().toString();
                psw=login_password.getText().toString();

                //String md5Psw = MD5Utils.md5(psw);

                //spPsw=readPsw(user_name);
                if(TextUtils.isEmpty(user_name)){
                    Toast.makeText(AtyLogin.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    login_password.setText("");
                    return;
                }
                else if(TextUtils.isEmpty(psw)){
                    Toast.makeText(AtyLogin.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(login(user_name,psw)){
                    Toast.makeText(AtyLogin.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    //saveLoginStatus(true, user_name);
                   /* Intent data=new Intent();
                    data.putExtra("isLogin",true);
                    setResult(RESULT_OK,data);*/
                    AtyLogin.this.finish();
                    startActivity(new Intent(AtyLogin.this, AtyMainInterface.class));
                    return;
                }
                else{
                    Toast.makeText(AtyLogin.this, "输入的用户名和密码不一致", Toast.LENGTH_SHORT).show();
                    login_password.setText("");
                    return;
                }
                /*else{
                    Toast.makeText(AtyLogin.this, "此用户名不存在", Toast.LENGTH_SHORT).show();
                }*/
        }
    }

    /*private String readPsw(String userName){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        return sp.getString(userName , "");
    }

    private void saveLoginStatus(boolean status,String userName){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("isLogin", status);
        editor.putString("loginUserName", userName);
        editor.commit();
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            String userName=data.getStringExtra("user_name");
            login_user.setText(userName);
            login_user.setSelection(userName.length());
        }
    }

    private boolean login(String username,String password){
        String msg = "[LOGIN]:[" + username + ", " + password + "]";
        serverManager.sendMessage(this,msg);
        String ack = serverManager.getMessage();
        if(ack == null){
            return false;
        }
        serverManager.setMessage(null);
        String p = "\\[ACKLOGIN\\]:\\[(.*)\\]";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(ack);
        return matcher.find() && matcher.group(1).equals("1");
    }
}
