package com.presto.core.da;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */
@Repository
public class BaseDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BaseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private SimpleDateFormat timeStampFormat;

    public String getTimeStamp() {
        if (timeStampFormat == null) {
            timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return timeStampFormat.format(new Date());
    }

    public String insertOrUpdateValueObject(ValueObject vo) throws Exception {
        boolean isNew= vo.getState() == 1;
        BaseStatmentBean statementBean = null;
        switch (vo.getState()){
            case 0:
                throw new Exception("status error");
            case 1:{
                if(vo.getId()==null||vo.getId().trim().length()==0){
                    vo.setId(GenerateID.getInstance().generateShortUuid());
                }
                statementBean = generateInsertVoStatment(vo);
                break;
            }
            case 2: {
                statementBean = generateUpdateVoStatment(vo);
                break;
            }
        }

        int updates = executeStatment(statementBean);
        return vo.getId();
    }

    private int executeStatment(BaseStatmentBean statementBean){
        return jdbcTemplate.update(statementBean.getSql().toString(), new BaseStatementSetter(statementBean.getPara()));
    }

    private BaseStatmentBean generateInsertVoStatment(ValueObject vo) throws Exception {
        String[] fields = vo.getFields();
        BaseStatmentBean sb=new BaseStatmentBean();
        sb.getSql().append(" insert into ").append(vo.getTableName());
        sb.getSql().append("(");
        StringBuilder valueSQL = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            //拼sql
            if (i > 0) {
                sb.getSql().append(",");
                valueSQL.append(",");
            }
            sb.getSql().append(fields[i]);
            valueSQL.append("?");
            //value
            sb.getPara().setValue(getBeanValueToDB(vo,fields[i]));
        }
        // ts
        sb.getSql().append(",ts");
        valueSQL.append(",?");
        sb.getSql().append(")");
        sb.getSql().append(" values (").append(valueSQL).append(")");
        sb.getPara().setValue(getTimeStamp());
        return sb;
    }

    private BaseStatmentBean generateUpdateVoStatment(ValueObject vo) throws Exception {
        String[] fields = vo.getFields();
        BaseStatmentBean sb=new BaseStatmentBean();
        sb.getSql().append(" update ").append(vo.getTableName()).append(" set ");
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) {
                sb.getSql().append(",");
            }
            sb.getSql().append(fields[i]).append("=?");
            //value
            sb.getPara().setValue(getBeanValueToDB(vo,fields[i]));
        }
        sb.getSql().append(",ts=?");
        sb.getSql().append(" where id=?");
        sb.getPara().setValue(getTimeStamp());
        sb.getPara().setValue(vo.getId());
        return sb;
    }

    public <T extends ValueObject> T queryValueObject(Class clz,String id) throws Exception{
        String sql = generateValueObjectSQL(clz);
        ValueObject vo;
        BaseStatmentParamter para;
        try {
            vo = (ValueObject) clz.newInstance();
            sql+=" where id = ? and coalesce(dr,0)=0";
            para =new BaseStatmentParamter();
            para.setValue(id);
        } catch (InstantiationException e) {
            throw new Exception(e);
        } catch (IllegalAccessException e) {
            throw new Exception(e);
        }
        Collection<ValueObject> coll=queryBeans(sql, clz, para);
        if(coll!=null&&coll.size()>0){
            ValueObject bean = coll.iterator().next();
            return (T) bean;
        }
        return null;
    }
    /**
     * saaaadfsdfs
     * @param clz ������
     * @return ���� list����
     * @throws Exception
     */
    public <T extends ValueObject> List<T>  queryValueObjectForList(Class clz) throws Exception{
        String sql = generateValueObjectSQL(clz);
        ValueObject vo;
        BaseStatmentParamter para;
        try {
            vo = (ValueObject) clz.newInstance();
            sql+=" where 1=1 and coalesce(dr,0)=0";
        } catch (InstantiationException e) {
            throw new Exception(e);
        } catch (IllegalAccessException e) {
            throw new Exception(e);
        }
        Collection<ValueObject> coll=queryBeans(sql, clz, null);
        if(coll!=null&&coll.size()>0){
            return (List<T>) coll;
        }
        return null;
    }

    private <T extends ValueObject> List<T> queryBeans(String sql, Class clz, BaseStatmentParamter para) throws Exception {
        if (para != null) {
            return (List<T>) jdbcTemplate.query(sql, new BaseStatementSetter(para), new BaseResultsetProcessor(clz));
        } else {
            return (List<T>) jdbcTemplate.query(sql, new BaseResultsetProcessor(clz));
        }
    }


    private String generateValueObjectSQL(Class clz) throws Exception {
        ValueObject vo;
        try {
            vo = (ValueObject) clz.newInstance();
            StringBuilder sql = new StringBuilder();
            sql.append(" select ");
            String[] attrs = vo.getFields();
            for (int i = 0; i < attrs.length; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append(attrs[i]);
            }
            sql.append(",ts");
            sql.append(" from ").append(vo.getTableName());
            return sql.toString();
        } catch (InstantiationException e) {
            throw new Exception(e);
        } catch (IllegalAccessException e) {
            throw new Exception(e);
        }
    }

    private Object getBeanValueToDB(ValueObject vo, String field) throws Exception{
        Object value=null;
        PropertyDescriptor pd = new PropertyDescriptor(field, vo.getClass());
        Method getter = pd.getReadMethod();
        if(getter!=null){
            value=getter.invoke(vo);
        }
        return value;
    }

}
