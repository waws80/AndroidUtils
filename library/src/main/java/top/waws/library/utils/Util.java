package top.waws.library.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;

import androidx.annotation.NonNull;

/**
 *  @desc: 工具类
 *  @className: Utils
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午3:43
 */
public class Util {

    private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

    /**
     * base64加密
     * @param str
     * @return
     */
    public String encodeBase64(@NonNull String str){
        return new String(Base64.encode(str.getBytes(),Base64.DEFAULT));
    }

    /**
     * base64解密
     * @param str
     * @return
     */
    public String decodeBase64(@NonNull String str){
        return new String(Base64.decode(str,Base64.DEFAULT));
    }

    /**
     * AES加密
     * @param str
     * @return
     */
    public String encodeAES(@NonNull String str){
        try {
            return AESUtil.getInstance().encodeToString(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解密AES
     * @param str
     * @return
     */
    public String decodeAES(@NonNull String str){
        return AESUtil.getInstance().decode(str.getBytes());
    }

    /**
     * MD5加密
     * @param str 字符
     * @return
     */
    public String md5Encode(@NonNull String str){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            return byteArrayToHexString(md.digest(str.getBytes()));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private String byteArrayToHexString(byte b[]){
        StringBuffer resultSb = new StringBuffer();
        for(int i = 0; i < b.length; i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigIts[d1] + hexDigIts[d2];
    }


    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public String bitmapToBase64(@NonNull Bitmap bitmap) {

        return bitmapToBase64(bitmap,100);
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @param quality 压缩百分比 0 -100
     * @return
     */
    public String bitmapToBase64(@NonNull Bitmap bitmap, int quality){
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                double value = bitmap.getRowBytes() * bitmap.getHeight() * 1.0;
                value = value /1024.0/1024.0;
                if (value <= 10.0){
                    quality = 100;
                }
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转bitmap
     * @param base64
     * @return
     */
    public Bitmap base64ToBitmap(@NonNull String base64) {
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 检测sdcard是否存在
     *
     * @return
     */
    public boolean sdcardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取sdcard卡剩余空间
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public long getSDAvaliableSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long availableBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (availableBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 获取sdcard卡总容量
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public long getSDTotalSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long totalBlocks = sf.getBlockCount();
        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (totalBlocks * blockSize) / 1024 / 1024; // 单位MB
    }


    /**
     * 是否锁屏
     * @param context
     * @return
     */
    public boolean isScreenLocked(Context context){
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }



}
