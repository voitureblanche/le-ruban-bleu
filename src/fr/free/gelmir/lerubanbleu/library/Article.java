package fr.free.gelmir.lerubanbleu.library;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 22:40
 * To change this template use File | Settings | File Templates.
 */
public class Article implements Comparable<Article> {

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

    @Override
    public int compareTo(Article article) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
