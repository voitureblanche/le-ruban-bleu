package fr.free.gelmir.lerubanbleu.service;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Article implements Comparable<Article>, Parcelable {

    private Long mNumber;
    private String mTitle;
    private String mLink;
    private String mDescription;
    private String mPubDate;

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setPubDate(String pubDate) {
        mPubDate = pubDate;
    }

    public void setContent() {

    }

    public void setLocalContent(String location) {

    }

    public void isComplete() {

    }

    public int compareTo(Article article) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int describeContents() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void writeToParcel(Parcel parcel, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
