package activity;

import android.content.Intent;
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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddCardFragment extends Fragment implements View.OnClickListener {
    View rootView;
    EditText et_name;
    EditText et_bank;
    EditText et_balance;
    EditText et_card_no;
    EditText et_password;
    String token;
    Bundle bundle=new Bundle();
    private static int flag = 0;

    public AddCardFragment() {
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
        rootView = inflater.inflate(R.layout.fragment_add_card, container, false);
        rootView.findViewById(R.id.save_button).setOnClickListener(this);
        token = getArguments().getString("token");
        bundle.putString("token", token);
        String name = getArguments().getString("name");
        bundle.putString("name",name);
        if (getArguments() != null) {
            try {
                et_card_no = rootView.findViewById(R.id.txt_card_no);
                et_bank = rootView.findViewById(R.id.txt_bank);
                et_password = rootView.findViewById(R.id.txt_password);

            } catch (NullPointerException e) {

            }
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.save_button:
                HttpPost();
                try {
                    while (flag == 0)
                        Thread.sleep(100);
                } catch (Exception e) {

                }
                if (flag == 3) {
                    Toast.makeText(getActivity(), "System Error", Toast.LENGTH_SHORT).show();
                } else if (flag == 2) {
                    Toast.makeText(getActivity(), "Wrong card or password", Toast.LENGTH_SHORT).show();
                } else if (flag == 1) {
                    Toast.makeText(getActivity(), "Card Added", Toast.LENGTH_SHORT).show();
                    fragment=new CardFragment();
                }

                break;
        }

        if (fragment != null) {
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/bankcard-user";

        OkHttpClient client = new OkHttpClient();

        String password = et_password.getText().toString();
        String card=et_card_no.getText().toString();
        System.out.println(token);
        RequestBody formBody = new FormBody.Builder()
                .add("bankcardNumber", card)
                .add("bankcardPassword", password)
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = new String(response.body().string());
                    System.out.println(result);
                    JSONObject responseobj = new JSONObject(result);

                    if (responseobj.getString("resultCode").equals("0000")) {
                        flag = 1;
                    } else if (responseobj.getString("resultCode").equals("9999")) {
                        flag = 3;
                    } else {
                        flag = 2;
                    }
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