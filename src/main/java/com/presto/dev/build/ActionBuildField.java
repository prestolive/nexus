package com.presto.dev.build;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017/3/22.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented//action生成注释
public @interface ActionBuildField {

    String module();

    String app();

    String group();

    String action();

    String name();

    String desc();

}
