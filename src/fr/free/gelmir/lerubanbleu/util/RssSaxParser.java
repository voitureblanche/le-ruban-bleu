package fr.free.gelmir.lerubanbleu.util;

import fr.free.gelmir.lerubanbleu.service.Episode;

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

    public List<Episode> getLatestArticles() {

        // allocate an handler and get latest episodes
        RssSaxHandler rssSaxHandler = new RssSaxHandler();
        List<Episode> episodes = rssSaxHandler.getLatestArticles("http://www.lerubanbleu.com/rss.xml");

        return episodes;
    }

}
