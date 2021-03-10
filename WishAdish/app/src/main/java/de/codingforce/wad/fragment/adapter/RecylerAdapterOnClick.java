package de.codingforce.wad.fragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.codeingforce.wad.R;
import de.codingforce.wad.item.item_layout;

public class RecylerAdapterOnClick extends RecyclerView.Adapter<RecylerAdapterOnClick.RecylerViewHolder> {
    private ArrayList<item_layout> mlist;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener = listener;
    }

    public static class RecylerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mTextView1;
        public TextView mTextView2;

        public RecylerViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.item_1);
            mTextView2 = itemView.findViewById(R.id.item_2);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RecylerAdapterOnClick(ArrayList<item_layout> list)
    {
        mlist = list;
    }

    @NonNull
    @Override
    public RecylerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        RecylerViewHolder rvh = new RecylerViewHolder(v, mListener);

        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewHolder holder, int position) {
        item_layout currentItemlayout = mlist.get(position);

        holder.mTextView1.setText(currentItemlayout.getmText1());
        holder.mTextView2.setText(currentItemlayout.getmText2());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
