package com.yyp.sidebar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yyp.sidebar.R;
import com.yyp.sidebar.model.User;

import java.util.ArrayList;

/**
 * Created by yyp on 2017/8/23.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {

    private ArrayList<User> data = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener listener;

    public MainAdapter(Context context){
        this.ctx = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(ctx).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder){
            ((ItemHolder) holder).avatar.setText(data.get(position).getName().substring(0, 1));
            ((ItemHolder) holder).name.setText(data.get(position).getName());

            //根据position获取联系人的首字母的char ascii值
            int section = getSectionForPosition(position);

            //如果当前位置等于该首字母的Char第一次出现的位置
            if(position == getPositionForSection(section)){
                ((ItemHolder) holder).item_sort_letter.setVisibility(View.VISIBLE);
                ((ItemHolder) holder).item_sort_letter.setText(data.get(position).getSortLetters());
            }else{
                ((ItemHolder) holder).item_sort_letter.setVisibility(View.GONE);
            }

            ((ItemHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onItemClick(holder.getAdapterPosition(),
                                data.get(holder.getAdapterPosition()).getName());
                    }
                }
            });
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

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 顺序比较，根据联系人的首字母的Char ascii值获取其第一次出现的位置
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 获取当前滑动到的联系人的首字母的char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return data.get(position).getSortLetters().charAt(0);
    }

    private static class ItemHolder extends RecyclerView.ViewHolder{
        LinearLayout item;
        TextView item_sort_letter;
        TextView avatar;
        TextView name;

        public ItemHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            item_sort_letter = itemView.findViewById(R.id.item_sort_letter);
            avatar = itemView.findViewById(R.id.item_avatar);
            name = itemView.findViewById(R.id.item_name);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position, String name);
    }
}
