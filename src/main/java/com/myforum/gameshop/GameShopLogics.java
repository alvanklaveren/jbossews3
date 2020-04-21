package com.myforum.gameshop;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public static List<Product> getProductList(int gameConsoleId, int productTypeId, String searchTitle, ESortOrder sortOrder){
		GameConsoleDao gameConsoleDao = new GameConsoleDao();
		ProductTypeDao productTypeDao = new ProductTypeDao();
		ProductDao productDao = new ProductDao();

		List<Product> productList = null;

		// if no title search then search on filter -> gameconsole or producttype
		if(!StringLogics.isEmpty(searchTitle)){
			//search on title
			searchTitle = StringLogics.prepareMessage(searchTitle);
			productList = productDao.search( searchTitle );
			return productList;
		}

		GameConsole gameConsole = gameConsoleDao.find(gameConsoleId);
		ProductType productType = productTypeDao.find(productTypeId);
		if( gameConsole == null && productType == null){
			productList = productDao.list(24 /*Maximum number of result*/ ); // get most recently added products
			return productList; // do not sort.. we want the last entries to show up first
		}
		if( gameConsole != null && productType == null){	
			productList = productDao.list( gameConsole, sortOrder );
		}		

		if( gameConsole == null && productType != null){	
			productList = productDao.list( productType, sortOrder );
		}		

		if( gameConsole != null && productType != null){	
			productList = productDao.list( gameConsole, productType, sortOrder );
		}
				
		productList = sortProductList(productList, sortOrder);
		
		return productList;
	}
	
	public static List<Product> sortProductList(List<Product> defaultList, ESortOrder sortOrder) {
		
    	final Map<String, Integer> versionMap = new HashMap<>();
    	final Map<String, String> shortNameMap = new HashMap<>();
		
    	for(Product product: defaultList) {

    		Integer version = 0;
    		String  shortName = product.getName();
    		
    		String productName = product.getName();
    		String productNameAlt = StringLogics.convertVersionNumbers(product.getName());
    		
    		int diffIndex = StringLogics.indexOfDifference(productName, productNameAlt);
    		if(diffIndex >= 0) {
				shortName = productNameAlt.substring(0,diffIndex);

				try {
    				version = Integer.parseInt(productNameAlt.substring(diffIndex, diffIndex + 2)); // version > 10
    			} catch(Exception e) {
        			try {
        				version = Integer.parseInt(productNameAlt.substring(diffIndex, diffIndex + 1)); // version < 10
        			} catch(Exception e2) {
        				version = 0; // makes sure all version-less products with same name are sorted on name only
        			}
    			}
    		}
    		
    		versionMap.put(product.getName(), version);
    		shortNameMap.put(product.getName(), shortName);
    	}
    	
    	if(sortOrder.equals(ESortOrder.AZ)) {
    	
		    Collections.sort(defaultList, new Comparator<Product>() {
		    	@Override
		        public int compare(Product p1, Product p2) {
		    		
		    		int p1Version = versionMap.get(p1.getName());
		    		int p2Version = versionMap.get(p2.getName());
		    		String p1Short = shortNameMap.get(p1.getName());
		    		String p2Short = shortNameMap.get(p2.getName());
		    		    		
		    		if(p1Version == 0 && p2Version > 0 && p1Short.equals(p2Short)) {
		    			return 1;
		    		}
	
		    		if(p1Version > 0 && p2Version == 0 && p1Short.equals(p2Short)) {
		    			return -1;
		    		}
		    		
		    		if(p1Version > 0 && p2Version > 0 && p1Short.equals(p2Short)) {
		    			return p1Version - p2Version;
		    		}
	
		    		return StringLogics.convertVersionNumbers(p1.getName()).compareTo(StringLogics.convertVersionNumbers(p2.getName()));
		        }
		    });
    	} else if(sortOrder.equals(ESortOrder.ZA)) {
    		
		    Collections.sort(defaultList, new Comparator<Product>() {
		    	@Override
		        public int compare(Product p1, Product p2) {
		    		
		    		int p1Version = versionMap.get(p1.getName());
		    		int p2Version = versionMap.get(p2.getName());
		    		String p1Short = shortNameMap.get(p1.getName());
		    		String p2Short = shortNameMap.get(p2.getName());
		    		    		
		    		if(p1Version == 0 && p2Version > 0 && p1Short.equals(p2Short)) {
		    			return 1;
		    		}
	
		    		if(p1Version > 0 && p2Version == 0 && p1Short.equals(p2Short)) {
		    			return -1;
		    		}
		    		
		    		if(p1Version > 0 && p2Version > 0 && p1Short.equals(p2Short)) {
		    			return p1Version - p2Version;
		    		}
	
		    		return StringLogics.convertVersionNumbers(p2.getName()).compareTo(StringLogics.convertVersionNumbers(p1.getName()));
		        }
		    });   		
    	}
	    
	    return defaultList;
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
		// what you are about to see is a manual "ON CASCADE DELETE"... I do not know why I chose to do so, but there it is
		for( ProductImage image:new ProductImageDao().list(product)){
			DBHelper.deleteAndCommit(image);
		}
		// the below product will no longer contain the link to the images in product, and will delete nicely. 
		// this would not have worked on the product in the arguments, because it still has a reference to the images we just deleted.
		Product toBeDeleted = new ProductDao().find(product.getCode());
		DBHelper.deleteAndCommit(toBeDeleted);
	}
}
