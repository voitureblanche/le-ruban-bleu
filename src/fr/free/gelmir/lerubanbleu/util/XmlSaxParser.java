package fr.free.gelmir.lerubanbleu.util;

import android.content.Context;
import fr.free.gelmir.lerubanbleu.service.Episode;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 23:43
 * To change this template use File | Settings | File Templates.
 */
public class XmlSaxParser
{
    public Episode getEpisode(InputStream is, Context context) {

        // Allocate an handler and get the episode
        XmlSaxHandler xmlSaxHandler = new XmlSaxHandler();
        return xmlSaxHandler.getEpisode(is, context);

    }

    public int getTotalNumber(InputStream is, Context context) {

        // Allocate an handler and get total number of episodes
        XmlSaxHandler xmlSaxHandler = new XmlSaxHandler();
        return xmlSaxHandler.getTotalNumber(is, context);

    }
}
