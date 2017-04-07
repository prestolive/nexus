package com.presto.core.da;

/**
 * Created by Administrator on 2017/3/18.
 */
public class BaseStatmentBean {

    private StringBuilder sql;

    private BaseStatmentParamter para;

    public BaseStatmentBean() {
        this.sql = new StringBuilder();
        this.para = new BaseStatmentParamter();
}

    public StringBuilder getSql() {
        return sql;
    }

    public BaseStatmentParamter getPara() {
        return para;
    }

}
