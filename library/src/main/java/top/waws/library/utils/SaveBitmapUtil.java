package top.waws.library.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.lang.ref.WeakReference;

import top.waws.library.AppUtils;


/**
 *  @desc: 保存图片
 *  @className: SaveBitmapUtil
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:15
 */
public final class SaveBitmapUtil {



    private File f;

    private SaveBitmapUtil(){}

    private static final class Inner{
        private static final SaveBitmapUtil SAVE_BITMAP_UTIL = new SaveBitmapUtil();
    }

    public static SaveBitmapUtil getInstance(){
        return Inner.SAVE_BITMAP_UTIL;
    }

    public String getPath(){
        if (f == null) {
            return null;
        }
        return f.getPath();
    }

    public File saveBitmap(String dir, Bitmap bmSave,String prefix){
        return saveBitmap(dir, bmSave,prefix,true);
    }

    public File saveBitmap(String dir, final Bitmap bmSave, final String prefix, final boolean isShowToast){
        f = FileUtil.getInstance().saveBitmap(dir,bmSave,prefix+System.currentTimeMillis());
        if (f != null){
            scanPhoto(f);
        }
        if (isShowToast){
            AppUtils.getInstance().showToast("图片已保存至"+dir);
        }
        return f;
    }

    /** 通知系统，刷新相册 */
    private void scanPhoto(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        AppUtils.getInstance().getContext().sendBroadcast(mediaScanIntent);

    }
}
