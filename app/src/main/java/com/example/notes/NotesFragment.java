package com.example.notes;

import static com.example.notes.R.layout.fragment_notes;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;


public class NotesFragment extends Fragment {

    private boolean isLands;
    public static final String CURRENT_NOTE = "currentNote";
    private CardsSource currentPosition;
    private CardsSource data;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private CheckBox checkBox;
    private TextView textView;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new CardsSourceFirebaseImpl();
        data.init(data -> adapter.notifyDataSetChanged());

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
        init();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    public void init() {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemAdapter(data,this);
        recyclerView.setAdapter(adapter);

        adapter.setListener(position -> {
            currentPosition = new CardsSourceFirebaseImpl();

            showText(currentPosition);
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentPosition);
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
            currentPosition =  new CardsSourceFirebaseImpl().init(cardsSource -> adapter.notifyDataSetChanged());
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
        int buttonPosition = adapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.action_add:
                NoteStructure noteStructure = new NoteStructure("Title", "dd.mm.eeee", "description", false);
                noteStructure.setId(UUID.randomUUID().toString());
                data.addCardData(noteStructure);
                adapter.notifyItemChanged(data.size() - 1);
                Toast.makeText(getContext(), "Добавили заметку", Toast
                        .LENGTH_LONG).show();
            case R.id.action_clear:
//                data.deletePosition(buttonPosition);
//                adapter.notifyItemRemoved(buttonPosition);
//                Toast.makeText(getContext(), "заметка удалена", Toast
//                        .LENGTH_LONG).show();
                return true;
        }
        return super.onContextItemSelected(item);
    }


    private void showText(CardsSource currentPosition) {
        if (isLands) {
            showLandText(currentPosition);
        } else {
            showPortText(currentPosition);
        }
    }

    private void showLandText(CardsSource currentPosition) {
        TextFragment fragment = TextFragment.newInstance(currentPosition);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_land, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showPortText(CardsSource currentPosition) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TextActivity2.class);
        intent.putExtra(TextFragment.ARG_NOTE, currentPosition);
        startActivity(intent);
    }

}
