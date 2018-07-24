package activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cherrycha.material_design.R;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    public EditProfileFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        rootView.findViewById(R.id.save_button).setOnClickListener(this);
        String[] infos = getActivity().getResources().getStringArray(R.array.info_labels);
        if (getArguments() != null) {
            for (int i = 0; i < infos.length; i++) {
                TextView tv=null;
                switch (i) {
                    case 0:
                        tv = rootView.findViewById(R.id.txt_name);
                        break;
                    case 1:
                        tv = rootView.findViewById(R.id.txt_phone);
                        break;
                    case 2:
                        tv = rootView.findViewById(R.id.txt_email);
                        break;
                    case 3:
                        tv = rootView.findViewById(R.id.txt_nickname);
                        break;
                    case 4:
                        tv= rootView.findViewById(R.id.txt_password);
                        break;
                }
                try{
                    tv.setText(getArguments().getString(infos[i]));
                }catch (NullPointerException e)
                {

                }
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
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
    public void onDetach() {
        super.onDetach();
    }
}