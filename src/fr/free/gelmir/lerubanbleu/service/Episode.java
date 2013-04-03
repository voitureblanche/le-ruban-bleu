package fr.free.gelmir.lerubanbleu.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Episode implements Comparable<Episode>, Parcelable {

    private int mEpisodeId;
    private URI mImageUri;

    public Episode(int episodeId, URI imageURI) {
        mEpisodeId = episodeId;
        mImageUri = imageURI;
    }

    public URI getImageUri() {
        return mImageUri;
    };

    public int getEpisodeId() {
        return mEpisodeId;
    };

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
