package com.example.norik.chatrealtime;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MessageList extends AppCompatActivity {
    ArrayList<item_message_list_data> message_list_data = new ArrayList<>();
    RecyclerView recyclerView;
    TextView tvNameOfUser;
    LinearLayoutManager linearLayoutManager;
    MessageListAdapter messageListAdapter;
    String friendName;
    String usernameLogin;
    MyDatabaseHelper databaseHelper;
    EditText edtMessageInput;
    ImageButton btnSend;
    private Socket mSocket;
    ScrollView scrllView;
    {
        try {
            mSocket = IO.socket("http://192.168.43.79:3001");
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        tvNameOfUser = findViewById(R.id.tvNameOfUser);
        recyclerView = findViewById(R.id.recyclerViewChat);
        recyclerView.setHasFixedSize(true);
        edtMessageInput = findViewById(R.id.edtMessageInput);
        btnSend = findViewById(R.id.btnSendMessage);
        scrllView = findViewById(R.id.scrll);
        scrllView.post(new Runnable() {
            @Override
            public void run() {
                scrllView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
        mSocket.connect();
        mSocket.on("receive-message", onReceivedMsg);

        //get du lieu tu nam hinh danh sach nguoi online
        Intent intent = getIntent();
        friendName = intent.getStringExtra("ten ban");
        usernameLogin = intent.getStringExtra("ten tai khoan");
        tvNameOfUser.setText(friendName);
        Log.e("kiem tra ten :",friendName+" "+usernameLogin);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        databaseHelper = new MyDatabaseHelper(this,usernameLogin);
        databaseHelper.addTableName(friendName);
        databaseHelper.createTable();
        message_list_data = databaseHelper.getAllMessage();

        messageListAdapter = new MessageListAdapter(getApplicationContext(), message_list_data, usernameLogin);
        recyclerView.setAdapter(messageListAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.addMessage(usernameLogin, edtMessageInput.getText().toString());
                item_message_list_data item = new item_message_list_data();

                item.setName(usernameLogin);
                item.setMessage(edtMessageInput.getText().toString());
                message_list_data.add(item);

                mSocket.emit("send-msg-to-friend", edtMessageInput.getText().toString(), friendName);
                edtMessageInput.setText("");
                messageListAdapter.notifyDataSetChanged();
            }
        });

    }

    private Emitter.Listener onReceivedMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    message_list_data = databaseHelper.getAllMessage();
                    messageListAdapter.notifyDataSetChanged();
                }
            });
        }
    };
}
