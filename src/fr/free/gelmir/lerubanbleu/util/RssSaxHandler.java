package fr.free.gelmir.lerubanbleu.util;

import android.util.Log;
import fr.free.gelmir.lerubanbleu.service.Article;
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
    private List<Article> mArticles;
    private Article mCurrentArticle;
    private StringBuilder mStringBuilder;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mArticles = new ArrayList<Article>();
        mStringBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equalsIgnoreCase("item")) {
            mCurrentArticle = new Article();
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (this.mCurrentArticle != null) {
            if (localName.equalsIgnoreCase("title")) {
                mCurrentArticle.setTitle(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("link")) {
                mCurrentArticle.setLink(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("description")) {
                mCurrentArticle.setDescription(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("puDate")) {
                mCurrentArticle.setPubDate(mStringBuilder.toString());
            }
            else if (localName.equalsIgnoreCase("item")) {
                mArticles.add(mCurrentArticle);
            }
            mStringBuilder.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        mStringBuilder.append(ch, start, length);
    }


    public List<Article> getLatestArticles(String feedUrl) {
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

        return mArticles;

    }
}

