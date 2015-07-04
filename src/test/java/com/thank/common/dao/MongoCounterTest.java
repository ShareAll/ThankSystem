package com.thank.common.dao;

import org.junit.Test;

import com.thank.common.model.ClaimableTask;
import com.thank.common.model.Counter;

public class MongoCounterTest {

	@Test
	public void testGetNext() {
		MongoCounter c=new MongoCounter(null,null,Counter.class);
		long x=c.getNext("fenwang");
		System.out.println(x);
	}
}
