package activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import cn.example.cherrycha.material_design.R;


public class ShoppingCartFragment extends Fragment implements View.OnClickListener {

    public ShoppingCartFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        boolean isChecked= false;
        CheckBox cbx = (CheckBox) rootView.findViewById(R.id.checkbox_apple);
        cbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //do something
                }else{
                    //do something else
                }
            }
        });
        rootView.findViewById(R.id.check_button).setOnClickListener(this);
        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        Fragment fragment=null;
        switch (view.getId()){
            case R.id.check_button:
                CheckOutFragment checkOutDialog = new CheckOutFragment();
                checkOutDialog.show(getFragmentManager(), "EditNameDialog");
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
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}