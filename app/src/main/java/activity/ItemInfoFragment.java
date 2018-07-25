package activity;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import org.json.JSONArray;
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


public class ItemInfoFragment extends Fragment implements View.OnClickListener {

    private ImageView image;
    private TextView name, descript, price, location;
    private View rootView;
    private String token;
    private String item_name;

    Button btn_buy_now;
    JSONArray cards = new JSONArray();

    private static int flag = 0;
    Bundle bundle = new Bundle();

    public ItemInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_item_info, container, false);

        Integer itemNum = getArguments().getInt("ItemNum");
        token = getArguments().getString("token");
        bundle.putString("token", token);

        btn_buy_now = rootView.findViewById(R.id.btn_buy_now);
        btn_buy_now.setOnClickListener(this);
        name = (TextView) rootView.findViewById(R.id.item_name);
        image = (ImageView) rootView.findViewById(R.id.item_image);
        descript = (TextView) rootView.findViewById(R.id.item_intro);
        descript.setText("");
        price = (TextView) rootView.findViewById(R.id.item_price);
        //location= (TextView)rootView.findViewById(R.id.item_location);
        flag = 0;
        HttpPost(itemNum);

        try {
            while (flag == 0)
                Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Button card_add_to_cart=rootView.findViewById(R.id.btn_add_to_cart);
//        card_add_to_cart.setOnClickListener(this);

        return rootView;
    }

    public void HttpPost(int itemNum) {
        String url = "http://120.79.132.224:9090/shopkeeper/commodity?commodityId=" + String.valueOf(itemNum);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    System.out.println(result);
                    JSONObject responseobj = new JSONObject(result);
                    JSONObject info = responseobj.getJSONObject("commodityInfo");//通过user字段获取其所包含的JSONObject对象

                    item_name = info.getString("commodityName");

                    bundle.putString("item_name", item_name);
                    String in_description = info.getString("description");
                    String in_location = info.getString("location");
                    Double in_price = info.getDouble("price");
                    String in_pic = info.getString("picture");

                    name.setText(item_name);
                    descript.setText(in_description);
                    //location.setText(in_location);
                    price.setText("CNY ￥" + String.valueOf(in_price));
                    if (in_pic.equals("apple")) {
                        image.setImageResource(R.drawable.apple);
                    } else if (in_pic.equals("water")) {
                        image.setImageResource(R.drawable.water);
                    } else if (in_pic.equals("pen")) {
                        image.setImageResource(R.drawable.pen);
                    }
                    flag = 1;
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

    public void HttpPost_card() {
        String url = "http://120.79.132.224:9090/shopkeeper/bankcard-user/list";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).header("token", token).build(); // 请求
        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                // 请求成功调用，该回调在子线程
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);
                    if (result != "") {
                        if (responseobj.getString("resultCode").equals("0000")) {
                            cards = responseobj.getJSONArray("bankcardList");//通过user字段获取其所包含的JSONObject对象
                            flag = 1;
                        } else {
                            flag = 2;
                        }

                    } else {
                        flag = 2;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_add_to_cart:
//                ItemAddedFragment itemAddedDialog = new ItemAddedFragment();
//                itemAddedDialog.show(getFragmentManager(), "EditNameDialog");
//                break;
            case R.id.btn_buy_now:
                flag = 0;
                HttpPost_card();
                try {
                    while (flag == 0)
                        Thread.sleep(100);
                } catch (Exception e) {

                }

                if (cards.length() > 0) {
                    SetAmountFragment itemAddedDialog = new SetAmountFragment();
                    try {
                        String card_no = String.valueOf(cards.getJSONObject(0).getInt("relationshipId"));
                        bundle.putString("card_no", card_no);
                    } catch (Exception e) {

                    }
                    itemAddedDialog.setArguments(bundle);
                    itemAddedDialog.show(getFragmentManager(), "EditNameDialog");

                } else {
                    Toast.makeText(getActivity(), "Please add a card first!", Toast.LENGTH_LONG).show();
                }

        }
    }
}
