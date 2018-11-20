package com.example.norik.chatrealtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mainscreen extends AppCompatActivity {
    ArrayList<RowItemsUserOnlineData> userOnlineList = new ArrayList<>();
    RecyclerView uOnlRecylView;
    String usernameLogin;
    LinearLayoutManager linearLayoutManager;
    ItemUserOnlineListAdapter userOnlineListAdapter;
    MyDatabaseHelper databaseHelper;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.43.79:3001");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        mSocket.connect();
        mSocket.on("server-send-accountonline",onUpdataAccOnlineList);
        mSocket.on("receive-message", onReceivedMsg);
        Intent intent = getIntent();
        ArrayList<String> accArr = intent.getStringArrayListExtra("accOnline");
        usernameLogin = intent.getStringExtra("ten tai khoan");

        uOnlRecylView = findViewById(R.id.UOnlRecycView);
        uOnlRecylView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        uOnlRecylView.setLayoutManager(linearLayoutManager);
        databaseHelper = new MyDatabaseHelper(this,usernameLogin);
        for(int i = 0;i<accArr.size();i++)
        {
            if(!accArr.get(i).equals(usernameLogin))
            {
                userOnlineList.add(new RowItemsUserOnlineData(accArr.get(i),R.drawable.circular));
                databaseHelper.addTableName(accArr.get(i));
                databaseHelper.createTable();
            }

        }
        Toast.makeText(getApplicationContext(),userOnlineList.size()+" dang nhap",Toast.LENGTH_SHORT).show();
        userOnlineListAdapter = new ItemUserOnlineListAdapter(userOnlineList,getApplicationContext());
        uOnlRecylView.setAdapter(userOnlineListAdapter);

        userOnlineListAdapter.setOnItemClickedListener(new ItemUserOnlineListAdapter.OnItemClickedListener() {
            @Override
            public void onItemClick(String username) {
                Intent moveToChatScreen = new Intent(mainscreen.this,MessageList.class);
                moveToChatScreen.putExtra("ten ban",username);
                moveToChatScreen.putExtra("ten tai khoan",usernameLogin);
                startActivity(moveToChatScreen);

            }
        });
    }
    private Emitter.Listener onUpdataAccOnlineList = new Emitter.Listener() {
        @Override
        public void call(final Object...args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray content;
                    userOnlineList.clear();
                    try {
                        ArrayList<String> accArrOnline = new ArrayList<String>();
                        content = data.getJSONArray("accArr");

                        for(int i= 0;i<content.length();i++)
                        {
                            accArrOnline.add(content.get(i).toString());
                        }
                        for(int i = 0;i<accArrOnline.size();i++)
                        {
                            if(!accArrOnline.get(i).equals(usernameLogin))
                            {
                                userOnlineList.add(new RowItemsUserOnlineData(accArrOnline.get(i),R.drawable.circular));
                            }

                        }
                        Toast.makeText(getApplicationContext(),userOnlineList.size()+"",Toast.LENGTH_SHORT).show();
                        userOnlineListAdapter.notifyDataSetChanged();


                    }catch(JSONException E)
                    {
                        Log.e("Excep nhan",E.getMessage());
                        return;
                    }

                }
            });
        }
    };
    private Emitter.Listener onReceivedMsg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String msg,name;
                    try {
                        msg = data.getString("msg");
                        name = data.getString("name");
                        databaseHelper.addTableName(name);
                        databaseHelper.addMessage(name, msg);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("exit","thoat");
        mSocket.emit("user-exit",true);
    }
}
