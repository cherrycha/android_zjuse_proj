package adapter;

import android.content.Context;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.example.cherrycha.material_design.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import model.AddressDrawerItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddressDrawerAdapter extends RecyclerView.Adapter<AddressDrawerAdapter.AddressViewHolder> implements View.OnClickListener {
    private List<AddressDrawerItem> data= Collections.emptyList();
    private LayoutInflater inflater;
    final Context context;
    private View rootView;

    private OnItemClickListener mOnItemClickListener = null;



    public AddressDrawerAdapter(Context context, List<AddressDrawerItem> datas) {
        this.data = datas;
        inflater = LayoutInflater.from(context);

        this.context = context;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.address_item, null);
        AddressViewHolder  holder = new AddressViewHolder(view);
        holder.mButtonDelete.setOnClickListener(this);
        holder.mButtonEdit.setOnClickListener(this);
//        holder.setOnclickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
//        holder.mTextView.setText(datas[position]);
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        model.AddressDrawerItem current =data.get(position);
        holder.itemView.setTag(position);
        holder.mAddrTextView.setText(current.getAddress());
        holder.mPhoneTextView.setText(current.getPhone_no());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView mAddrTextView;
        TextView mPhoneTextView;
        ImageButton mButtonEdit;
        ImageButton mButtonDelete;

        public AddressViewHolder(View itemView) {
            super(itemView);
            mAddrTextView = (TextView) itemView.findViewById(R.id.txt_address);
            mPhoneTextView = (TextView) itemView.findViewById(R.id.txt_phone);
            mButtonEdit=itemView.findViewById(R.id.btn_edit_addr);
            mButtonDelete=itemView.findViewById(R.id.btn_delete_addr);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }


    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }



}