package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.example.cherrycha.material_design.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class EditCardFragment extends Fragment implements View.OnClickListener {
    View rootView;
    EditText et_name;
    EditText et_email;
    EditText et_nickname;
    EditText et_phone_no;
    EditText et_password;
    String token;
    private static int flag = 0;

    public EditCardFragment() {
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
                            et_password = rootView.findViewById(R.id.txt_password);
                            et_password.setText(getArguments().getString(infos[i]));
                            break;
                        case 5:
                            token = getArguments().getString(infos[i]);
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
        Bundle bundle = new Bundle();
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.save_button:
                HttpPost();
                try {
                    while (flag == 0)
                        Thread.sleep(100);
                } catch (Exception e) {

                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack(null, 0);
                break;
        }

//        if (fragment != null) {
//            fragment.setArguments(bundle);
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//        }
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/user/info";

        OkHttpClient client = new OkHttpClient();
        System.out.println(token);
        String nickname = et_nickname.getText().toString();
        String phoneNumber = et_phone_no.getText().toString();
        String email = et_email.getText().toString();
        RequestBody formBody = new FormBody.Builder()
                .add("nickname", nickname)
                .add("phoneNumber", phoneNumber)
                .add("email", email)
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
//                Looper.prepare();
//                Toast.makeText(getActivity(), "done~",Toast.LENGTH_SHORT).show();
//                Looper.loop();
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