package com.afmobi.palmchat.util;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by hp on 14-3-11.
 */
public interface XmlParserData {
    void startDoc(XmlPullParser xpp, String key_word);
    void dataParse(XmlPullParser xpp, String nodeName);
    void endParse(String key_word);
    void endDoc();
}
