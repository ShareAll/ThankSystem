package com.thank.common.model;

import java.util.ArrayList;
import java.util.List;

public enum ReminderEnum {
	DAILY(1,"Daily"),
	WEEKLY(2,"Weekly"),
	MONTHLY(3,"Monthly"),
	QUARTERLY(4,"Quarterly"),
	YEARLY(5,"Yearly");
	private int key;
	private String desc;
	
	private ReminderEnum(int key, String desc){
		this.key = key;
		this.desc = desc;
	}
	
	
	public static ReminderEnum getEnum(int value) {
		switch (value){
		case 1 : 
			return DAILY;
		case 2:
			return WEEKLY ;
		case 3:
			return  MONTHLY;
		case 4:
			return QUARTERLY;
		case 5:
			return YEARLY;
		}
		//Default is weekly
		return WEEKLY;
	}
	
	public int getKey(){
		return key;
	}
	
	public String getDesc(){
		return desc;
	}
	
	
	public static List<ReminderEnum> getReminderEnums(){
		List<ReminderEnum> list = new ArrayList<ReminderEnum>();
		list.add(DAILY);
		list.add(WEEKLY);
		list.add(MONTHLY);
		list.add(QUARTERLY);
		list.add(YEARLY);
		return list;
	}
	

	

}
