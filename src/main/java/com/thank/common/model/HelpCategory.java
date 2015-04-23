package com.thank.common.model;

import java.io.Serializable;
import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
@Entity("HelpCategory")
public class HelpCategory implements Serializable{

	private static final long serialVersionUID = 2753561022812570376L;
	
	public @Id long id;
	public @Indexed(unique=true) String name;
	public Date createTime=new Date();
}
