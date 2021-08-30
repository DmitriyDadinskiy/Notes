package com.example.notes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

public class NoteStructure implements Parcelable {

    private int description;
    private String title;
    private int date;
    private boolean check;


    public NoteStructure(String title, int description) {
        this.title = title;
        this.description = description;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected NoteStructure(Parcel in) {
        title = in.readString();
        description = in.readInt();
        date = in.readInt();
        check = in.readBoolean();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(description);
        dest.writeInt(date);
        dest.writeBoolean(check);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteStructure> CREATOR = new Creator<NoteStructure>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
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
