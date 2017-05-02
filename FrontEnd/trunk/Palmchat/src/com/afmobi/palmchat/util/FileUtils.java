package com.afmobi.palmchat.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.RequestConstant;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.main.constant.Constants;
import com.afmobi.palmchat.util.universalimageloader.core.DisplayImageOptions;
import com.afmobi.palmchat.util.universalimageloader.core.ImageLoader;
import com.afmobi.palmchat.util.universalimageloader.core.assist.ImageSize;
import com.afmobi.palmchat.util.universalimageloader.core.display.FitXDisplayer;
import com.afmobigroup.gphone.R;
import com.core.AfStoreProdList;

public class FileUtils {

    public static boolean sdcardIsEnable() {

        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static LruCache<String, Bitmap> mMemoryBitmapCache;

    /**
     * add by zhh
     *
     * @param context
     * @param fileName
     * @return
     */
    public static Drawable getImageFromAssetsFile2(Context context, String fileName, int h) {
        if (h <= 0) {//当被回收的时候这里的h就是为0了 所以要加个保护 不让crash 并设置一个默认高
            h = ImageUtil.DISPLAYW * 2 / 3;
        }
        Bitmap bitmap = ImageLoader.getInstance().loadImageSync("assets://album/" + fileName + ".jpg"
                , new ImageSize(ImageUtil.DISPLAYW, h));
        if (bitmap != null) {
            bitmap = ImageUtil.getImageThumbnail(bitmap, ImageUtil.DISPLAYW, h);
            return new BitmapDrawable(bitmap);
        }
        /*AssetManager am = context.getResources().getAssets();
        InputStream is = null;
		PalmchatLogUtils.i(TAG, fileName + "");
		try {
			is = am.open("album/" + fileName + ".jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (is != null) {
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(is, null, null);
				int w = PalmchatApp.getApplication().getWindowWidth();
				bitmap = ImageUtil.getImageThumbnail(bitmap, w, h);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				// mMemoryBitmapCache.evictAll();
			}
			return new BitmapDrawable(bitmap);
		}*/
        return null;
    }

    public static String getStringFromInputStream(InputStream a_is) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(a_is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    public static Bitmap newBitmap(Bitmap bm) {
        Bitmap newbm = null;
        int dispalyW = PalmchatApp.getApplication().getWindowWidth();
        newbm = Bitmap.createScaledBitmap(bm, dispalyW, dispalyW, false);
        return newbm;

    }

    public static String writeBytes(String path, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            Log.e("writeBytes", bytes.length + ">" + path);
            fos.write(bytes);
            fos.flush();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void copyToImg(final Handler handler, final String inPath, final String outPath) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (copyToImg(inPath, outPath) != null) {
                    handler.sendEmptyMessage(Constants.SAVE_IMG_TOPHONE_SUCCESS);
                } else {
                    handler.sendEmptyMessage(Constants.SAVE_IMG_TOPHONE_FAILED);
                }
            }
        }).start();
    }

    /**
     * heguiming copy file
     *
     * @param inPath
     * @param outPath
     * @return
     */
    public static File copyToImg(String inPath, String outPath) {
        File source = new File(inPath);
        File target = new File(outPath);
        boolean isSuccess = false;
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
            isSuccess = true;// return target;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inStream) {
                    inStream.close();
                }
                if (null != in) {
                    in.close();
                }
                if (null != outStream) {
                    outStream.close();
                }
                if (null != out) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isSuccess) {
            return target;
        } else {
            return null;
        }
    }

    /*
     * @param fromFile 被复制的文件
     *
     * @param toFile 复制的目录文件
     *
     * @param rewrite 是否重新创建文件
     *
     * 文件的复制操作方法
     */
    public static void copyfile(String inPath, String outPath) {
        File fromFile = new File(inPath);
        File toFile = new File(outPath);
        if (!fromFile.exists()) {
            PalmchatLogUtils.e(TAG, inPath + "copyfile exists==false");
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            // 关闭输入、输出流
            fosfrom.close();
            fosto.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            PalmchatLogUtils.e(TAG, e.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // public static File copyToImg(String inPath, String outPath) {
    // FileInputStream fis = null;
    // FileOutputStream fos = null;
    // try {
    // File file = new File(inPath);
    //
    // fis = new FileInputStream(file);
    // byte[] bytes = new byte[fis.available()];
    // fis.read(bytes);
    //
    // File file2 = new File(outPath);
    // fos = new FileOutputStream(file2);
    // fos.write(bytes);
    // fos.flush();
    // PalmchatLogUtils.e(TAG, "copyToImg = " + file2.getAbsolutePath());
    // return file2;
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // return null;
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // } finally {
    // if (fis != null) {
    // try {
    // fis.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // if (fos != null) {
    // try {
    // fos.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }

    /**
     * 把图片复制到/mnt/sdcard/afmobi/palmchat/camera/
     *
     * @param path
     * @return
     */
    public static File copyToImg(String path) {// /mnt/sdcard/afmobi/palmchat/camera/img_20130907_034318.jpg
        // //
        // /mnt/sdcard/afmobi/palmchat/camera/
        File file = new File(path);
        if (!(RequestConstant.CAMERA_CACHE + file.getName()).equals(path)) {
            File file2 = copyToImg(path, RequestConstant.CAMERA_CACHE + file.getName());
            if (file2 != null) {
                return file2;
            }
        }
        return file;
    }

    public static byte[] readBytes(String file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int len = fis.available();
            if (len > 5000 * 1024 || len < 0) {
                throw new RuntimeException("");
            }
            Log.e("readBytes", len + ">" + file);
            byte[] bytes = new byte[len];
            fis.read(bytes);
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public static boolean fileIsExists(String path) {
        File file = new File(path);
        PalmchatLogUtils.i("file", "file " + path + " is exists :" + file.exists());
        boolean isExists = file.exists();
        if (isExists) {
            PalmchatLogUtils.println("file.length():" + file.length());
        }
        return isExists;
    }

    public static boolean fileDelete(String path) {
        File file = new File(path);
        boolean isDelete = file.delete();
        Log.i("file", "file " + path + " is delete :" + isDelete);
        return isDelete;
    }

    public static boolean isHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private static final String TAG = "FileUtil";

    public static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    public static List<String> getEmojiFile(Context context) {
        try {
            List<String> list = new ArrayList<String>();
            InputStream in = context.getResources().getAssets().open("emoji");// emoji.txt
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    String SDPATH = Environment.getExternalStorageDirectory().getPath();

    public List<String> readSDFile(String fileName) {
        File file = new File(SDPATH + "/" + fileName);
        // FileReader fis=null;
        // InputStream in = null;
        BufferedReader br = null;
        List<String> list = new ArrayList<String>();
        try {

            // fis = new FileReader(file);
            // in = new BufferedInputStream(fis);
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            if (br != null) {
                br.close();
            }
            // fis.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

        }

        return list;

    }

    /**
     * delete file and folder
     */
    public static void delete(String path) {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i].getAbsolutePath());
            }
            file.delete();
        }
    }

    /**
     * Access to the specified all eligible path.
     */
    private static List<String> listPath(File path) {
        List<String> list = new ArrayList<String>();
        File[] files = path.listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File file : files) {
                if (file.isDirectory()) {
                    List<String> _list = listPath(file);
                    list.addAll(_list);
                } else {
                    list.add(file.getAbsolutePath());
                }
            }
        }

        return list;
    }

    /**
     * renameTo
     *
     * @param FacePath
     */
    public static void renameTo(String FacePath) {
        File file = new File(FacePath);
        String dstSuffix = "pal";
        List<String> paths = listPath(file);
        for (String path : paths) {
            File srcFile = new File(path);
            String name = srcFile.getName();
            int idx = name.lastIndexOf(".");
            String prefix = name.substring(0, idx);
            File dstFile = new File(srcFile.getParent() + "/" + prefix + "." + dstSuffix);
            srcFile.renameTo(dstFile);
        }
    }

    private static void delete(File file) {
        // TODO Auto-generated method stub
        boolean isDelete = file.delete();
        Log.i("file", "file is delete :" + isDelete);
    }

    public static boolean fileIsExistsAndCanUse(String path) {
        File file = new File(path);
        boolean flag = file.exists();
        if (flag) {
            long length = file.length();
            if (length > 10) {
                return true;
            }
        }
        return false;
    }

    public static void SaveAfStoreProdListToFile(AfStoreProdList obj, String filepath) {
        if (null == obj || filepath == null || filepath.length() <= 0) {
            return;
        }

        if (fileIsExists(filepath)) {
            fileDelete(filepath);
        }

        ObjectOutputStream outObj = null;
        try {
            // 将对象内容写入到文本中
            FileOutputStream outStr = new FileOutputStream(filepath);
            outObj = new ObjectOutputStream(outStr);
            outObj.writeObject(obj);

            outObj.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outObj != null) {
                    outObj.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static AfStoreProdList LoadAfStoreProdListFromFile(String filepath) {
        AfStoreProdList readlist = null;
        if (null == filepath || filepath.length() <= 0 || !fileIsExists(filepath)) {
            return readlist;
        }
        ObjectInputStream inObj = null;

        try {
            FileInputStream inStr = new FileInputStream(filepath);
            inObj = new ObjectInputStream(inStr);
            readlist = (AfStoreProdList) inObj.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inObj != null) {
                try {
                    inObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return readlist;
    }

    /**
     * add by zhh 2016-09-05
     * <p/>
     * 将json数据保存到sd卡的文件中
     *
     * @param jsonStr
     */
    public static void writeJsonStrToSDFile(String jsonStr, String path, String fileName) {
        ObjectOutputStream outObj = null;
        try {

            File mFile = new File(path);
            if (!mFile.exists())
                mFile.mkdir();

            path = path + fileName;
            mFile = new File(path);
            if (!mFile.exists())
                mFile.createNewFile();


            // 将数据写入到文本中
            FileOutputStream outStr = new FileOutputStream(path, false);
            outObj = new ObjectOutputStream(outStr);
            outObj.writeObject(jsonStr);
            outObj.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outObj != null) {
                    outObj.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * add by zhh 2016-09-05
     * <p/>
     * 读取sd卡中json数据
     *
     * @param
     */
    public static String readJsonStrFromSDFile(String path, String fileName) {
        ObjectInputStream inObj = null;
        try {
//            String path = RequestConstant.MOBILE_TOP_UP_CACHE;
            path = path + fileName;
            File mFile = new File(path);
            if (mFile.exists()) {
                // 将对象内容写入到文本中
                FileInputStream inStr = new FileInputStream(path);
                inObj = new ObjectInputStream(inStr);
                String data = (String) inObj.readObject();
                return data;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inObj != null) {
                try {
                    inObj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 遍历某个目录下文件
     *
     * @param dirPath
     * @return
     */
    public static HashMap<String, String> getFileList(String dirPath) {
        HashMap<String, String> map = new HashMap<>();
        File[] file = new File(dirPath).listFiles();
        for (File mFile : file) {
            String data = readJsonStrFromSDFile(dirPath, mFile.getName());
            map.put(mFile.getName(), data);
        }
        return map;
    }

    /**
     * 删除某个目录下的指定文件
     *
     * @param dirPath
     */
    public static void deleteFilesBydir(String dirPath, HashMap<String, String> map) {
        if (map != null) {
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String fileName = (String) entry.getKey();
                File mFile = new File(dirPath + "/" + fileName);
                if (mFile.exists())
                    mFile.delete();
            }
        }


    }

}
