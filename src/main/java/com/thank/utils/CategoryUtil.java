package com.thank.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.thank.common.dao.CategoryDao;
import com.thank.common.model.HelpCategory;

public class CategoryUtil {
	private static CategoryUtil _instance=null;
	CategoryDao dao=new CategoryDao(null,null,HelpCategory.class);
	long MAX_ID=1l;
	LinkedHashMap<String,HelpCategory> nameMap=new LinkedHashMap<String,HelpCategory>();
	
	private CategoryUtil() {
		List<HelpCategory> categories=dao.getAllCategories();
		for(HelpCategory item:categories) {
			MAX_ID=Math.max(MAX_ID,item.id);
			nameMap.put(item.name, item);
		}
	}
	public static CategoryUtil instance() {
		if(_instance==null) {
			synchronized(CategoryUtil.class) {
				if(_instance==null) {
					_instance=new CategoryUtil();
				}
			}
		}
		return _instance;
	}
	public HelpCategory getCategoryId(String categoryName) {
		return nameMap.get(categoryName);
	}
	public List<HelpCategory> getAvailableCategories() {
		Set<String> names=new TreeSet(nameMap.keySet());
		List<HelpCategory> ret=new ArrayList<HelpCategory>();
		for(String n:names) {
			ret.add(nameMap.get(n));
		}
		return ret;
	}
	public long createCategory(String categoryName) {
		HelpCategory newCat=new HelpCategory();
		MAX_ID=MAX_ID+1;
		newCat.id=MAX_ID;
		newCat.name=categoryName;
		dao.save(newCat);
		nameMap.put(newCat.name, newCat);
		return newCat.id;
	}
}
