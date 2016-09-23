package com.mini_orm.dao.database;


import com.mini_orm.dao.utils.ReflexEntity;

import java.util.List;

public interface DeleteInterface {

	public <T> String delete(T t, ReflexEntity reflexEntity);
	
	public <T> String delete(List<T> t, ReflexEntity reflexEntity);
	public <T> String deleteAll(T t, ReflexEntity reflexEntity);


}
