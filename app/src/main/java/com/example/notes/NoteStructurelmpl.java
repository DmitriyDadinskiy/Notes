package com.example.notes;

import android.content.res.Resources;
import android.os.Parcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteStructurelmpl implements CardsSource {
    private List<NoteStructure> dataSource;
    private Resources resources;

    public NoteStructurelmpl(Resources resources){
        dataSource = new ArrayList<>();
        this.resources = this.resources;
    }


    @Override
    public CardsSource init(CardsSourceResponse cardsSourceResponse) {
                String [] title = resources.getStringArray(R.array.notes);
                String [] description = resources.getStringArray(R.array.text_notes);
        for (int i = 0; i < title.length; i++) {
            dataSource.add(new NoteStructure(title[i],  Calendar.getInstance().getTime(), description[i], false));
        }
        if (cardsSourceResponse != null){
            cardsSourceResponse.initialized(this);
        }


        return this;
    }

    @Override
    public NoteStructure getCardData(int position) {
        return dataSource.get(position);

    }

    @Override
    public int size() {
        return dataSource.size();
    }

    @Override
    public void deletePosition(int position) {
        dataSource.remove(position);

    }

    @Override
    public void addCardData(NoteStructure noteStructure) {
        dataSource.add(noteStructure);

    }

    @Override
    public void isCheck(int position) {
        dataSource.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

