package top.waws.library.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import top.waws.library.AppUtils;

/**
 *  功能描述: 日期工具类
 *  @className: DateUtil
 *  @author: thanatos
 *  @createTime: 2017/11/24
 *  @updateTime: 2017/11/24 上午9:02
 */
public class DateFormatUtil {

    private DateFormatUtil(){}

    private static final class Inner{
        private static final DateFormatUtil DATE_FORMAT_UTIL = new DateFormatUtil();
    }

    public static DateFormatUtil getInstance(){
        return Inner.DATE_FORMAT_UTIL;
    }

    /**
     * 日期区间获取类
     * @return 0：源日期列表 1：显示日期列表（从开始日期到结束日期，）
     */
    public Object[] getDateRes(int yearCount){
        Date start = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdfe = new SimpleDateFormat("EEEE",Locale.CHINA);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            calendar.add(Calendar.YEAR,yearCount);
            Date dEnd = calendar.getTime();
            List<Date> listDate = getDatesBetweenTwoDate(start, dEnd);
            List<String> res = new ArrayList<>();
            for(int i=0;i<listDate.size();i++){
                String msg = sdf.format(listDate.get(i))+"-"+sdfe.format(listDate.get(i));
                String[] arr = msg.split("-");
                arr[3] = arr[3].replace("星期","周");
                res.add(arr[1]+"月"+arr[2]+"日 "+arr[3]);
            }
            return new Object[]{listDate,res};
        } catch (Exception e) {
            e.printStackTrace();
            return new Object[2];
        }
    }


    /**
     * 获取时间回调函数
     */
    public static class DataResCallBack{

        public void noNet(){}
        /**
         * 从现在开始到两年后的 时间原值 和 格式化后的集合
         * @param src 源数据
         * @param res 格式化后的数据
         */
        public void next(List<Date> src, List<String> res){}

        /**
         * 当前时间
         * @param time 时间戳
         * @param format 格式化后的时间
         */
        public void currentTime(long time, String format){}
    }


    /**
     * 日期区间获取类
     * @param yearCount 几年
     * @return 0：源日期列表 1：显示日期列表（从开始日期到结束日期，）
     */
    public void getNetDateRes(String baseUrl,int yearCount,final DataResCallBack callBack){
        if (!AppUtils.getInstance().isConnectedNet()){
            callBack.next(new ArrayList<Date>(),new ArrayList<String>());
            callBack.noNet();
            return;
        }
        try {
            Observable.create(new ObservableOnSubscribe<Long>() {
                @Override
                public void subscribe(ObservableEmitter<Long> e) throws Exception {
                    URLConnection uc = new URL(baseUrl).openConnection();
                    uc.connect();
                    e.onNext(uc.getDate());
                }
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long o) throws Exception {
                    Date start = new Date(o);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    SimpleDateFormat sdfe = new SimpleDateFormat("EEEE",Locale.CHINA);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start);
                    calendar.add(Calendar.YEAR,yearCount);
                    Date dEnd = calendar.getTime();
                    callBack.currentTime(o,getDateFormat("yyyy-MM-dd HH:mm:ss",o));
                    AppUtils.getInstance().logd("网络时间: "+start.toString());
                    List<Date> listDate = getDatesBetweenTwoDate(start, dEnd);
                    List<String> res = new ArrayList<>();
                    for(int i=0;i<listDate.size();i++){
                        String msg = sdf.format(listDate.get(i))+"-"+sdfe.format(listDate.get(i));
                        String[] arr = msg.split("-");
                        arr[3] = arr[3].replace("星期","周");
                        res.add(arr[1]+"月"+arr[2]+"日 "+arr[3]);
                    }
                    callBack.next(listDate,res);
                }
            });
        }catch (Exception e){
            callBack.next(new ArrayList<Date>(),new ArrayList<String>());
        }
    }

    /**
     * 获取格式化后的date
     * @param date 格式化前的date
     * @return 标准的date字符串
     */
    public String getDateFormat(String date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        try {
            return sf.format(sf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取格式化后的date
     * @param time 时间戳
     * @return 标准的date字符串
     */
    public String getDateFormat(String format, long time){
        SimpleDateFormat sf = new SimpleDateFormat(format,Locale.CHINA);
        try {
            Date date = new Date();
            date.setTime(time);
            return sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return List
     */
    private List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        while (true) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }


    /**
     * 判断前者时间是否在后者时间之前
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 0： 开始时间在结束时间之前 1：开始时间在结束时间之后 2：两个时间相等
     */
    public int isBeginBeforeEnd(String beginTime, String endTime){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        try {
            long start = sf.parse(beginTime).getTime();
            long end = sf.parse(endTime).getTime();
            if (start <end){
                return 0;
            }else if (start == end){
                return 2;
            }else {
                return 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * 传入一个时间戳 返回：格式化的日期
     * @param longTime  时间戳
     * @return 字符串格式化的日期
     */
    public String parse(@NonNull Object longTime) {
        try {
            long time = Long.valueOf(String.valueOf(longTime));
            Calendar target = Calendar.getInstance();
            target.setTimeInMillis(time);
            Calendar curr = Calendar.getInstance();
            if (curr.get(Calendar.YEAR) == target.get(Calendar.YEAR)){//今年

                int offset = day(time);
                if (offset == 0){//今天
                    double d = System.currentTimeMillis()/(1000*60*60.0) - time/(1000*60*60.0);
                    if (d >= 1.0 && d <= 24.0){
                        return (int)d+"小时前";
                    }else if (d < 1){
                        int s = (int)(d*60);
                        if (s <= 1){
                            return "刚刚";
                        }else {
                            return s+"分钟前";
                        }
                    }
                }else if (offset == 1){//昨天
                    return "昨天"+formatQuickly("HH:mm", time);
                }else if (offset == 2){//前天
                    return "前天"+formatQuickly("HH:mm", time);
                }else {//更早
                    return formatQuickly("MM-dd HH:mm", time);
                }
            } else{//不是今年
                return formatQuickly("yyyy-MM-dd HH:mm", time);
            }
        }catch (Exception e){
            return String.valueOf(longTime);
        }
        return String.valueOf(longTime);
    }

    /**
     * 获取时间戳
     * @param formatData 格式化的时间
     * @return
     */
    public String getTimeStamp( String formatData) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
        try {
            long time = sf.parse(formatData).getTime();
            return String.valueOf(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 判断 今天 昨天  前天  更早 0  ， 1  ，2， 3
     * @param timeStamp
     * @return  今天 昨天  前天  更早 0  ， 1  ，2， 3
     */
    public int day(long timeStamp) {
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if(timeStamp >= todayStartMillis) {
            return 0;
        }
        int oneDayMillis = 24 * 60 * 60 * 1000;
        long yesterdayStartMilis = todayStartMillis - oneDayMillis;
        if(timeStamp >= yesterdayStartMilis) {
            return 1;
        }
        long yesterdayBeforeStartMilis = yesterdayStartMilis - oneDayMillis;
        if(timeStamp >= yesterdayBeforeStartMilis) {
            return 2;
        }
        return  3;
    }

    /**
     * 格式化时间
     * @param format 格式
     * @param time 时间戳
     * @return
     */
    public String formatQuickly(String format, long time){
        try {
            Date date = new Date(time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format,Locale.CHINA);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
