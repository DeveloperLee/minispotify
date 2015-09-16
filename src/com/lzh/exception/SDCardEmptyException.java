package com.lzh.exception;

public class SDCardEmptyException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SDCardEmptyException(String content){
		super(content);
	}

}
