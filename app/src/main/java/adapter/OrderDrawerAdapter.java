package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.example.cherrycha.material_design.R;

import java.util.Collections;
import java.util.List;

import model.AddressDrawerItem;

public class OrderDrawerAdapter extends RecyclerView.Adapter<OrderDrawerAdapter.OrderViewHolder> implements View.OnClickListener {
    private List<AddressDrawerItem> data= Collections.emptyList();
    private LayoutInflater inflater;
    final Context context;
    private View rootView;

    private OnItemClickListener mOnItemClickListener = null;



    public OrderDrawerAdapter(Context context, List<AddressDrawerItem> datas) {
        this.data = datas;
        inflater = LayoutInflater.from(context);

        this.context = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.order_item, null);
        OrderViewHolder holder = new OrderViewHolder(view);
//        holder.setOnclickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
//        holder.mTextView.setText(datas[position]);
        //将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        AddressDrawerItem current =data.get(position);
        holder.itemView.setTag(position);
        holder.mAddrTextView.setText(current.getAddress());
        holder.mPhoneTextView.setText(current.getPhone_no());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView mAddrTextView;
        TextView mPhoneTextView;

        public OrderViewHolder(View itemView) {
            super(itemView);
            mAddrTextView = (TextView) itemView.findViewById(R.id.txt_order);
            mPhoneTextView = (TextView) itemView.findViewById(R.id.txt_value);
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
        void onItemClick(View view, int position);
    }



}