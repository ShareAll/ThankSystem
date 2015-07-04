package com.thank.common.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Id;

public class Counter implements Serializable{
	private static final long serialVersionUID = 3694347239828691630L;
	public @Id String key;
	public long seq=0l;
	public Counter(){	
	}
	public Counter(String key,long seq){
		this.key=key;
		this.seq=seq;
	}
}
