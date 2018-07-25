package activity;

import android.content.Intent;
import android.os.Looper;
import android.os.Trace;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import cn.example.cherrycha.material_design.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username, password;
    int flag = 0;
    String name, nickname;
    Integer phone;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username_etx);

        password = (EditText) findViewById(R.id.password_etx);
        username.setText("cherry");
        password.setText("123456");
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        findViewById(R.id.btn_contact_us).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_button:
                flag=0;
                HttpPost();
                try {
                    while (flag == 0)
                        Thread.sleep(100);
                } catch (Exception e) {

                }
                if(flag==1){

                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, ModelActivity.class);
                    intent.putExtra("username", name);//传递数据
                    intent.putExtra("phone", phone);//传递数据
                    intent.putExtra("nickname", nickname);//传递数据
                    intent.putExtra("token", token);//传递数据
                    intent.putExtra("name", name);//传递数据
                    startActivity(intent);
                }else if(flag==2){
                    Toast.makeText(getApplicationContext(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.register_button:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.btn_contact_us:
                startActivity(new Intent(this, AboutUsActivity.class));
        }
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user/token";
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("account", username.getText().toString())
                .add("password", password.getText().toString())
                .add("method", "0")
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);

                    if (responseobj.getString("resultCode").equals("0000")) {
                        JSONObject user = responseobj.getJSONObject("userInfo");//通过user字段获取其所包含的JSONObject对象
                        name = user.getString("username");
                        phone = user.getInt("phoneNumber");
                        nickname = user.getString("nickname");
                        token = user.getString("token");
                        flag=1;
                    } else {
                        flag=2;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                Looper.prepare();
                Toast.makeText(MainActivity.this, "Bad Network", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }
}