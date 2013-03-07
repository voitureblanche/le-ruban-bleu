package fr.free.gelmir.lerubanbleu;

import android.os.Message;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class RssHandler extends DefaultHandler {

    private List<Article> articles;
    private Article currentArticle;
    private StringBuilder builder;

    public List<Message> getLatestMessages(){
        return this.articles;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        articles = new ArrayList<Message>();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (localName.equalsIgnoreCase(ITEM)){
            this.currentArticle = new Message();
        }
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        super.endElement(uri, localName, name);
        if (this.currentArticle != null){
            if (localName.equalsIgnoreCase(TITLE)){
                currentArticle.setTitle(builder.toString());
            }

            else if (localName.equalsIgnoreCase(LINK)){
                currentArticle.setLink(builder.toString());
            }

            else if (localName.equalsIgnoreCase(DESCRIPTION)){
                currentArticle.setDescription(builder.toString());
            }

            else if (localName.equalsIgnoreCase(PUB_DATE)){
                currentArticle.setDate(builder.toString());
            }

            else if (localName.equalsIgnoreCase(ITEM)){
                articles.add(currentArticle);
            }
            builder.setLength(0);
        }
    }

}


