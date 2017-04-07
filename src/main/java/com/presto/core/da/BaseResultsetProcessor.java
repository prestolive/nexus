package com.presto.core.da;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class BaseResultsetProcessor<T extends ValueObject> implements ResultSetExtractor<Object> {

	private Class<?> clz;

	public BaseResultsetProcessor(Class<?> clz) {
		this.clz = clz;
	}

	public List<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int cols = metaData.getColumnCount();
			List<Method> invokes =new ArrayList<Method>();
			List<String> colNames =new ArrayList<String>();
			for (int i = 0; i < cols; i++) {
				String filename=metaData.getColumnName(i + 1).toLowerCase();
				if(filename.equals("dr")){
					continue;
				}
				if(filename.equals("rn")){
					continue;
				}
				colNames.add(filename);
				invokes.add(new PropertyDescriptor(filename, clz).getWriteMethod());
			}
			List<T> data=new ArrayList<T>();
			while(rs.next()){
				ValueObject vo=(ValueObject) clz.newInstance();
				for(int i=0;i<colNames.size();i++){
					Object value=rs.getObject(colNames.get(i));
					if(value==null){
						continue;
					}
					invokes.get(i).invoke(vo, value);
				}
				data.add((T) vo);
			}
			if(data.size()>0){
				return data;
			}
			return null;

		} catch (Exception e) {
			throw new SQLException("Bean get set 错误", e);
		}
	}

}
