package com.example.notes;




import android.app.assist.AssistStructure;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;


import java.util.Date;

public class NoteStructure implements Parcelable {

    private static String title;
    private static Date date;
    private String description;
    private boolean check;


    public NoteStructure(String title, String description, Date date, boolean check) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.check = check;
    }




    protected NoteStructure(Parcel in) {
        description = in.readString();
        check = in.readByte() != 0;
        date = new Date (in.readLong());
        title = in.readString();
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

    public static String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public static Date getDate() {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeLong(date.getTime());
        dest.writeString(title);
    }
}
