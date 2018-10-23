package top.waws.library.utils;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import top.waws.library.AppUtils;

/**
 *  @desc: AES加密工具类
 *  @className: AESUtils
 *  @author: thanatos
 *  @createTime: 2018/10/20
 *  @updateTime: 2018/10/20 下午7:24
 */
public class AESUtil {

    private final static String HEX = "0123456789ABCDEF";
    private  static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";//AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private  static final String AES = "AES";//AES 加密
    private  static final String  SHA1PRNG="SHA1PRNG";// SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法


    private static final String AESKEY = "cdd4eabf30a6414fae0dc71286284c69";

    private AESUtil(){}

    private static final class Inner{
        private static final AESUtil UTILS = new AESUtil();
    }

    public static AESUtil getInstance(){
        return Inner.UTILS;
    }


    public byte[] encode(String content) throws Exception {
        // 创建AES秘钥
        SecretKeySpec key = new SecretKeySpec(AESKEY.getBytes(), CBC_PKCS5_PADDING);
        // 创建密码器
        Cipher cipher = Cipher.getInstance(AES);
        // 初始化加密器
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // 加密
        return cipher.doFinal(AppUtils.getInstance().getDefaultUtil().encodeBase64(content)
                .getBytes(Charset.forName("UTF-8")));
    }

    /**
     * 加密成string
     * @param content
     * @return
     */
    public String encodeToString(String content){
        try {
            return new String(encode(content),Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private Cipher decodeCipher(){
        try {
            // 创建AES秘钥
            SecretKeySpec key = new SecretKeySpec(AESKEY.getBytes(), CBC_PKCS5_PADDING);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(AES);
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private byte[] decrypt(byte[] content) throws Exception {
        // 解密
        if (decodeCipher() != null){
            return decodeCipher().doFinal(content);
        }else {
            return new byte[0];
        }

    }

    public String decode(byte[] content){
        try {
            byte[] bytes = decrypt(content);
            return AppUtils.getInstance().getDefaultUtil().decodeBase64(new String(bytes,Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String decode(byte[] content,Cipher cipher){
        try {
            byte[] bytes = cipher.doFinal(content);
            return AppUtils.getInstance().getDefaultUtil().decodeBase64(new String(bytes,Charset.forName("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
