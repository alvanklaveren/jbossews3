package com.myforum.gameshop;

import java.util.List;

import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.util.lang.Bytes;

import com.myforum.application.DBHelper;
import com.myforum.application.IoLogics;
import com.myforum.application.StringLogics;
import com.myforum.tables.GameConsole;
import com.myforum.tables.Product;
import com.myforum.tables.ProductImage;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductDao;
import com.myforum.tables.dao.ProductImageDao;
import com.myforum.tables.dao.ProductTypeDao;

public class GameShopLogics {

	public GameShopLogics(){}
	
	/*
	 * Returns a list of all the game consoles
	 */
	public static List<GameConsole> getConsoleList(){
		GameConsoleDao gameConsoleDao = new GameConsoleDao();
		return gameConsoleDao.list();
	}

	/*
	 * Returns a list of all the game consoles
	 */
	public static List<ProductType> getProductTypeList(){
		ProductTypeDao productTypeDao = new ProductTypeDao();
		return productTypeDao.list();
	}

	
	/*
	 * Returns a list of products based on arguments console and type, or title
	 */
	public static List<Product> getProductList(int gameConsoleId, int productTypeId, String searchTitle){
		GameConsoleDao gameConsoleDao = new GameConsoleDao();
		ProductTypeDao productTypeDao = new ProductTypeDao();
		ProductDao productDao = new ProductDao();

		List<Product> productList = null;

		// if no title search then search on filter -> gameconsole or producttype
		if(!StringLogics.isEmpty(searchTitle)){
			//search on title
			searchTitle = StringLogics.prepareMessage(searchTitle);
			productList = productDao.list( searchTitle );
			return productList;
		}

		GameConsole gameConsole = gameConsoleDao.find(gameConsoleId);
		ProductType productType = productTypeDao.find(productTypeId);
		if( gameConsole == null && productType == null){
			productList = productDao.list(40 /*Maximum number of result*/ ); // get most recently added products
		}
		if( gameConsole != null && productType == null){	
			productList = productDao.list( gameConsole );
		}		

		if( gameConsole == null && productType != null){	
			productList = productDao.list( productType );
		}		

		if( gameConsole != null && productType != null){	
			productList = productDao.list( gameConsole, productType );
		}
		
		return productList;
	}

	/* 
	 *  Returns an image byte-array based on a particular product.
	 */
	public static byte[] getImage(Product product){
		ProductImage productImage = new ProductImageDao().getPrimaryImage(product);
		byte[] image = null;
		if( productImage != null ){
			image = productImage.getImage();
		}
		return image;
	}

	/*
	 *  Uploads a product image file, given that a valid file was selected
	 */
	public static boolean uploadFile(ListItem<Product> listItem, FileUploadField imageFile){
		final Product product = listItem.getModelObject();
		if( StringLogics.isEmpty( imageFile.getInput() ) ){
			return true; // nothing to do
		}
		
		byte[] imageData = IoLogics.readImageFile( imageFile, "" );
		if(imageData == null){
			return false;
		}
		
		int maxKbSize = 100; // 100 kb
		if(Bytes.kilobytes( maxKbSize ).lessThan( imageData.length )){
				return false;
		}
		if(imageData.length == 0){
			return false;
		}
		ProductImageDao productImageDao = new ProductImageDao();
		ProductImage productImage 		= productImageDao.getPrimaryImage(product);

		if(productImage == null){
			productImage = new ProductImage();
			productImage.setProduct(product);
			productImage.setSortorder(99);
			productImage.setImage(imageData);
			productImageDao.setPrimaryImage(product, productImage);
		}else{
			productImage.setSortorder(99);
			productImage.setImage(imageData);
		}
		DBHelper.saveAndCommit(productImage);

		return true;
	}

	/*
	 *  Delete all images for a given product
	 */
	public static void deleteAllImages(Product product){
		for( ProductImage image:new ProductImageDao().list(product)){
			DBHelper.deleteAndCommit(image);
		}
		DBHelper.deleteAndCommit(product);      				
	}

}
