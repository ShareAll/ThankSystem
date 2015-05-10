package com.thank.common.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mongodb.DB;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class ImageDao {
	private DB db;
	public static String IMAGE_TABLE="photo";
	public ImageDao(DB db) {
		this.db=db;
	}
	
	public void save(String key,InputStream in) {
		GridFS photo = new GridFS(db, IMAGE_TABLE);
		GridFSInputFile fInput=photo.createFile(in);
		fInput.setFilename(key);
		fInput.save();
	}
	public boolean writeTo(String key, OutputStream out) throws IOException {
		GridFS photo = new GridFS(db, IMAGE_TABLE);
		GridFSDBFile imageForOutput = photo.findOne(key);
		imageForOutput.writeTo(out);
		return true;
	}
}
