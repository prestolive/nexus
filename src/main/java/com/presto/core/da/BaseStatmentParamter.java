package com.presto.core.da;

import java.util.ArrayList;
import java.util.List;

public class BaseStatmentParamter {

    private List<Object> values;
	
	public BaseStatmentParamter() {
		values=new ArrayList<Object>();
	}
	
	public void setValue(Object value){
		values.add(value);
	}

	public List<Object> getValues() {
		return values;
	}

}