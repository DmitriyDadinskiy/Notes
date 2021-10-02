package com.example.notes;


import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;


import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class NoteStructure implements Parcelable, CardsSource {
    @Exclude
    private String id;
    private Date date;
    private String title;
    private String description;
    private boolean check;

    public NoteStructure(String title, Date date, String description, boolean check) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.check = check;
    }

    public NoteStructure() {
    }



    public void setId(String id) {
        this.id = id;
    }


    protected NoteStructure(Parcel in) {
        title = in.readString();
        description = in.readString();
        check = in.readByte() != 0;
        date = new Date(in.readLong());

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeLong(date.getTime());
        dest.writeString(title);
    }

    public static final Creator<NoteStructure> CREATOR = new Creator<NoteStructure>() {
        @Override
        public NoteStructure createFromParcel(Parcel in) {
            return new NoteStructure(in);
        }

        @Override
        public NoteStructure[] newArray(int size) {
            return new NoteStructure[size];
        }
    };

    public String getId() {
        return id;
    }

    public  String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public  Date getDate() {
        return date;
    }

    public boolean isCheck() {
        return check;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public CardsSource init(CardsSourceResponse cardsSourceResponse) {
        return null;
    }

    @Override
    public NoteStructure getCardData(int position) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public void deletePosition(int position) {

    }

    @Override
    public void addCardData(NoteStructure noteStructure) {

    }

    @Override
    public void isCheck(int position) {

    }
}
