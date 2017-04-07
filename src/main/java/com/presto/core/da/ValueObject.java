package com.presto.core.da;

/**
 * Created by Administrator on 2017/3/13.
 */
public abstract class ValueObject {

    public abstract String getTableName();

    public abstract String[] getFields();

    //vo状�?�不通过ID是否为空决定，�?�过state决定
    //0 默认状�??  1 新增状�?? 2 修改状�?? 3 删除状�??

    private String id;

    private int state;

    private String ts;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id=id;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state=state;
    }

    public void setTs(String ts){
        this.ts=ts;
    }

    public String getTs(){
        return ts;
    }

}
