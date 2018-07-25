package activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import model.MyRegex;
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


public class AddAddressFragment extends DialogFragment implements View.OnClickListener {

    private static int flag = 0;
    Bundle bundle=new Bundle();
    String token;
    String phoneNumber;
    String addressDescription;
    EditText txt_address;
    EditText txt_phone_no;
    View view;

    public AddAddressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_address, container, false);
        token = getArguments().getString("token");
        bundle.putString("token", token);
        txt_address=view.findViewById(R.id.id_txt_new_address);
        txt_phone_no=view.findViewById(R.id.id_txt_new_phone);
        final SpannableStringBuilder style1 = new SpannableStringBuilder();

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Fragment fragment=null;
                switch (view.getId()) {
                    case R.id.id_txt_confirm:
                        MyRegex regex=new MyRegex();
                        phoneNumber = txt_phone_no.getText().toString();
                        addressDescription = txt_address.getText().toString();
                        if(MyRegex.isValidPhoneNumber(phoneNumber)){
                            flag = 0;
                            HttpPost();
                            try {
                                while (flag == 0)
                                    Thread.sleep(100);
                            } catch (Exception e) {

                            }

                            if (flag == 1) {//修改成功
                                fragment = new AddressesFragment();
                                dismiss();
                            } else if (flag == 2) {//原密码错误
//                                Looper.prepare();
//                                Toast.makeText(getActivity(), "Wrong Old Password, Please Check", Toast.LENGTH_LONG).show();
//                                Looper.loop();
                            }
                            dismiss();
                        }else{
                            Toast.makeText(getActivity(), "Invalid Phone Number, Please Check", Toast.LENGTH_LONG).show();
                        }

                }
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.commit();
                }

            }
        };

        TextView continue_shopping = view.findViewById(R.id.id_txt_confirm);


        String txt_confirm = getActivity().getString(R.string.txt_confirm);
        style1.append(txt_confirm);

        style1.setSpan(clickableSpan, 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style1.setSpan(new StyleSpan(Typeface.NORMAL), 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        continue_shopping.setMovementMethod(LinkMovementMethod.getInstance());
        continue_shopping.setText(style1);

        return view;

    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/address";

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("addressDescription", addressDescription)
                .add("phoneNumber", phoneNumber)
                .build(); // 表单键值对

        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求
        client.newCall(request).enqueue(new Callback() { // 回调
            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = response.body().string();
                    JSONObject responseobj = new JSONObject(result);
                    if (responseobj.getString("resultCode").equals("0000")) {
                        flag = 1;
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

    @Override
    public void onClick(View view) {

    }
}
