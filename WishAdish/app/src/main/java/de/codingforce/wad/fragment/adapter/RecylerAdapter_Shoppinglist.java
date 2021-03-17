package de.codingforce.wad.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.item.Item_layout;
import de.codingforce.wad.item.Item_layout_ingredients;

public class RecylerAdapter_Shoppinglist extends RecyclerView.Adapter<RecylerAdapter_Shoppinglist.RecylerViewHolder> {
    private ArrayList<Item_layout_ingredients> mlist;

    public static class RecylerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView1;
        public TextView mTextView2;
        public CheckBox checkBox;

        public RecylerViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.item_1_ingredients);
            mTextView2 = itemView.findViewById(R.id.item_2_ingredients);
            checkBox = itemView.findViewById(R.id.ingredients_checkbox);

        }
    }

    public RecylerAdapter_Shoppinglist(ArrayList<Item_layout_ingredients> list)
    {
        mlist = list;
    }

    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_shoppinglist, parent, false);

        RecylerViewHolder rvh = new RecylerViewHolder(v);

        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder holder, int position) {
        Item_layout_ingredients currentItemlayout = mlist.get(position);

        holder.mTextView1.setText(currentItemlayout.getmText1());
        holder.mTextView2.setText(currentItemlayout.getmText2());
        holder.checkBox.setChecked(currentItemlayout.isDone());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
