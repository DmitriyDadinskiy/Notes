package com.example.notes;

import static com.example.notes.R.layout.fragment_notes;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class NotesFragment extends Fragment {

    private boolean isLands;
    public static final String CURRENT_NOTE = "currentNote";
    private NoteStructure currentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    public void init(View view) {
        LinearLayout layoutView = (LinearLayout) view;
        String[] notes = getResources().getStringArray(R.array.notes);
        for (int i = 0; i < notes.length; i++) {
            String note = notes[i];
            TextView notesTV = new TextView(getContext());
            notesTV.setText(note);
            notesTV.setTextSize(32);
            layoutView.addView(notesTV);
            final int ac = i;
            notesTV.setOnClickListener(v -> {
                currentPosition = new NoteStructure(getResources()
                        .getStringArray(R.array.notes)[ac], ac);
                showText(currentPosition);
            });
        }
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
