package com.example.notes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.appcompat.widget.AppCompatEditText;

public class NoteStructure implements Parcelable {

    private int description;
    private String title;


    public NoteStructure(String title, int description) {
        this.title = title;
        this.description = description;
    }

    protected NoteStructure(Parcel in) {
        title = in.readString();
        description = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(description);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getTitle() {
        return title;
    }

    public int getDescription() {
        return description;
    }
}
