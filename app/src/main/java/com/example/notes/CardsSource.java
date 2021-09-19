package com.example.notes;

import android.os.Parcel;
import android.os.Parcelable;

public interface CardsSource extends Parcelable {
    NoteStructure getCardData(int position);
    int size();
    void deletePosition(int position);
    void addCardData(int buttonPosition, NoteStructure noteStructure);
    void isCheck(int position);

    Parcelable.Creator<CardsSource> CREATOR = new Parcelable.Creator<CardsSource>() {

        @Override
        public CardsSource createFromParcel(Parcel in) {
            return new CardsSource() {
                @Override
                public NoteStructure getCardData(int position) {
                    return null;
                }

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public void deletePosition(int position) {

                }

                @Override
                public void addCardData(int buttonPosition, NoteStructure noteStructure) {

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
            };
        }

        @Override
        public CardsSource[] newArray(int size) {
            return new CardsSource[size];
        }
    };
}
