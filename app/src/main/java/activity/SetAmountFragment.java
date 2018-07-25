package activity;

import android.graphics.Typeface;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.example.cherrycha.material_design.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;


public class SetAmountFragment extends DialogFragment implements View.OnClickListener {

    private static int flag = 0;
    Bundle bundle=new Bundle();
    String token;
    EditText txt_amount;
    View view;
    Integer amount;
    String card_no;
    String name;
    public SetAmountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_set_amount, container, false);
        token = getArguments().getString("token");
        card_no = getArguments().getString("card_no");
        bundle.putString("token", token);
        name=getArguments().getString("item_name");
        txt_amount =view.findViewById(R.id.id_txt_amount);
        final SpannableStringBuilder style1 = new SpannableStringBuilder();


        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.id_txt_confirm_payment:
                        amount = Integer.valueOf(txt_amount.getText().toString());
                        flag = 0;

//                        HttpPost();
//                        try {
//                            while (flag == 0)
//                                Thread.sleep(100);
//                        } catch (Exception e) {
//
//                        }

                        flag=1;
                        if (flag == 1) {//支付成功
                                Toast.makeText(view.getContext(), "Successful Payment", Toast.LENGTH_SHORT).show();
                            dismiss();
                        } else if (flag == 2) {//额度不足

                                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();

                        }
                        dismiss();
                }

            }
        };

        TextView continue_shopping = view.findViewById(R.id.id_txt_confirm_payment);


        String txt_confirm = getActivity().getString(R.string.txt_confirm);
        style1.append(txt_confirm);

        style1.setSpan(clickableSpan, 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style1.setSpan(new StyleSpan(Typeface.NORMAL), 0, txt_confirm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        continue_shopping.setMovementMethod(LinkMovementMethod.getInstance());
        continue_shopping.setText(style1);

        return view;

    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/userorder";
        JSONArray commodityList=new JSONArray();
        JSONObject commodityInfo=new JSONObject();
        try {
            Integer no=2;
//            switch("name"){
//                case "":
//            }
            commodityInfo.put("commodityId", no.toString());
            commodityInfo.put("count", amount);
            commodityList.put(0,commodityInfo );
        }catch(Exception e){

        }
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("type", "0")
                .add("bankcardId", card_no)
                .build(); // 表单键值对

        Request request = new Request.Builder().url(url).header("token", token).post(formBody).build(); // 请求
        System.out.println(commodityList.toString());
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
