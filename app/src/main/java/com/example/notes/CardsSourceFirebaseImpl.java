package com.example.notes;

import android.os.Parcel;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

public class CardsSourceFirebaseImpl implements CardsSource {
    private static final String NOTES_COLLECTION = "cards";
    private static final String TAG ="[CardsSourceFirebaseImpl]";
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collection = store.collection(NOTES_COLLECTION);
    private List<NoteStructure> noteStructures = new ArrayList<>();



    @Override
    public CardsSource init(final CardsSourceResponse cardsSourceResponse) {
        collection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        noteStructures = new ArrayList<>();
                        for (QueryDocumentSnapshot document:task.getResult()) {
                            NoteStructure data = document.toObject(NoteStructure.class);
                            data.setId(document.getId());
                            noteStructures.add(data);
                        }
                        Log.d(TAG, "получено " + noteStructures.size());

                    }else {
                        Log.d(TAG, "не получено" + task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "ничего не происходит", e));
        return this;
    }
    @Override
    public NoteStructure getCardData(int position) {
        return noteStructures.get(position);
    }

    @Override
    public int size() {
        return noteStructures.size();
    }

    @Override
    public void deletePosition(int position) {
        collection.document(noteStructures.get(position).getId()).delete();
        noteStructures.remove(position);

    }

    @Override
    public void addCardData(final NoteStructure noteStructure) {
        collection.document(noteStructure.getId()).set(noteStructure);
        noteStructures.add(noteStructure);

    }

    @Override
    public void isCheck(int position) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
