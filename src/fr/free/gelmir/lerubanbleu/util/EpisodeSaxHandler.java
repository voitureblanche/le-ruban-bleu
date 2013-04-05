package fr.free.gelmir.lerubanbleu.util;

import android.net.Uri;
import android.util.Base64;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: gerard
 * Date: 07/03/13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class EpisodeSaxHandler extends DefaultHandler
{
    private StringBuilder mStringBuilder;
    private Episode mEpisode;
    private String mImageName = null;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        mEpisode = new Episode();
        mStringBuilder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equalsIgnoreCase("episode"))
        {
            try {
                int number = Integer.parseInt(attributes.getValue("number"));
                mEpisode.setEpisodeNb(number);
                Log.d("EpisodeSaxHandler", "episode number " + attributes.getValue("number") + " found!");
            } catch(Exception e) {
                throw new SAXException(e); // attribute content is not an integer
            }
        }
        else if (localName.equalsIgnoreCase("image"))
        {
            mImageName = attributes.getValue("name");
        }
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        super.endElement(uri, localName, name);
        if (localName.equalsIgnoreCase("image"))
        {
            // base64 decode
            String imageBase64 = mStringBuilder.toString();
            byte[] image = Base64.decode(imageBase64, Base64.DEFAULT);

            // Create file
            File file = new File(mImageName);
            Log.d("EpisodeSaxHandler", "image " + mImageName + " found!");

            // write file
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(image);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // save file URI
            mEpisode.setImageUri(Uri.fromFile(file));
        }

        mStringBuilder.setLength(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        mStringBuilder.append(ch, start, length);
    }


    public Episode getEpisode(InputStream is)
    {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();

            xr.setContentHandler(this);
            xr.parse(new InputSource(is));

        } catch (IOException e) {
            Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
        } catch (SAXException e) {
            Log.e("RSS Handler SAX", e.toString());
        } catch (ParserConfigurationException e) {
            Log.e("RSS Handler Parser Config", e.toString());
        }

        return mEpisode;
    }


}

