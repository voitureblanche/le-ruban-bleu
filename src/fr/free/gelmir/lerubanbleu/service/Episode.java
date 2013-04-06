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
public class Episode implements Parcelable {

    private int mEpisodeNumber;
    private Uri mImageUri;

    // Basic constructor for non-parcel object creation
    public Episode() {

    }

    // Constructor used when reconstructing object from a parcel
    public Episode(Parcel parcel) {
        mEpisodeNumber = parcel.readInt();
        mImageUri = Uri.parse(parcel.readString());
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

    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>()
    {
        public Episode createFromParcel(Parcel parcel) {
            return new Episode(parcel);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(mEpisodeNumber);
        parcel.writeString(mImageUri.toString());
    }
}
