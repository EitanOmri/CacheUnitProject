package com.hit.dao;

import java.io.Serializable;

public interface IDao<ID extends Serializable, T> {

	void delete(T entity);

	T find(ID id);

	void save(T entity);
}
