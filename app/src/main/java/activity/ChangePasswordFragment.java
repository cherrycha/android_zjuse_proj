package activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import cn.example.cherrycha.material_design.R;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangePasswordFragment extends DialogFragment implements View.OnClickListener {

    private static int flag = 0;
    String token;
    String password;
    String old_password;
    EditText txt_password1;
    EditText txt_password2;
    EditText txt_old_password;
    View view;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_passwd, container, false);

        final SpannableStringBuilder style1 = new SpannableStringBuilder();
        txt_old_password = view.findViewById(R.id.id_txt_old_password);
        txt_password1 = view.findViewById(R.id.id_txt_confirm_password);
        txt_password2 = view.findViewById(R.id.id_txt_new_password);

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.id_txtbtn_confirm_change_passwd:
                        if (txt_password1.getText().toString().equals(txt_password2.getText().toString())) {
                            password = txt_password2.getText().toString();
                            old_password = txt_old_password.getText().toString();
                            flag = 0;
                            HttpPost();
                            try {
                                while (flag == 0)
                                    Thread.sleep(100);
                            } catch (Exception e) {

                            }

                            if (flag == 1) {//修改成功
                                Toast.makeText(view.getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();

                                dismiss();
                            } else if (flag == 2) {//原密码错误
                                Toast.makeText(getActivity(), "原密码错误，请重新输入", Toast.LENGTH_LONG).show();
                            }

                        } else {

                            Toast.makeText(getActivity(), "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();

                        }
                        dismiss();
                }

            }
        };

        TextView continue_shopping = view.findViewById(R.id.id_txtbtn_confirm_change_passwd);


        String txt_confirm = getActivity().getString(R.string.txt_confirm);
        style1.append(txt_confirm);

        style1.setSpan(clickableSpan, 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style1.setSpan(new StyleSpan(Typeface.NORMAL), 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        continue_shopping.setMovementMethod(LinkMovementMethod.getInstance());
        continue_shopping.setText(style1);

        return view;

    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user/password";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("oldPassword", old_password)
                .add("newPassword", password)
                .build(); // 表单键值对
        token = getArguments().getString("token");

        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求
        client.newCall(request).enqueue(new Callback() { // 回调
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);
                    if (responseobj.getString("resultCode").equals("0000")) {
                        flag = 1;
                    } else {
                        flag = 2;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    flag=2;
                }
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                System.out.println(e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
