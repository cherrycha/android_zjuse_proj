package activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import cn.example.cherrycha.material_design.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView email_txt;
    TextView nickname_txt;
    TextView phone_no_txt;
    TextView username_txt;
    String name;
    String email = "123@123.com";
    String nickname = "Admin123456";
    String phone_no = "1234567890";
    String password = "hello";
    String token;
    Bundle bundle = new Bundle();
    private View rootView;
    private static int flag = 0;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        token = getArguments().getString("token");
        bundle.putString("token", token);
        System.out.println("token:"+token);
        flag = 0;
        nickname_txt = rootView.findViewById(R.id.nickname_tv);
        email_txt = rootView.findViewById(R.id.email_tv);
        phone_no_txt = rootView.findViewById(R.id.phone_tv);
        username_txt = rootView.findViewById(R.id.username_tv);
        rootView.findViewById(R.id.btn_contact_us).setOnClickListener(this);
        //---------------------------------------------
        TextView txt_change_password = rootView.findViewById(R.id.id_txt_change_password);
        final SpannableStringBuilder style1 = new SpannableStringBuilder();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("token", token);
                android.support.v4.app.Fragment fragment = null;
                switch (view.getId()) {
                    case R.id.id_txt_change_password:
                        ChangePasswordFragment itemAddedDialog = new ChangePasswordFragment();
                        itemAddedDialog.setArguments(bundle);
                        itemAddedDialog.show(getFragmentManager(), "EditNameDialog");
                }
//                if (fragment != null) {
//                    fragment.setArguments(bundle);
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.container_body, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }

            }
        };
        String txt_continue_shopping = getActivity().getString(R.string.txt_change_password);
        style1.append(txt_continue_shopping);

        style1.setSpan(clickableSpan, 0, txt_continue_shopping.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style1.setSpan(new StyleSpan(Typeface.NORMAL), 0, txt_continue_shopping.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_change_password.setMovementMethod(LinkMovementMethod.getInstance());
        txt_change_password.setText(style1);
        //------------------------------------------------


        HttpPost();
        try {
            while (flag == 0)
                Thread.sleep(100);
        } catch (Exception e) {

        }

        rootView.findViewById(R.id.edit_button).setOnClickListener(this);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("token", token).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    System.out.println(result);
                    JSONObject responseobj = new JSONObject(result);

                    if (responseobj.getString("resultCode").equals("0000")) {
                        JSONObject user = responseobj.getJSONObject("userInfo");//通过user字段获取其所包含的JSONObject对象
                        name = user.getString("username");
                        Long phone = user.getLong("phoneNumber");
                        String nickname = user.getString("nickname");
                        String email = user.getString("email");
                        nickname_txt.setText(nickname);
                        username_txt.setText(name);
                        phone_no_txt.setText(String.valueOf(phone));
                        email_txt.setText(email);
                    } else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "Fail to get userInfo", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    flag = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                System.out.println(e.getMessage());
                flag = 2;
            }
        });
    }

    @Override
    public void onClick(View view) {
//        Intent intent = getActivity().getIntent();
//        Bundle bundle = intent.getExtras();

        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.edit_button:
                fragment = new EditProfileFragment();
                bundle.putString("name", name);
                bundle.putString("nickname", nickname_txt.getText().toString());
                bundle.putString("email", email_txt.getText().toString());
                bundle.putString("password", password);
                bundle.putString("phone", phone_no_txt.getText().toString());
                bundle.putString("token", token);
                break;
            case R.id.btn_contact_us:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}