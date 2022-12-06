package com.example.myannprocessor.myLog;

import java.lang.annotation.*;

/**
 * 任何的实现类中加入此注解编译过后会生成Logger log = LogFactory.getLogger("xxxxx");
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface MyLog {
}
