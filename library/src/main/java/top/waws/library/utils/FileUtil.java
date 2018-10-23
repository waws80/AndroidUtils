package top.waws.library.utils;

import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;

import androidx.annotation.NonNull;
import top.waws.library.AppUtils;


/**
 *  @desc: 文件工具类
 *  @className: FileUtil
 *  @author: thanatos
 *  @createTime: 2018/10/19
 *  @updateTime: 2018/10/19 下午4:09
 */
public class FileUtil {

    private FileUtil(){}

    private static final class Inner{
        private static final FileUtil FILE_UTIL = new FileUtil();
    }

    public static FileUtil getInstance(){
        return Inner.FILE_UTIL;
    }

    /**
     * 读取Assets文件内容
     */
    public String getFromAssets(@NonNull String fileName) {
        try {
            InputStream r = AppUtils.getInstance().getContext().getResources()
                    .getAssets().open(fileName);
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            byte tmp[] = new byte[1024];
            byte content[];
            int i = 0;
            while ((i = r.read(tmp)) > 0) {
                byteout.write(tmp, 0, i);
            }
            content = byteout.toByteArray();
            String str = new String(content, "UTF-8");
            r.close();
            byteout.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把Assets里的文件拷贝到sd卡上
     */
    public boolean copyAssetToSDCard(AssetManager assetManager, String fileName, String destinationPath) {
        try {
            InputStream is = assetManager.open(fileName);
            FileOutputStream os = new FileOutputStream(destinationPath);

            if (is != null && os != null) {
                byte[] data = new byte[1024];
                int len;
                while ((len = is.read(data)) > 0) {
                    os.write(data, 0, len);
                }
                close(os);
                close(is);
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * 相对路径转绝对路径
     */
    public String uriToPath(Uri uri) {
        Cursor cursor = null;
        try {
            if (uri.getScheme().equalsIgnoreCase("file")) {
                return uri.getPath();
            }
            cursor = AppUtils.getInstance().getContext().getContentResolver()
                    .query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA)); //图片文件路径
            }
        } catch (Exception e) {
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
            return null;
        }
        return null;
    }

    /**
     * 删除文件夹
     */
    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 获取文件大小
     */
    public long getSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取目录名
     */
    public String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }
        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 检查文件是否不大于指定大小
     */
    public boolean checkFileSize(String filepath, int maxSize) {
        File file = new File(filepath);
        if (!file.exists() || file.isDirectory()) {
            return false;
        }
        if (file.length() <= maxSize * 1024) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 格式化文件大小的显示
     */
    public String formatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 读取文件
     */
    public StringBuilder readFile(File file) {
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(
                    new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            close(reader);
        }
    }

    /**
     * 写入文件
     */
    public boolean writeFile(File file, String content, boolean append) {
        if (file == null || !file.isFile()) {
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            close(fileWriter);
        }
    }

    /**
     * 写入文件
     */
    public boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDir(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            close(o);
            close(stream);
        }
    }

    /**
     * 移动文件
     */
    public void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile);
        }
    }

    /**
     * 复制文件
     */
    public boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(new File(destFilePath), inputStream, false);
    }


    /**
     * 删除文件
     */
    public boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
            return file.delete();
        }
    }

    /**
     * 创建目录
     */
    public boolean makeDir(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }
        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * 创建文件
     */
    public boolean makeFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 创建并返回文件
     * @param fileName 文件名字   eg: /test/a.png
     * @return
     */
    public File createFile(@NonNull String fileName){
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /**
     * 关闭流
     */
    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }

    public File saveBitmap(String dir, Bitmap bitmap, String fileName){
        if (bitmap == null || TextUtils.isEmpty(fileName)){
            return null;
        }
        File file = new File(Environment.getExternalStorageDirectory(), dir+fileName+".png");
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        }
    }

    public File saveBitmap(Bitmap bitmap, String path, String fileName){

        if (bitmap == null || TextUtils.isEmpty(fileName)){
            return null;
        }
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"DevoteQr";
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
//        File appDir = new File(path);
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fName = fileName+".png";
        File file = new File(appDir, fName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Uri uri = Uri.fromFile(file);
            AppUtils.getInstance().getContext()
                    .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
            return file;
        } catch (FileNotFoundException e){
            e.printStackTrace();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return file;
        } finally {
            if(!bitmap.isRecycled()){
                System.gc();
            }
        }
    }

    /**
     * 扫描方式
     */
    public enum ScannerType {
        RECEIVER,MEDIA
    }

    /**
     * Receiver 扫描更新图库
     * @param path
     */
    private void ScannerByReceiver(String path){
        AppUtils.getInstance().getContext()
                .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://"+path)));
    }

    /**
     * Media 扫描更新图库
     * @param path
     */
    private void ScannerByMedia(String path){
        MediaScannerConnection.scanFile(AppUtils.getInstance().getContext(),
                new String[]{path},null,null);
    }

    public void saveBitmapType(Bitmap bitmap, String path, String fileName,ScannerType type){
        if (bitmap == null || TextUtils.isEmpty(fileName)){
            return;
        }
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
        if(!appDir.exists()){
            appDir.mkdir();
        }
        String fName = fileName+".png";
        File file = new File(appDir, fName);

        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(type == ScannerType.RECEIVER){
                ScannerByReceiver(file.getAbsolutePath());
            }else if(type == ScannerType.MEDIA){
                ScannerByMedia(file.getAbsolutePath());
            }
            if(!bitmap.isRecycled()){
                System.gc();
            }
        }
    }
}
