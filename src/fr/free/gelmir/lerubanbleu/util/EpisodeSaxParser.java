package fr.free.gelmir.lerubanbleu.util;

import fr.free.gelmir.lerubanbleu.service.Episode;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeSaxParser
{
    public Episode getEpisode(InputStream is) {

        // Allocate an handler and get the episode
        EpisodeSaxHandler episodeSaxHandler = new EpisodeSaxHandler();
        return episodeSaxHandler.getEpisode(is);

    }
}
