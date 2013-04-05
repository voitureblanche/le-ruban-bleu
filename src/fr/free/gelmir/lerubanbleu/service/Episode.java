package fr.free.gelmir.lerubanbleu.service;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Episode implements Comparable<Episode>, Parcelable {

    private int mEpisodeNumber;
    private Uri mImageUri;

    public Episode() {
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri;
    }

    public int getEpisodeNb() {
        return mEpisodeNumber;
    }

    public void setEpisodeNb(int episodeNb) {
        mEpisodeNumber = episodeNb;
    }

    public int compareTo(Episode episode) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void writeToParcel(Parcel parcel, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
