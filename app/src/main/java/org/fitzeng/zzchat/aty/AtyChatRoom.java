package org.fitzeng.zzchat.aty;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.fitzeng.zzchat.R;
import org.fitzeng.zzchat.adapter.AdapterChatMsg;
import org.fitzeng.zzchat.util.ChatMsg;
import org.fitzeng.zzchat.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class AtyChatRoom extends AppCompatActivity{

    private TitleBar titleBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    private List<ChatMsg> chatMsgList;
    private AdapterChatMsg adapterChatMsgList;
    private String chatObj;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_chat_room);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        AndroidBug5497Workaround.assistActivity(this);
        initViews();
    }

    private void initViews() {

        titleBar = (TitleBar) findViewById(R.id.tb_chat_room);
        listView = (ListView) findViewById(R.id.lv_chat_room);
        myMsg = (EditText) findViewById(R.id.myMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        chatMsgList = new ArrayList<>();

        chatObj = getIntent().getStringExtra("username");
        titleBar.setTitleText(chatObj);

        adapterChatMsgList = new AdapterChatMsg(AtyChatRoom.this, R.layout.chat_other, chatMsgList);

        listView.setAdapter(adapterChatMsgList);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = myMsg.getText().toString();
                if (!content.isEmpty()) {
                    ChatMsg msg = new ChatMsg();
                    msg.setContent(content);
                    msg.setUsername("hello");
                    msg.setIconID(R.drawable.avasterwe);
                    msg.setMyInfo(true);
                    msg.setChatObj(chatObj);
                    chatMsgList.add(msg);
                    myMsg.setText("");
                }
            }
        });

        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
            @Override
            public void rightButtonClick() { }
        });
    }
}
