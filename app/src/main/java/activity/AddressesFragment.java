package activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
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
import adapter.NavigationDrawerAdapter;
import model.AddressDrawerItem;
import model.NavDrawerItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class AddressesFragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerView;
    Bundle bundle = new Bundle();
    String token;
    private static int flag = 0;
    private static JSONArray addresses = null;
    private FragmentDrawer drawerFragment;
    private FragmentDrawer.FragmentDrawerListener drawerListener;

    public AddressesFragment() {
        // Required empty public constructor

    }

    public void setDrawerListener(FragmentDrawer.FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        addresses = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

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
        if (addresses.length() > 0) {
            rootView = inflater.inflate(R.layout.fragment_address, container, false);

            recyclerView = rootView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//这里用线性显示 类似于listview
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流

            recyclerView.setAdapter(new AddressDrawerAdapter(getActivity(), getData(addresses)));
            recyclerView.addOnItemTouchListener(new FragmentDrawer.RecyclerTouchListener(getActivity(), recyclerView, new FragmentDrawer.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    DialogFragment fragment = new EditAddressFragment();
                    try {
                        bundle.putString("address", addresses.getJSONObject(position).getString("addressDescription"));
                        bundle.putString("phone_no", addresses.getJSONObject(position).getString("phoneNumber"));
                        Integer id=(addresses.getJSONObject(position).getInt("id"));
                        bundle.putString("id", id.toString());
                    }catch(Exception e){

                    }
                    fragment.setArguments(bundle);

                    fragment.show(getFragmentManager(), "EditNameDialog");
                }

                @Override
                public void onLongClick(View view, int position) {
                    DialogFragment fragment = new DeleteAddressFragment();
                    try {
                        Integer id=(addresses.getJSONObject(position).getInt("id"));
                        bundle.putString("id", id.toString());
                    }catch(Exception e){

                    }
                    fragment.setArguments(bundle);
                    fragment.show(getFragmentManager(), "EditNameDialog");
                }
            }));
        } else {
            rootView = inflater.inflate(R.layout.fragment_empty_address, container, false);
        }
        // Inflate the layout for this fragment
        rootView.findViewById(R.id.btn_add_address).setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void HttpPost() {
        String url = "http://120.79.132.224:9090/shopkeeper/address/list";
        token = getArguments().getString("token");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).header("token", token).build(); // 请求

        client.newCall(request).enqueue(new Callback() { // 回调

            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String result = new String(response.body().string());
                    JSONObject responseobj = new JSONObject(result);

                    if (responseobj.getString("resultCode").equals("0000")) {
                        addresses = responseobj.getJSONArray("addressList");//通过user字段获取其所包含的JSONObject对象
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


    @Override
    public void onClick(View view) {
        Fragment fragment = null;
        System.out.println("clicked");
        switch (view.getId()) {
            case R.id.btn_add_address:
                System.out.println("button");
                AddAddressFragment itemAddedDialog = new AddAddressFragment();
                itemAddedDialog.setArguments(bundle);
                itemAddedDialog.show(getFragmentManager(), "EditNameDialog");
                fragment = new AddressesFragment();
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

    public static List<AddressDrawerItem> getData(JSONArray addresses) {
        List<AddressDrawerItem> data = new ArrayList<>();

        try {
            for (int i = 0; i < addresses.length(); i++) {
                AddressDrawerItem addrItem = new AddressDrawerItem();
                String address = addresses.getJSONObject(i).getString("addressDescription");
                String phone_no = addresses.getJSONObject(i).getString("phoneNumber");
                Integer type=Integer.valueOf(addresses.getJSONObject(i).getString("type"));
                addrItem.setIcon_name(type==1?"ic_action_address":"ic_action_location_off");
                addrItem.setAddress(address);
                addrItem.setPhone_no(phone_no);
                data.add(addrItem);
            }
        } catch (Exception e) {

        }
        return data;
    }
}