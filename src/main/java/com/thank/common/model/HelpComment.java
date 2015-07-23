package com.thank.common.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity("HelpComment")
public class HelpComment implements Serializable {
	private static final long serialVersionUID = 4095458934651927857L;
	public @Id String id;
	public @Indexed long pos;
	public @Indexed String helpId;
	public String content;
	public Set<String> voted=new HashSet<String>();
	//public boolean voted=false;
	public @Indexed Date createTime=new Date();
	public String owner;
	public String ownerName;
	
}
