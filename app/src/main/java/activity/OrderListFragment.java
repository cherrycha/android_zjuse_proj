package activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.AddressDrawerAdapter;
import adapter.OrderDrawerAdapter;
import model.AddressDrawerItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OrderListFragment extends Fragment  {
    RecyclerView recyclerView;
    Bundle bundle = new Bundle();
    String token;
    private static int flag = 0;
    private static JSONArray orders = new JSONArray();
    private FragmentDrawer drawerFragment;
    private FragmentDrawer.FragmentDrawerListener drawerListener;

    public OrderListFragment() {
        // Required empty public constructor

    }

    public void setDrawerListener(FragmentDrawer.FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        orders = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flag = 0;
        View rootView;
        token = getArguments().getString("token");
        bundle.putString("token", token);
        HttpPost();
        try {
            while (flag == 0) {
                Thread.sleep(100);
            }
        } catch (Exception e) {

        }
        if(orders.length()>0) {
            rootView = inflater.inflate(R.layout.fragment_order, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview

            recyclerView.setAdapter(new OrderDrawerAdapter(getActivity(), getData(orders)));
            recyclerView.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                }

                @Override
                public void onLongClick(View view, int position) {
                    DialogFragment fragment = new DeleteOrderFragment();
                    try {
                        Integer id=(orders.getJSONObject(position).getInt("id"));
                        bundle.putString("id", id.toString());
                    }catch(Exception e){

                    }
                    fragment.setArguments(bundle);
                    fragment.show(getFragmentManager(), "EditNameDialog");
                }
            }));
        }else {
            rootView = inflater.inflate(R.layout.fragment_empty_order_list, container, false);
        }
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/userorder/buyer-list";
        token = getArguments().getString("token");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("token", token).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);

                    if (responseobj.getString("resultCode").equals("0000")) {
                        orders = responseobj.getJSONArray("orderList");//通过user字段获取其所包含的JSONObject对象
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


    public static List<AddressDrawerItem> getData(JSONArray addresses) {
        List<AddressDrawerItem> data = new ArrayList<>();

        try {
            for (int i = 0; i < addresses.length(); i++) {
                AddressDrawerItem addrItem = new AddressDrawerItem();
                String order = addresses.getJSONObject(i).getString("orderNumber");
                String price = addresses.getJSONObject(i).getString("totalPrice");
                addrItem.setAddress(order);
                addrItem.setPhone_no(price);
                data.add(addrItem);
            }
        } catch (Exception e) {

        }
        return data;
    }
}