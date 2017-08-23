package com.yyp.sidebar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyp.sidebar.R;
import com.yyp.sidebar.model.User;

import java.util.ArrayList;

/**
 * Created by yyp on 2017/8/23.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<User> data = new ArrayList<>();
    private Context ctx;

    public MainAdapter(Context context){
        this.ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(ctx).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder){
            ((ItemHolder) holder).avatar.setText(data.get(position).getAvatar());
            ((ItemHolder) holder).name.setText(data.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 刷新数据
     * @param data
     */
    public void refresh(ArrayList<User> data){
        this.data = data;
        notifyDataSetChanged();
    }

    private static class ItemHolder extends RecyclerView.ViewHolder{
        TextView avatar;
        TextView name;

        public ItemHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.item_avatar);
            name = itemView.findViewById(R.id.item_name);
        }
    }
}
