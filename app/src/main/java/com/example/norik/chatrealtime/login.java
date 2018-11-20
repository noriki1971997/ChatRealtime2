package com.example.norik.chatrealtime;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class login extends AppCompatActivity {
    EditText edtUserNameLogin,edtPasswordLogin;
    Button btnLogin,btnGotoSignUp;
    ArrayList<String> accArrOnline ;
    TextView tvDangKiTaiKhoan;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.43.79:3001");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSocket.connect();
        mSocket.on("server-send-loginresult",onLoginResult);


        tvDangKiTaiKhoan = findViewById(R.id.tvdangnhap);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        edtUserNameLogin = findViewById(R.id.edtUserNameLogin);
        btnLogin = findViewById(R.id.btnLogin);
        btnGotoSignUp = findViewById(R.id.btnGotoSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.emit("client-login",edtUserNameLogin.getText().toString(),edtPasswordLogin.getText().toString());
            }
        });
        btnGotoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignUpScreen = new Intent(login.this,MainActivity.class);
                startActivity(moveToSignUpScreen);
            }
        });
        mSocket.on("server-send-accountonline",onAccountOnlineResult);
    }
    private Emitter.Listener onLoginResult = new Emitter.Listener() {
        @Override
        public void call(final Object...args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String content;
                    try {
                        content = data.getString("loginresult");
                        if(content == "true")
                        {
                            Toast.makeText(getApplicationContext(),"Dang nhap thanh cong",Toast.LENGTH_SHORT).show();
                            Intent moveToMain = new Intent(login.this,mainscreen.class);
                            moveToMain.putStringArrayListExtra("accOnline",accArrOnline);
                            moveToMain.putExtra("ten tai khoan",edtUserNameLogin.getText().toString());
                            startActivity(moveToMain);
                        }
                        if(content == "false")
                        {
                            Toast.makeText(getApplicationContext(),"Ten tai khoan hoac password khong dung",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
    private Emitter.Listener onAccountOnlineResult = new Emitter.Listener() {
        @Override
        public void call(final Object...args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray content;
                    try {
                        accArrOnline = new ArrayList<String>();
                        content = data.getJSONArray("accArr");

                        for(int i= 0;i<content.length();i++)
                        {
                            accArrOnline.add(content.get(i).toString());
                        }
                        for(int i= 0;i<content.length();i++)
                        {
                            Log.e("kiem tra",accArrOnline.get(i));
                        }

                    }catch(JSONException E)
                    {
                        Log.e("Excep nhan",E.getMessage());
                        return;
                    }

                }
            });
        }
    };
}
