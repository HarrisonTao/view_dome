package com.dykj.module.util.PhotoGallery;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小智
 * on 2017/12/25
 * 描述：
 */

public class PhotoGalleryData implements Parcelable {
    public String name;
    public int index;
    public String url;
    public String[] images;
    public List<String> imageList;
    public boolean isLocal;
    public static final Creator<PhotoGalleryData> CREATOR = new Creator() {
        public PhotoGalleryData createFromParcel(Parcel in) {
            return new PhotoGalleryData(in);
        }

        public PhotoGalleryData[] newArray(int size) {
            return new PhotoGalleryData[size];
        }
    };

    public PhotoGalleryData(int index, String... images) {
        this.index = index;
        this.images = images;
        this.isLocal = false;
    }

    public PhotoGalleryData(boolean isLocal, int index, String... images) {
        this.index = index;
        this.images = images;
        this.isLocal = isLocal;
    }

    public PhotoGalleryData(int index, List<String> list) {
        this.imageList = new ArrayList();
        this.index = index;
        this.imageList.addAll(list);
        this.isLocal = false;
    }

    public PhotoGalleryData(boolean isLocal, int index, List<String> list) {
        this.imageList = new ArrayList();
        this.index = index;
        this.imageList.addAll(list);
        this.isLocal = isLocal;
    }

    public PhotoGalleryData() {
    }

    private PhotoGalleryData(Parcel in) {
        this.name = in.readString();
        this.index = in.readInt();
        this.url = in.readString();
        this.images = in.createStringArray();
        this.imageList = in.createStringArrayList();
        this.isLocal = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.index);
        dest.writeString(this.url);
        dest.writeStringArray(this.images);
        dest.writeStringList(this.imageList);
        dest.writeByte((byte) (this.isLocal ? 1 : 0));
    }
}
