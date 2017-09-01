package com.myforum.tables;

import java.io.Serializable;

public abstract class AVKTable implements Serializable{
	private static final long serialVersionUID = 1L;

	/*
	 *  returns the primary key (code) from the table. This does not necessarily have to be named "code" 
	 */
	public abstract int getCode();
}
