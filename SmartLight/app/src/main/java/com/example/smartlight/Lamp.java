package com.example.smartlight;

import android.os.Parcel;
import android.os.Parcelable;

public class Lamp implements Parcelable {

    private int id;
    private String name;
    private String url;
    private int type;
    private int brightness;
    private boolean statusOn;

    // type value
    public static final int DEFAULT = 0;
    public static final int DESK = 1;
    public static final int FLOOR = 2;
    public static final int CEILING = 3;

    public Lamp(int id, String name, String url, int type, int brightness, boolean statusOn) {
        setId(id);
        setName(name);
        setUrl(url);
        setType(type);
        setBrightness(brightness);
        setStatusOn(statusOn);
    }

    protected Lamp(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setUrl(in.readString());
        setType(in.readInt());
        setBrightness(in.readInt());
        setStatusOn(in.readByte() != 0);
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

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        } else {
            this.id = 0;
        }
    }

    public int getId() {
        return id;
    }

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

    public void setBrightness(int brightness) {
        if (brightness >= 0 && brightness <= 255) {
            this.brightness = brightness;
        } else {
            this.brightness = 100;
        }
    }

    public int getBrightness() {
        return brightness;
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
        dest.writeInt(type);
        dest.writeInt(brightness);
        dest.writeByte((byte) (statusOn ? 1 : 0));
    }
}
