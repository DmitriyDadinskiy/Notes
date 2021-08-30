package com.example.notes;

import static com.example.notes.R.layout.fragment_notes;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class NotesFragment extends Fragment {

    private boolean isLands;
    public static final String CURRENT_NOTE = "currentNote";
    private NoteStructure currentPosition;
    public CheckBox checkBoxPosition;


    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(fragment_notes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_lines);
        String[] notes = getResources().getStringArray(R.array.notes);
        init(recyclerView, notes);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    public void init(RecyclerView recyclerView, String[] notes) {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter adapter = new ItemAdapter(notes);
        recyclerView.setAdapter(adapter);

        adapter.setListener(position -> {
            currentPosition = new NoteStructure(getResources()
                    .getStringArray(R.array.notes)[position], position);
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
            currentPosition = new NoteStructure(getResources().getStringArray(R.array.notes)[0], 0);
        }
        if (isLands) {
            showLandText(currentPosition);
        }
    }

    private void showText(NoteStructure currentPosition) {
        if (isLands) {
            showLandText(currentPosition);
        } else {
            showPortText(currentPosition);
        }
    }

    private void showLandText(NoteStructure currentPosition) {
        TextFragment fragment = TextFragment.newInstance(currentPosition);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.text_land, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void showPortText(NoteStructure currentPosition) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), TextActivity2.class);
        intent.putExtra(TextFragment.ARG_NOTE, currentPosition);
        startActivity(intent);
    }
}
