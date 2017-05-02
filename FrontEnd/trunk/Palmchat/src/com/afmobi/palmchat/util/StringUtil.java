package com.afmobi.palmchat.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.TextView;


public class StringUtil {
    public static String SLASH = "/";


    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static String MD5(String myinfo) {
        byte[] digesta = null;
        try {
            java.security.MessageDigest alga = java.security.MessageDigest.getInstance("MD5");
            alga.update(myinfo.getBytes());
            digesta = alga.digest();

        } catch (java.security.NoSuchAlgorithmException ex) {
        }
        if (null != digesta) {
            return byte2hex(digesta);
        } else {
            return "";
        }
    }

    public static String byte2hex(byte[] b) { //
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs;
            }
        }
        return hs;
    }

    public static byte[] hex2byte(String hexstr) {
        int length = hexstr.length() / 2;
        byte[] bytes = new byte[length];
        byte[] source = hexstr.getBytes();

        for (int i = 0; i < bytes.length; ++i) {
            byte bh = Byte.decode("0x" + new String(new byte[]
                    {source[i * 2]})).byteValue();
            bh = (byte) (bh << 4);
            byte bl = Byte.decode("0x" + new String(new byte[]
                    {source[i * 2 + 1]})).byteValue();
            bytes[i] = (byte) (bh ^ bl);
        }

        return bytes;

    }

    // public static String getIDFromJID(String jid) {
    // String id = null;
    // if (jid != null && jid.contains("@"))
    // {
    // id = jid.substring(0, jid.indexOf("@"));
    // } else {
    // id = jid;
    // }
    // return id;
    // }
    //
    // public static String getJIDFromJID(String jid) {
    // String id = null;
    // if (jid.contains("/")) {
    // id = jid.substring(0, jid.indexOf("/"));
    // } else {
    // id = jid;
    // }
    // return id;
    // }

    public static String replace(String oldStr, String newStr, String replacement) {
        if (newStr == null || oldStr == null || replacement == null) {
            return replacement;
        }
        int lenc = replacement.length();
        int lenb = oldStr.length();
        if (lenb <= lenc) {
            if (replacement.substring(0, lenb).equals(oldStr)) {
                replacement = newStr + replacement.substring(lenb);
            }
        }
        return replacement;
    }

    public static String[] split2(String str, String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return new String[]
                    {""};
        }
        Vector<String> vector = new Vector<String>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            while (i < len) {
                if (str.charAt(i) == '\r' || str.charAt(i) == '\n' || str.charAt(i) == '\t') {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        } else if (separatorChars.length() == 1) {
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        } else {
            while (i < len) {
                int id = i + separatorChars.length() < len ? i + separatorChars.length() : len;
                if (separatorChars.indexOf(str.charAt(i)) >= 0 && separatorChars.equals(str.substring(i, id))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        vector.addElement(str.substring(start, i));
                        match = false;
                    }
                    i += separatorChars.length();
                    start = i;
                } else {
                    lastMatch = false;
                    match = true;
                    i++;
                }
            }
        }

        if (match || preserveAllTokens && lastMatch) {
            vector.addElement(str.substring(start, i));
        }
        String[] ret = new String[vector.size()];
        vector.copyInto(ret);
        return ret;
    }

    public static String deleteBlank(String str) {
        if (null != str && 0 < str.trim().length()) {
            char[] array = str.toCharArray();
            int start = 0, end = array.length - 1;
            while (array[start] == ' ')
                start++;
            while (array[end] == ' ')
                end--;
            return str.substring(start, end + 1).replaceAll("\n", "");
            // return new String(array, start, end - start);
        } else {
            return "";
        }

    }


    public static String deleteLastTrim(String str) {
        String s = str.replaceAll("\n", "");
         /*if (str!=null) {
             Pattern p = Pattern.compile("\t|\r|\n");  
             Matcher m = p.matcher(str);  
             dest = m.replaceAll("");  
     } */
        if (null != s && 0 < s.trim().length()) {
            char[] array = s.toCharArray();
            int start = 0, end = array.length - 1;
            while (array[end] == ' ')
                end--;
            return s.substring(start, end + 1);
        } else {
            return "";
        }
    }


    public static String utfString(String str) {
        if (str != null && str.length() > 0) {
            try {
                str = new String(str.getBytes(), "UTF_8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return str;
        }
        return str;
    }

    public static boolean isFileExist(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getFormatTime(String format, Date date) {
        SimpleDateFormat tempDate = new SimpleDateFormat(format);
        String datetime = tempDate.format(date);
        return datetime;
    }

    public static byte[] BitmapToBytes(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (OutOfMemoryError oom) {
            Log.e("error", " --------lowmemory--------- StringUtil(line 481)");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static String getAvatarHash(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        digest.update(bytes);
        return encodeHex(digest.digest());
    }

    public static String encodeHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder(bytes.length * 2);

        for (byte aByte : bytes) {
            if (((int) aByte & 0xff) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toString((int) aByte & 0xff, 16));
        }

        return hex.toString();
    }

    public static String getPhotoType(String imgName) {
        if (null == imgName) {
            Log.e("StringUtil", "error.the param is null.");
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String type = imgName.substring(imgName.lastIndexOf(".") + 1).toLowerCase();
        if (type.equals("gif") || type.equals("png") || type.equals("jpeg") || type.equals("tiff")
                || type.equals("jpg")) {
            sb.append("image/");
            sb.append(type);
        } else if (type.equals("basic") || type.equals("x-wav") || type.equals("x-mpeg") || type.equals("x-mpeg-2")
                || type.equals("mp3") || type.equals("amr") || type.equals("wma") || type.equals("wav")
                || type.equals("aac"))// add by liheng 2011-9-7
        {
            sb.append("audio/");
            sb.append(type);
        } else if (type.equals("mp4")) {
            sb.append("video/");
            sb.append(type);
        }

        return sb.toString();
    }

    public static String getFileType(String imgName) {
        if (null == imgName) {
            Log.e("StringUtil", "error.the param is null.");
            return null;
        }
        String sb = null;
        String type = imgName.substring(imgName.lastIndexOf(".") + 1).toLowerCase();
        if (type.equals("gif") || type.equals("png") || type.equals("jpeg") || type.equals("tiff")
                || type.equals("jpg")) {
            sb = "image";

        } else if (type.equals("basic") || type.equals("x-wav") || type.equals("x-mpeg") || type.equals("x-mpeg-2")
                || type.equals("mp3") || type.equals("amr") || type.equals("aac"))// add
        // by
        // liheng
        // 2011-9-7
        {
            sb = "audio";
        } else if (type.equals("mp4")) {
            sb = "video";

        }

        return sb;
    }


    public static String strToName(String name, int length) {

        if (name != null && name.length() > length) {
            Paint paint = new Paint();
            paint.setTextSize(18);
            float strWidth = paint.measureText(name);
            if (strWidth > length) {
                name = name.substring(0, length - 1) + "...";
            }
        }
        return name;
    }

    public static String strToName(String name) {
        if (name != null && name.length() > 9) {
            Paint paint = new Paint();
            paint.setTextSize(18);
            float strWidth = paint.measureText(name);
            if (strWidth > 9) {
                name = name.substring(0, 9) + "...";
            }
        }
        return name;
    }

    public static boolean isContainString(String source, String des) {
        boolean contain = false;
        int tem = 0;
        for (int i = 0; i < des.length(); i++) {
            for (int j = tem; j < source.length(); j++) {
                if (des.charAt(i) == source.charAt(j)) {
                    if (i == des.length() - 1) {
                        return true;
                    } else {
                        tem = j + 1;
                        break;
                    }
                } else {
                    if (j == source.length() - 1) {
                        return contain;
                    }
                }
            }
        }
        return contain;
    }


    public static String escapeElementEntities(String text) {
        char block[] = null;
        int last = 0;
        int size = text.length();
        StringBuffer buffer = new StringBuffer();
        int i;
        for (i = 0; i < size; i++) {
            String entity = null;
            char c = text.charAt(i);
            switch (c) {
                case 60: // '<'
                    entity = "&lt;";
                    break;

                case 62: // '>'
                    entity = "&gt;";
                    break;

                case 38: // '&'
                    entity = "&amp;";
                    break;

                case 9: // '\t'
                case 10: // '\n'
                case 13: // '\r'
                    entity = String.valueOf(c);
                    break;

                default:
                    break;
            }
            if (entity == null)
                continue;
            if (block == null)
                block = text.toCharArray();
            buffer.append(block, last, i - last);
            buffer.append(entity);
            last = i + 1;
        }

        if (last == 0)
            return text;
        if (last < size) {
            if (block == null)
                block = text.toCharArray();
            buffer.append(block, last, i - last);
        }
        String answer = buffer.toString();
        buffer.setLength(0);
        return answer;
    }

    public static boolean isNullString(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }

        return false;
    }


    public static String getTelecomPlmn(Context context) {
        if (context == null) {
            return null;
        }

        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (TelephonyManager.SIM_STATE_READY != telManager.getSimState()) {
            return null;
        }

        String imsi = telManager.getSubscriberId();
        String plmn = null;

        if (!StringUtil.isEmpty(imsi, true) && imsi.length() > 5) {
            plmn = imsi.substring(0, 5);
        } else {
            plmn = telManager.getSimOperator();
        }

        return plmn;
    }

    public static String getTelecomImsi(Context context) {
        if (context == null) {
            return null;
        }

        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (TelephonyManager.SIM_STATE_READY != telManager.getSimState()) {
            return null;
        }

        return telManager.getSubscriberId();
    }

    public static String getWifiMacAdress(Context context) {
        if (context == null) {
            return null;
        }
        WifiManager wifi = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
        WifiInfo info = wifi.getConnectionInfo();
        if (null != info) {
            return info.getMacAddress();
        } else {
            return null;
        }
    }


    public static boolean isEmpty(String str, boolean isToTrim) {
        if (null == str) {
            return true;
        }
        if (isToTrim) {
            if (0 >= str.trim().length()) {
                return true;
            }
        } else {
            if (0 >= str.length()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(CharSequence cs, boolean isToTrim) {
        if (null == cs) {
            return true;
        }
        return isEmpty(cs.toString(), isToTrim);
    }


    public static ArrayList<String> parsePhoneList(TextView textView) {
        ArrayList<String> phoneList = new ArrayList<String>();
        final URLSpan[] spans = textView.getUrls();

        for (URLSpan span : spans) {
            Uri uri = Uri.parse(span.getURL());
            if (null != uri && uri.toString().startsWith("tel:")) {
                String phone = uri.toString().substring("tel:".length());
                if (!phoneList.contains(phone)) {
                    phoneList.add(phone);
                }
            }
        }
        return phoneList;
    }

    public static ArrayList<String> parseEmialList(TextView textView) {
        ArrayList<String> emailList = new ArrayList<String>();
        final URLSpan[] spans = textView.getUrls();

        for (URLSpan span : spans) {
            Uri uri = Uri.parse(span.getURL());
            if (null != uri && uri.toString().startsWith("mailto:")) {
                String email = uri.toString().substring("mailto:".length());
                if (!emailList.contains(email)) {
                    emailList.add(email);
                }
            }
        }
        return emailList;
    }

    public static String replaceForSql(String text) {
        return replaceForSql(text, "'", "''", -1);
    }

    public static String replaceForSql(String text, String searchString, String replacement, int max) {
        if (isEmpty(text, false) || isEmpty(searchString, false) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }


    public static String getMD5HexString(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(message.getBytes("utf-8"));
            return byteToHexStringSingle(b);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    private static String byteToHexStringSingle(byte[] byteArray) {
        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static boolean isNullOrEmpty(String str) {
        return null == str || str.trim().length() < 1;
    }

    public static String ToSBC(String input) {

        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * @param content
     * @return
     * @description
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }


    /**
     * @param s
     * @return
     * @description
     */
    public static int getChineseNum(String s) {

        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }
}
