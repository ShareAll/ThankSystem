package com.thank.common.dao;

import java.util.List;

import org.junit.Test;

import com.thank.common.model.HelpCategory;
import com.thank.utils.CategoryUtil;

public class CategoryUtilTest {
	@Test
	public void testGetAllCategories() {
		List<HelpCategory> set=CategoryUtil.instance().getAvailableCategories();
		for(HelpCategory item:set) {
			if(item==null) throw new RuntimeException("wrong");
			System.out.println(item.name);
		}
	}
}	
