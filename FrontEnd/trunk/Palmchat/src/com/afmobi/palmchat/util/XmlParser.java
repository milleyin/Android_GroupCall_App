package com.afmobi.palmchat.util;

import android.content.Context;
import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;


/**
 * Created by hp on 14-3-11.
 */
public class XmlParser implements XmlParserData{

    private XmlParserData m_xpd;
    public XmlParser(XmlParserData xpd)
    {
        m_xpd = xpd;
    }



    public  int parseFromAssets(Context context ,String name)
    {
        Resources res = context.getResources();

        InputStream is  = null;
        try {
            is = res.getAssets().open(name);

        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlPullParser xpp = null;
        XmlPullParserFactory pullParserFactory= null;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            xpp=pullParserFactory.newPullParser();
            xpp.setInput(is, "utf-8");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        try {
            loop_data(xpp);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }







    private  void loop_data(XmlPullParser xpp) throws XmlPullParserException, IOException {
            int eventType = xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT)
            {
                String nodeName=xpp.getName();
              /*  if(nodeName!=null)
                    printf("|   got name:%: ",nodeName);*/
                switch (eventType)
                {
                    case XmlPullParser.START_DOCUMENT:
                    {
                        startDoc(xpp,nodeName);
                        break;
                    }

                    case XmlPullParser.START_TAG:
                    {
                        dataParse(xpp,nodeName);
                        break;
                    }
                    case XmlPullParser.END_TAG:
                    {
                        endParse(nodeName);
                        break;
                    }
                    default:
                        break;
                }
                try {
                    eventType=xpp.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        endDoc();

    }

    /**
     *          Implements  Method
     */
    public void startDoc(XmlPullParser xpp, String key_word)
    {
        m_xpd.startDoc(xpp,key_word);
    }


    public void dataParse(XmlPullParser xpp,String nodeName)
    {
        m_xpd.dataParse(xpp,nodeName);
    }


    public void endParse(String key_word)
    {
        m_xpd.endParse(key_word);
    }


    public void endDoc()
    {
        m_xpd.endDoc();
    }







}
