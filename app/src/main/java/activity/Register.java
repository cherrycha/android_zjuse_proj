package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.MyRegex;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText username, password, email, phone, nickname;
    String Msg = null;
    String Code = null;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        username = (EditText) findViewById(R.id.username_etx);

        password = (EditText) findViewById(R.id.password_etx);
        phone = (EditText) findViewById(R.id.phone_etx);
        nickname = (EditText) findViewById(R.id.nickname_etx);
        email = (EditText) findViewById(R.id.email_etx);
        findViewById(R.id.btn_contact_us).setOnClickListener(this);
        //todo:del
        username.setText("cherry");
        password.setText("123456");
        phone.setText("18963668889");
        nickname.setText("cha");
        email.setText("123@qq.com");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button://读写MifareClassic格式
                if (MyRegex.isValidEmail(email.getText().toString()) && MyRegex.isValidPhoneNumber(phone.getText().toString())) {
                    flag = 0;
                    HttpPost();
                    try {
                        while (flag == 0)
                            Thread.sleep(100);
                        Toast.makeText(this, Msg, Toast.LENGTH_SHORT).show();
                        if (Code.equals("0000")) {
                            startActivity(new Intent(this, MainActivity.class));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (!MyRegex.isValidEmail(email.getText().toString())) {
                        Toast.makeText(this, "Invalid Email Address, Please Check!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Invalid Phone Number, Please Check!", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.btn_contact_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", username.getText().toString())
                .add("phoneNumber", phone.getText().toString())
                .add("nickname", nickname.getText().toString())
                .add("email", email.getText().toString())
                .add("password", password.getText().toString())
                .add("type", "1")
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    System.out.println(result);
                    JSONObject responseobj = new JSONObject(result);
                    Code = responseobj.getString("resultCode");
                    Msg = responseobj.getString("resultMsg");
                    flag=1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                flag=1;
                System.out.println(e.getMessage());
            }
        });
    }
}
