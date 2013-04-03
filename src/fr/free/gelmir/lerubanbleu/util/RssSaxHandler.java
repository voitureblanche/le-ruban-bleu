package fr.free.gelmir.lerubanbleu.util;

import android.util.Log;
import fr.free.gelmir.lerubanbleu.service.Episode;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class RssSaxHandler extends DefaultHandler
{
    private List<Episode> mEpisodes;
    private Episode mCurrentEpisode;
    private StringBuilder mStringBuilder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mEpisodes = new ArrayList<Episode>();
        mStringBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equalsIgnoreCase("item")) {
            mCurrentEpisode = new Episode();
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (this.mCurrentEpisode != null) {
            if (localName.equalsIgnoreCase("title")) {
                mCurrentEpisode.setTitle(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("link")) {
                mCurrentEpisode.setLink(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("description")) {
                mCurrentEpisode.setDescription(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("puDate")) {
                mCurrentEpisode.setPubDate(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("item")) {
                mEpisodes.add(mCurrentEpisode);
            }
            mStringBuilder.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        mStringBuilder.append(ch, start, length);
    }


    public List<Episode> getLatestArticles(String feedUrl) {
        URL url = null;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            url = new URL(feedUrl);

            xr.setContentHandler(this);
            xr.parse(new InputSource(url.openStream()));

        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        }

        return mEpisodes;

    }
}

