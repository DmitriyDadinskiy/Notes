package com.example.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TextFragment extends Fragment {


    public static final String ARG_NOTE = "note";
    private NoteStructure note;

    public TextFragment() {
    }

    public static TextFragment newInstance(NoteStructure note) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initText(view);
    }

    private void initText(@NonNull View view) {
        AppCompatEditText TextFragment = view.findViewById(R.id.text_fragment);
        String[] textNotes = getResources().getStringArray(R.array.text_notes);
        TextFragment.setText(textNotes[note.getDescription()]);


        TextView noteNameView = view.findViewById(R.id.text_in_fragment);
        noteNameView.setText(note.getTitle());
    }
}