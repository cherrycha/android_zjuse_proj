package activity;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.zip.Inflater;

import activity.EditProfileFragment;
import activity.ShoppingFragment;
import cn.example.cherrycha.material_design.R;


public class ItemAddedFragment extends DialogFragment implements View.OnClickListener {


    public ItemAddedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_added, container, false);


        final SpannableStringBuilder style1 = new SpannableStringBuilder();
        final SpannableStringBuilder style2 = new SpannableStringBuilder();

        //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                android.support.v4.app.Fragment fragment=null;
                switch (view.getId()){
                    case R.id.id_txt_continue_shopping:
                        dismiss();
                    case R.id.id_txt_go_to_cart:
                        fragment=new ShoppingFragment();

                }
                if (fragment != null) {
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container_body, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    dismiss();
                }

            }
        };

        TextView continue_shopping = view.findViewById(R.id.id_txt_continue_shopping);


        String txt_go_to_cart=getActivity().getString(R.string.txt_go_to_cart);
        String txt_continue_shopping=getActivity().getString(R.string.txt_continue_shopping);
        style1.append(txt_continue_shopping);

        style1.setSpan(clickableSpan, 0, txt_continue_shopping.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style1.setSpan(new StyleSpan(Typeface.NORMAL), 0, txt_continue_shopping.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        continue_shopping.setMovementMethod(LinkMovementMethod.getInstance());
        continue_shopping.setText(style1);

        TextView go_to_cart = view.findViewById(R.id.id_txt_go_to_cart);
        style2.append(txt_go_to_cart);

        style2.setSpan(clickableSpan, 0, txt_go_to_cart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style2.setSpan(new StyleSpan(Typeface.NORMAL), 0,  txt_go_to_cart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        go_to_cart.setMovementMethod(LinkMovementMethod.getInstance());
        go_to_cart.setText(style2);

        return view;

    }

    @Override
    public void onClick(View view) {

    }
}
