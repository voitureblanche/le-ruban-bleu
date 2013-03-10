package fr.free.gelmir.lerubanbleu.feed;

import fr.free.gelmir.lerubanbleu.library.Article;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class RssSaxParser
{

    public List<Article> getLatestArticles() {

        // allocate an handler and get latest articles
        RssSaxHandler rssSaxHandler = new RssSaxHandler();
        List<Article> articles = rssSaxHandler.getLatestArticles("http://www.lerubanbleu.com/rss.xml");

        return articles;
    }

}
