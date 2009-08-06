package org.jalgo.module.lambda.controller;

public class LambdaException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int index;
	
	public LambdaException(){}
	public LambdaException(String msg){
		super(msg);
	}
	public LambdaException(String msg, int index){
		super(msg);
		this.index=index;
	}
	public int getIndex(){
		return this.index;
	}

}
