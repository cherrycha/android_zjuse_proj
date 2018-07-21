package activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;

import cn.example.cherrycha.material_design.R;


public class ItemInfoFragment extends Fragment implements View.OnClickListener {



    public ItemInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_item_info, container, false);

        Button btn_add_to_cart=rootView.findViewById(R.id.btn_add_to_cart);
        btn_add_to_cart.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_to_cart:
                ItemAddedFragment itemAddedDialog = new ItemAddedFragment();
                itemAddedDialog.show(getFragmentManager(), "EditNameDialog");
                break;
        }
    }
}
