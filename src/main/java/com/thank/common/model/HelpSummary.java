package com.thank.common.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

@Entity("HelpSummary")
public class HelpSummary implements Serializable {
	private static final long serialVersionUID = 8452984685742656974L;
	public @Id String id;
	public @Indexed String owner;
	public String title;
	public int completeness=0;
	public Set<String> subscribers=new HashSet<String>();
	public int comments=0;
}
