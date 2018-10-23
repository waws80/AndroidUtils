package top.waws.library.activitystack;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.IntDef;

import static top.waws.library.activitystack.ActivityStackUtil.ACTIVITY_START;
import static top.waws.library.activitystack.ActivityStackUtil.ACTIVITY_STOP;
import static top.waws.library.activitystack.ActivityStackUtil.ACTIVITY_UNKNOW;

/**
 * activity状态注解
 */
@IntDef({ACTIVITY_START,ACTIVITY_STOP,ACTIVITY_UNKNOW})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.SOURCE)
public @interface ActivityStatus{}
