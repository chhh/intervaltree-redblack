package com.ixee.datastruct;

//WAT

public class LLNode{
	LLNode next;
	LLNode prev;

	public LLNode pop(){
		return this;
	}
	public void push(LLNode n){
		n.setNext(this);
	}
	public void setNext(LLNode n){
		next = n;
	}
	public void setPrev(LLNode p){
		prev = p;
	}
	public LLNode next(){
		return next;
	}
}