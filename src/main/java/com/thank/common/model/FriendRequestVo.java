package com.thank.common.model;

import java.io.Serializable;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("FriendRequest")
public class FriendRequestVo implements Serializable {
	private static final long serialVersionUID = -8007610831548581540L;
	public @Id String id;
	public String requester;
	public String requester_name;
	public String target;
	
}
