package com.presto.core.da;

import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/3/18.
 */
public class BaseStatementSetter implements PreparedStatementSetter {

    private BaseStatmentParamter para;

    public BaseStatementSetter(BaseStatmentParamter para) {
        this.para=para;
    }

    @Override
    public void setValues(PreparedStatement ps) throws SQLException {
        for(int i=0;i<para.getValues().size();i++){
            Object value=para.getValues().get(i);
            ps.setObject((i+1),value);
        }
    }
}
