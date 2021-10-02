package com.example.notes;


import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private CardsSource dataSource;
    private static OnItemClickListener listener;
    private final Fragment fragment;
    private static int menuPosition;


    public ItemAdapter(CardsSource dataSource, Fragment fragment) {
        this.dataSource = dataSource;
        this.fragment = fragment;
    }


//    public void setDataSource(CardsSource dataSource){
//        this.dataSource = dataSource;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new ItemViewHolder(view, fragment);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder viewHolder, int position) {
        viewHolder.setData(dataSource.getCardData(position));

    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void setListener(@Nullable OnItemClickListener listener) {
        this.listener = listener;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public int getMenuPosition() {
        return menuPosition;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageButton ImageButton;
        private TextView textView;
        private CheckBox checkBox;
        private Fragment fragment;
        private EditText dateText;


        public ItemViewHolder(@NonNull View itemView, Fragment fragment) {
            super(itemView);
            dateText = itemView.findViewById(R.id.date);
            textView = itemView.findViewById(R.id.title);
            this.fragment = fragment;
            textView.setOnClickListener(v -> listener.onItemClick(ItemViewHolder.this.getAdapterPosition()));
            checkBox = itemView.findViewById(R.id.check);
            checkBox.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    itemClicked(adapterPosition);
                }
                if (checkBox.isChecked()) {
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    textView.setPaintFlags(textView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            });

            registerContextMenu(itemView);

            ImageButton = itemView.findViewById(R.id.imageButton);
//            ImageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        listener.onItemClick(ItemViewHolder.this.getAdapterPosition());
//                    }
//                }
//            });
            ImageButton.setOnClickListener(new View.OnClickListener() {//показать меню
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(20, 20);
                }
            });
        }

        private void itemClicked(int adapterPosition) {
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuPosition = getLayoutPosition();
                        //menuPosition = getAdapterPosition();
                    }
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public void setData(NoteStructure cardData) {
            textView.setText(cardData.getTitle());
            checkBox.setChecked(cardData.isCheck());
            dateText.setText(new SimpleDateFormat("dd-MM-yy").format(cardData.getDate()));
        }
    }
}



