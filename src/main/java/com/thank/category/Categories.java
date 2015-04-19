package com.thank.category;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Categories {
	
	private static final Map<String, Integer> categories = new HashMap<String, Integer>(30);
	//TODO will move toDB later.
	static{
		categories.put("Techology", 1);
		categories.put("Science", 2);
		categories.put("Books", 3);
		categories.put("Business", 4);
		categories.put("Movies", 5);
		categories.put("Travel", 6);
		
		categories.put("Health", 7);
		categories.put("Music", 8);
		categories.put("Education", 9);
		categories.put("Food", 10);
		categories.put("Design", 11);
		categories.put("Psychology", 12);
		
		categories.put("Economics", 13);
		categories.put("Cooking", 14);
		categories.put("History", 15);
		categories.put("Writing", 16);
		categories.put("Spots", 17);
		categories.put("Photography", 18);
		
		
		categories.put("Philosophy", 19);
		categories.put("Marketing", 20);
		categories.put("Finance", 21);
		categories.put("Mathematics", 22);
		categories.put("Literature", 23);
		categories.put("Fashion and Style", 24);
		
		categories.put("Politics", 25);
		categories.put("Television Series", 26);
		categories.put("Fine Art", 27);
		categories.put("Journalism", 28);

	}
	
	public static Map<String, Integer> getCategories(){
		Map<String, Integer> sm = new TreeMap<String, Integer>();
		sm.putAll(categories);
		return sm;
	}
	
	public static void main(String[] args){
		System.out.println(getCategories().toString());
	}
}
