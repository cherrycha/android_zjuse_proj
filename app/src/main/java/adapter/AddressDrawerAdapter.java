package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cherrycha.material_design.R;

import java.util.ArrayList;
import java.util.List;

import model.AddressDrawerItem;

public class AddressDrawerAdapter extends RecyclerView.Adapter<AddressDrawerAdapter.AddressViewHolder> {
    private List<AddressDrawerItem> data;
    private Context context;

    public AddressDrawerAdapter(Context context, ArrayList<AddressDrawerItem> datas) {
        this.data = datas;
        this.context = context;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=View.inflate(context, R.layout.address_item, null);
        AddressViewHolder  holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        model.AddressDrawerItem current =data.get(position);
        holder.mTextView.setText("test location");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        public AddressViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.txt_address);
        }
    }
}