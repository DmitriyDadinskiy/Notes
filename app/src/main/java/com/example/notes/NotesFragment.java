package com.example.notes;

import static com.example.notes.R.layout.abc_list_menu_item_checkbox;
import static com.example.notes.R.layout.fragment_notes;
import static com.example.notes.R.layout.item;


import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class NotesFragment extends Fragment {

    private boolean isLands;
    public static final String CURRENT_NOTE = "currentNote";
    private NoteStructurelmpl currentPosition;
    private CardsSource data;
    private RecyclerView recyclerView;
    private int buttonPosition;
    private ItemAdapter adapter;
    private CheckBox checkBox;
    private TextView textView;




    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new NoteStructurelmpl(getResources()).init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(fragment_notes, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        checkBox = view.findViewById(R.id.check);
        textView = view.findViewById(R.id.title);
        init(recyclerView, data);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    public void init(RecyclerView recyclerView, CardsSource data) {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

       adapter = new ItemAdapter(data, this);
        recyclerView.setAdapter(adapter);

        adapter.setListener(position -> {
            currentPosition = new NoteStructurelmpl(getResources()).init();

            showText(currentPosition);
        });
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, (Parcelable) currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isLands = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentPosition = new NoteStructurelmpl(getResources()).init();//(getResources().getStringArray(R.array.notes)[0], 0);
        }
        if (isLands) {
            showLandText(currentPosition);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.contex_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
       buttonPosition = adapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.action_add:
                data.addCardData(buttonPosition, new NoteStructure(data
                        .getCardData(buttonPosition).getTitle(), data
                        .getCardData(buttonPosition).getDescription(), data
                        .getCardData(buttonPosition).getDate(), false));
                adapter.notifyItemChanged(buttonPosition);
                Toast.makeText(getContext(), "Добавили заметку", Toast
                        .LENGTH_LONG).show();
            case R.id.action_clear:
               data.deletePosition(buttonPosition);
                adapter.notifyItemRemoved(buttonPosition);
                Toast.makeText(getContext(), "заметка удалена", Toast
                        .LENGTH_LONG).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void showText(NoteStructurelmpl currentPosition) {
        if (isLands) {
            showLandText(currentPosition);
        } else {
            showPortText(currentPosition);
        }
    }

    private void showLandText(NoteStructurelmpl currentPosition) {
        TextFragment fragment = TextFragment.newInstance(currentPosition);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_land, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showPortText(NoteStructurelmpl currentPosition) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TextActivity2.class);
        intent.putExtra(TextFragment.ARG_NOTE, (Parcelable) currentPosition);
        startActivity(intent);
    }

}
