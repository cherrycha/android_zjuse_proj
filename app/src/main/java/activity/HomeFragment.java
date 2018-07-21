package activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.cherrycha.material_design.R;

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


public class HomeFragment extends Fragment implements View.OnClickListener {

    private View rootView;

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
        HttpPost();
//        TextView textView = (TextView) rootView.findViewById(R.id.username_tv);
//        String token = getArguments().getString("token");
//        textView.setText(token);
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
        String url = "http://120.79.132.224:9090/shopkeeper/user/token";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("account", "admin")
                .add("password","hello")
                .add("method","0")
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    System.out.println(result);
                    JSONObject responseobj=new JSONObject(result);
                    JSONObject user = responseobj.getJSONObject("userInfo");//通过user字段获取其所包含的JSONObject对象

                    String name = user.getString("username");
                    Integer phone = user.getInt("phoneNumber");
                    String nickname = user.getString("nickname");
                    String email = user.getString("email");
                    String token = user.getString("token");

                    TextView username_text = (TextView) rootView.findViewById(R.id.username_tv);
                    username_text.setText(name);
                    TextView phone_text = (TextView) rootView.findViewById(R.id.phone_tv);
                    phone_text.setText(String.valueOf(phone));
                    TextView email_text = (TextView) rootView.findViewById(R.id.email_tv);
                    email_text.setText(email);
                    TextView nickname_text = (TextView) rootView.findViewById(R.id.nickname_tv);
                    nickname_text.setText(nickname);


                } catch (JSONException e) {
                    e.printStackTrace();
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
        Bundle bundle = new Bundle();
        Fragment fragment=null;
        switch (view.getId()){
            case R.id.edit_button:
                fragment=new EditProfileFragment();
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