package com.thank.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.types.ObjectId;

public class IDGenerator {
	public static String genId() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		StringBuilder sb=new StringBuilder(sdf.format(new Date()));
		sb.append(ObjectId.get().toHexString());
		return sb.toString();
	}
	public static void main(String[] args) {
		System.out.println(IDGenerator.genId());
		System.out.println("2015-04-21".compareTo(IDGenerator.genId()));
		System.out.println("2015-04-20".compareTo(IDGenerator.genId()));
	}
}
