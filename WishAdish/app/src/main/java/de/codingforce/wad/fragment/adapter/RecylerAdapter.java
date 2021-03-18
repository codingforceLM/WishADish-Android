package de.codingforce.wad.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.item.layouts.ItemLayout;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.RecylerViewHolder> {
    private ArrayList<ItemLayout> mlist;

    public static class RecylerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView1;
        public TextView mTextView2;

        public RecylerViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.item_1);
            mTextView2 = itemView.findViewById(R.id.item_2);

        }
    }

    public RecylerAdapter(ArrayList<ItemLayout> list)
    {
        mlist = list;
    }

    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        RecylerViewHolder rvh = new RecylerViewHolder(v);

        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder holder, int position) {
        ItemLayout currentItemlayout = mlist.get(position);

        holder.mTextView1.setText(currentItemlayout.getmText1());
        holder.mTextView2.setText(currentItemlayout.getmText2());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
