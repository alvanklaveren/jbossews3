package com.myforum.base;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.util.lang.Bytes;

import com.myforum.application.DBHelper;
import com.myforum.application.IoLogics;
import com.myforum.application.StringLogics;
import com.myforum.tables.Classification;
import com.myforum.tables.Message;
import com.myforum.tables.MessageImage;
import com.myforum.tables.dao.ClassificationDao;
import com.myforum.tables.dao.MessageImageDao;

public class ForumLogics {
	
	public ForumLogics(){}
	
	public static List<Classification> getClassifications(Classification currentClassification, boolean shouldGetAll){
		List<Classification> classifications; // ArrayLists are serializable, Lists are not!

		// if shouldGetAll, then return all the classifications in the list
		if(shouldGetAll){
			// allow administrators to change the classification of a user
			classifications = (List<Classification>) new ClassificationDao().list();		
		}else{
			// only add the current forum user's classification to the list
			classifications = (List<Classification>) Arrays.asList( currentClassification );
		}
		return classifications;
	}	
	
	/* 
	 *  Returns an image byte-array based on a particular message.
	 */
	public static byte[] getImage(Message message, int index){
		
		MessageImage messageImage = new MessageImageDao().getImage(message, index);
		byte[] image = null;

		if( messageImage != null ){
			image = messageImage.getImage();
		}
		
		return image;
	}

	/* 
	 *  Returns an image byte-array based on a primary key in MESSAGE_IMAGE 
	 */
	public static byte[] getImage(int code){
		
		MessageImage messageImage = new MessageImageDao().getImageByCode(code);
		byte[] image = null;

		if( messageImage != null ){
			image = messageImage.getImage();
		}
		
		return image;
	}

	/*
	 *  Uploads a message image file
	 */
	public static MessageImage uploadImage(FileUploadField imageFile){
			
		if( StringLogics.isEmpty( imageFile.getInput() ) ){
			return null; // nothing to do
		}
		
		byte[] imageData = IoLogics.readImageFile( imageFile, "" );
		if(imageData == null){
			return null;
		}
		
		int maxKbSize = 100; // 100 KB
		if(Bytes.kilobytes( maxKbSize ).lessThan( imageData.length )){
				return null;
		}
		if(imageData.length == 0){
			return null;
		}

		MessageImage messageImage = new MessageImage();

		messageImage.setMessage(null);
		messageImage.setSortorder(99);
		messageImage.setImage(imageData);

		messageImage = (MessageImage) DBHelper.saveAndCommit(messageImage);

		return messageImage;
	}

}
