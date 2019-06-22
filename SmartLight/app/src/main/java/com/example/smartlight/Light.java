package com.example.smartlight;

import android.os.Parcel;
import android.os.Parcelable;

public class Light implements Parcelable {

    private int id;
    private String name;
    private String url;
    private int type;
    private int brightness;
    private boolean statusOn;

    // type value
    private static final int DEFAULT = 0;
    static final int DESK = 1;
    static final int FLOOR = 2;
    static final int CEILING = 3;

    Light(int id, String name, String url, int type, int brightness, boolean statusOn) {
        setId(id);
        setName(name);
        setUrl(url);
        setType(type);
        setBrightness(brightness);
        setStatusOn(statusOn);
    }

    private Light(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setUrl(in.readString());
        setType(in.readInt());
        setBrightness(in.readInt());
        setStatusOn(in.readByte() != 0);
    }

    public static final Creator<Light> CREATOR = new Creator<Light>() {
        @Override
        public Light createFromParcel(Parcel in) {
            return new Light(in);
        }

        @Override
        public Light[] newArray(int size) {
            return new Light[size];
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

    void setUrl(String url) {
        this.url = url;
    }

    String getUrl() {
        return url;
    }

    private void setType(int type) {
        if (type == DESK || type == FLOOR || type == CEILING) {
            this.type = type;
        } else {
            this.type = DEFAULT;
        }
    }

    int getType() {
        return type;
    }

    void setBrightness(int brightness) {
        if (brightness >= 0 && brightness <= 255) {
            this.brightness = brightness;
        } else {
            this.brightness = 100;
        }
    }

    int getBrightness() {
        return brightness;
    }

    void setStatusOn(boolean statusOn) {
        this.statusOn = statusOn;
    }

    boolean isStatusOn() {
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
