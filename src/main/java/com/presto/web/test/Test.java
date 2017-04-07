package com.presto.web.test;

import com.presto.dev.build.ActionBuildField;

/**
 * Created by Administrator on 2017/3/22.
 */
public class Test {

    @ActionBuildField(module="test",app="user",group="add",action="insertinto",name="插入用户",desc="插入用户功能")
    public void testInsert(){

    }
}
