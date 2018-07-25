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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CardFragment extends Fragment implements View.OnClickListener {
    View rootView;
    TextView et_card_no;
    TextView et_balance;
    TextView et_bank;
    TextView et_name;
    Integer id;
    String token;
    Bundle bundle = new Bundle();
    JSONArray cards=new JSONArray();

    private static int flag = 0;

    public CardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //token
        token = getArguments().getString("token");
        String name = getArguments().getString("name");
        bundle.putString("name",name);
        bundle.putString("token", token);

        flag=0;
        HttpPost();
        try {
            while (flag == 0)
                Thread.sleep(100);
        } catch (Exception e) {

        }
        if (cards.length() > 0) {
            rootView = inflater.inflate(R.layout.fragment_card, container, false);
            et_card_no = rootView.findViewById(R.id.card_tv);
            et_name = rootView.findViewById(R.id.name_tv);
            et_balance = rootView.findViewById(R.id.balance_tv);
            et_bank = rootView.findViewById(R.id.bank_tv);
//            rootView.findViewById(R.id.edit_button).setOnClickListener(this);
            rootView.findViewById(R.id.delete_button).setOnClickListener(this);
            try {
                id=cards.getJSONObject(0).getInt("relationshipId");
                et_card_no.setText(String.valueOf(cards.getJSONObject(0).getLong("bankcardNumber")));
                et_balance.setText(String.valueOf(cards.getJSONObject(0).getDouble("balance")));
                et_bank.setText("掌柜银行");
                et_name.setText(name);
            }catch(Exception e){

            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_empty_card, container, false);
            rootView.findViewById(R.id.btn_add_card).setOnClickListener(this);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        bundle.putString("token", token);
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.btn_add_card:
                fragment = new AddCardFragment();
                break;
            case R.id.delete_button:
                flag=0;
                HttpPost_del_card();
                try {
                    while (flag == 0)
                        Thread.sleep(100);
                } catch (Exception e) {

                }
                if(flag==1){
                    Toast.makeText(getActivity(), "Card Deleted", Toast.LENGTH_SHORT).show();
                }
                fragment = new CardFragment();
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
        String url = "http://120.79.132.224:9090/shopkeeper/bankcard-user/list";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("token", token).build(); // 请求
        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);
                   if(result!="") {
                       if (responseobj.getString("resultCode").equals("0000")) {
                           cards = responseobj.getJSONArray("bankcardList");//通过user字段获取其所包含的JSONObject对象
                       } else {
                           Looper.prepare();
                           Toast.makeText(getActivity(), "Fail to get userInfo", Toast.LENGTH_SHORT).show();
                           Looper.loop();
                       }
                       flag=1;
                   }else{
                       flag=2;
                   }
                } catch (Exception e) {

                }
            }
            public void onFailure(Call call, IOException e) {
                // 请求失败调用
                System.out.println(e.getMessage());
            }
        });
    }

    public void HttpPost_del_card() {
        String url = "http://120.79.132.224:9090/shopkeeper/bankcard-user";

        OkHttpClient client = new OkHttpClient();
        String id=this.id.toString();
        RequestBody formBody = new FormBody.Builder()
                .add("bankcardUserRelationshipId",id.toString())
                .build(); // 表单键值对
        Request request = new Request.Builder().url(url+"?bankcardUserRelationshipId="+id).header("token", token).delete().build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);
                    result=responseobj.getString("resultCode");
                    if (responseobj.getString("resultCode").equals("0000")) {
//                        JSONObject user = responseobj.getJSONObject("bankcardList");//通过user字段获取其所包含的JSONObject对象
//                        name = user.getString("username");
//                        Long phone = user.getLong("phoneNumber");
//                        String nickname = user.getString("nickname");
//                        String email = user.getString("email");
//                        nickname_txt.setText(nickname);
//                        username_txt.setText(name);
//                        phone_no_txt.setText(String.valueOf(phone));
//                        email_txt.setText(email);
                    } else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "Fail to get userInfo", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {

                }
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