package com.example.notes;

import static android.content.Context.MODE_PRIVATE;
import static android.preference.PreferenceManager.*;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private String[] dataSource;
    private static OnItemClickListener listener;

    public ItemAdapter(String[] dataSource) {
        this.dataSource = dataSource;
    }

    public void setListener(@Nullable OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.getTextView().setText(dataSource[position]);
    }

    @Override
    public int getItemCount() {
        return dataSource.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(ItemViewHolder.this.getAdapterPosition());
                }
            });
            checkBox = itemView.findViewById(R.id.check);
            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            });
            if (checkBox.isChecked()) {
                SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(checkBox.getContext()); //состояние чекбокса не схранено, нужно подумать как сохранить
                setting.edit().putBoolean("checked", true).apply();
            }
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
