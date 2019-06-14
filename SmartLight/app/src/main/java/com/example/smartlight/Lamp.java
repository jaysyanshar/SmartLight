package com.example.smartlight;

import android.os.Parcel;
import android.os.Parcelable;

public class Lamp implements Parcelable {

    private String name;
    private String url;
    private int type;
    private boolean statusOn;

    public static final int DEFAULT = 0;
    public static final int DESK = 1;
    public static final int FLOOR = 2;
    public static final int CEILING = 3;

    public Lamp(String name, String url, int type, boolean statusOn) {
        setName(name);
        setUrl(url);
        setType(type);
        setStatusOn(statusOn);
    }

    protected Lamp(Parcel in) {
        name = in.readString();
        url = in.readString();
        type = in.readInt();
        statusOn = in.readByte() != 0;
    }

    public static final Creator<Lamp> CREATOR = new Creator<Lamp>() {
        @Override
        public Lamp createFromParcel(Parcel in) {
            return new Lamp(in);
        }

        @Override
        public Lamp[] newArray(int size) {
            return new Lamp[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setType(int type) {
        if (type == DESK || type == FLOOR || type == CEILING) {
            this.type = type;
        } else {
            this.type = DEFAULT;
        }
    }

    public int getType() {
        return type;
    }

    public void setStatusOn(boolean statusOn) {
        this.statusOn = statusOn;
    }

    public boolean isStatusOn() {
        return statusOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(type);
        dest.writeByte((byte) (statusOn ? 1 : 0));
    }
}
