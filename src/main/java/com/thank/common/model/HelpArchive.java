package com.thank.common.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;

@Entity("HelpArchive")
public class HelpArchive extends HelpSummary {
	
	private static final long serialVersionUID = -520902437324011814L;
	public HelpArchive() {
		
	}
	public HelpArchive(HelpSummary vo) {
		this.categoryId=vo.categoryId;
		this.comments=vo.comments;
		this.owner=vo.owner;
		this.subscribers=vo.subscribers;
		this.privacy=vo.privacy;
		this.title=vo.title;
	}
	public String conclusion;
	public @Indexed Date archiveTime=new Date();
}
