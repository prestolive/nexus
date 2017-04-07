package com.presto.sys.operate;

import com.presto.core.da.ValueObject;

/**
 * Created by Administrator on 2017/3/20.
 */
public class SysOperateVO extends ValueObject {

    private String vcode;

    private String vname;

    private String vpassword;

    @Override
    public String getTableName() {
        return "sys_operate";
    }

    @Override
    public String[] getFields() {
        return new String[]{"id","vcode","vname","vpassword"};
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    public String getVpassword() {
        return vpassword;
    }

    public void setVpassword(String vpassword) {
        this.vpassword = vpassword;
    }
}
