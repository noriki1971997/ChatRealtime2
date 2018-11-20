package com.example.norik.chatrealtime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtUserName,edtPassword,edtComfirmPassword;
    Button btnSignUp;
    //List acc
    ArrayList<String> account;
    //mau cho chuoi username
    String usernamePattern = "\\w+";
    String passwordPattern = "\\w+";
    boolean checkUsernameSignUp;
    boolean checkPasswordSignUp;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.43.79:3001");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSocket.connect();
        mSocket.on("server-send-signupresult",onSignUpResult);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtComfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);

        //su kien an button dang ki
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiem tra tinh dung dan cua chuoi username
                checkUsernameSignUp = edtUserName.getText().toString().matches(usernamePattern);
                checkPasswordSignUp = edtPassword.getText().toString().matches(usernamePattern);
                if(!checkUsernameSignUp||!checkPasswordSignUp)
                {
                    Toast.makeText(getApplicationContext(),"Tên đăng nhập hoặc mật khẩu không được chứa kí tự đặc biệt",Toast.LENGTH_SHORT).show();
                }
                else if(edtPassword.getText().toString().length()<8)
                {
                    Toast.makeText(getApplicationContext(),"Độ dài mật khẩu phải lớn hơn 8",Toast.LENGTH_SHORT).show();
                }
                else if(!edtPassword.getText().toString().equals(edtComfirmPassword.getText().toString()))
                {
                    Toast.makeText(getApplicationContext(),"Mật khẩu xác thực sai",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"goi len server",Toast.LENGTH_SHORT).show();
                    mSocket.emit("client-send-username",edtUserName.getText().toString(),edtPassword.getText().toString());
                }

            }
        });

    }
    private Emitter.Listener onSignUpResult = new Emitter.Listener() {
        @Override
        public void call(final Object...args) {
           runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String content;
                    try {
                         content = data.getString("noidung");
                        if(content == "true")
                        {
                            Toast.makeText(getApplicationContext(),"Đăng kí thành công",Toast.LENGTH_SHORT).show();
                            Intent moveToLoginScreen = new Intent(MainActivity.this,login.class);
                            startActivity(moveToLoginScreen);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Tên đăng nhập đã tồn tại",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
}
