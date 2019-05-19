package org.techtown.db;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {

    private ArrayList<PersonalData> mList = null;
    private Activity context = null;


    public UsersAdapter(Activity context, ArrayList<PersonalData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView ID;
        protected TextView name;
        protected TextView HP;


        public CustomViewHolder(View view) {
            super(view);
            this.ID = (TextView) view.findViewById(R.id.textView_list_ID);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
            this.HP = (TextView) view.findViewById(R.id.textView_list_HP);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.ID.setText(mList.get(position).getMember_ID());
        viewholder.name.setText(mList.get(position).getMember_name());
        viewholder.HP.setText(mList.get(position).getMember_HP());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}