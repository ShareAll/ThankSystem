package com.thank.common.dao;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.mongodb.DB;


public class ImageDaoTest {
	@Test
	public void testSave() throws FileNotFoundException {
		//String file="/Users/fenwang/Documents/IMG_0144.jpg";
		String file="/Users/fenwang/Documents/signup.jpg";
		DB db=MongoUtil.getMongoDb(null, null);
		ImageDao dao=new ImageDao(db);
		dao.save("signup", new FileInputStream(file));
	
	}
	@Test
	public void testLoad() throws FileNotFoundException, IOException {
		String file="/Users/fenwang/Documents/test1.jpg";
		DB db=MongoUtil.getMongoDb(null, null);
		ImageDao dao=new ImageDao(db);
		dao.writeTo("test01_ebay_com", new FileOutputStream(file));
	}
	@Test
	public void testBase64() throws IOException {
		//String file="/Users/fenwang/Documents/test.jpg";
		String file="/Users/fenwang/Documents/IMG_0144.jpg";
		
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		byte[] buf=new byte[4096];
		int len=0;
		FileInputStream f=new FileInputStream(file);
		while((len=f.read(buf))>0) {
			bos.write(buf, 0, len);
		}
		f.close();
		FileWriter fw=new FileWriter("/Users/fenwang/Documents/base.txt");
		fw.write(Base64.encodeBase64String(bos.toByteArray()));
		fw.close();
		System.out.println(Base64.encodeBase64String(bos.toByteArray()));
	}
}
