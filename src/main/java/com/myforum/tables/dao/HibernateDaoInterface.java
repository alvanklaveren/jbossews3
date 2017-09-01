package com.myforum.tables.dao;

import java.util.List;

public interface HibernateDaoInterface<E, K> {

	boolean add(E entity);
	
	boolean update(E entity);
	
	boolean remove(E entity);
	
	E find(K key);
	
	List<E> list();
}