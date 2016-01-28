package com.justplay.android.mediagrid.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchViewModel implements Parcelable {

    private String queryString;
    private boolean isExpanded;
    private boolean isFocused;

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public String getQueryString() {
        return queryString;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public SearchViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.queryString);
        dest.writeByte(isExpanded ? (byte) 1 : (byte) 0);
        dest.writeByte(isFocused ? (byte) 1 : (byte) 0);
    }

    protected SearchViewModel(Parcel in) {
        this.queryString = in.readString();
        this.isExpanded = in.readByte() != 0;
        this.isFocused = in.readByte() != 0;
    }

    public static final Creator<SearchViewModel> CREATOR = new Creator<SearchViewModel>() {
        public SearchViewModel createFromParcel(Parcel source) {
            return new SearchViewModel(source);
        }

        public SearchViewModel[] newArray(int size) {
            return new SearchViewModel[size];
        }
    };
}
