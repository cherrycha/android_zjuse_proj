package activity;

import android.app.Activity;
import android.content.Intent;
import android.icu.util.EthiopicCalendar;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import model.MyRegex;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    View rootView;
    EditText et_email, et_nickname, et_phone_no;
    String nickname, email, phoneNumber, token;
    Bundle bundle = new Bundle();
    private static int flag = 0;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //碎片的简单用法
        //新建碎片布局.xml，在代码中extends Fragment，重写onCreateView（）方法，将刚才定义的布局动态加载进来。
        rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        rootView.findViewById(R.id.save_button).setOnClickListener(this);
        String[] infos = getActivity().getResources().getStringArray(R.array.info_labels);
        token = getArguments().getString("token");
        if (getArguments() != null) {
            try {
                for (int i = 0; i < infos.length; i++) {
                    switch (i) {
                        case 0:
//                            et_name = rootView.findViewById(R.id.txt_name);
//                            et_name.setText(getArguments().getString(infos[i]));
//                            break;
                        case 1:
                            et_phone_no = rootView.findViewById(R.id.txt_phone);
                            et_phone_no.setText(getArguments().getString(infos[i]));
                            break;
                        case 2:
                            et_email = rootView.findViewById(R.id.txt_email);
                            et_email.setText(getArguments().getString(infos[i]));
                            break;
                        case 3:
                            et_nickname = rootView.findViewById(R.id.txt_nickname);
                            et_nickname.setText(getArguments().getString(infos[i]));
                            break;
                        case 4:
//                            et_password = rootView.findViewById(R.id.txt_password);
//                            et_password.setText(getArguments().getString(infos[i]));
//                            break;
                        default:
                            break;
                    }
                }
            } catch (NullPointerException e) {

            }


        }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                nickname = et_nickname.getText().toString();
                phoneNumber = et_phone_no.getText().toString();
                email = et_email.getText().toString();
                if (MyRegex.isValidEmail(email) && MyRegex.isValidPhoneNumber(phoneNumber)) {
                    flag = 0;
                    HttpPost();
                    try {
                        while (flag == 0)
                            Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    Toast.makeText(getActivity(), "done~", Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack(null, 0);
                } else {
                    if (!MyRegex.isValidPhoneNumber(phoneNumber)) {
                        Toast.makeText(getActivity(), "Invalid Phone Number, Please Check!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Invalid Email Address, Please Check!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user/info";

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("nickname", nickname)
                .add("phoneNumber", phoneNumber)
                .add("email", email)
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {

                flag = 1;
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                System.out.println(e.getMessage());
            }
        });
    }

    private void sendResult(int resultOk) {
        TextView tv;
        if (getTargetFragment() == null) {
            return;
        } else {
            Intent i = new Intent();
//            i.putExtra("name", );
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultOk, i);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}