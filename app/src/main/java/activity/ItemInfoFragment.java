package activity;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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


public class ItemInfoFragment extends Fragment implements View.OnClickListener {

    private ImageView image;
    private TextView name,descript,price,location;
    private View rootView;
    private String token;
    private static int flag=0;
    public ItemInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_item_info, container, false);

        Integer itemNum = getArguments().getInt("ItemNum");


        name = (TextView)rootView.findViewById(R.id.item_name);
        image = (ImageView)rootView.findViewById(R.id.item_image);
        descript = (TextView)rootView.findViewById(R.id.item_intro);
        descript.setText("");
        price = (TextView)rootView.findViewById(R.id.item_price);
        //location= (TextView)rootView.findViewById(R.id.item_location);

        HttpPost(itemNum);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Button card_add_to_cart=rootView.findViewById(R.id.btn_add_to_cart);
        card_add_to_cart.setOnClickListener(this);

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
                    JSONObject responseobj=new JSONObject(result);
                    JSONObject info = responseobj.getJSONObject("commodityInfo");//通过user字段获取其所包含的JSONObject对象

                    String in_name = info.getString("commodityName");
                    String in_description = info.getString("description");
                    String in_location = info.getString("location");
                    Double in_price = info.getDouble("price");
                    String in_pic = info.getString("picture");

                    name.setText(in_name);
                    descript.setText(in_description);
                    //location.setText(in_location);
                    price.setText("CNY ￥"+String.valueOf(in_price));
                    if(in_pic.equals("apple")){
                        image.setImageResource(R.drawable.apple);
                    }else if(in_pic.equals("water")){
                        image.setImageResource(R.drawable.water);
                    }else if(in_pic.equals("pen")){
                        image.setImageResource(R.drawable.pen);
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
        switch (view.getId()){
            case R.id.btn_add_to_cart:
                ItemAddedFragment itemAddedDialog = new ItemAddedFragment();
                itemAddedDialog.show(getFragmentManager(), "EditNameDialog");
                break;
            case R.id.btn_buy_now:
                try {
                    while(flag==0){
                        Thread.sleep(100);
                    }
                }catch(Exception e){

                }

        }
    }
}
