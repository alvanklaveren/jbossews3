package com.myforum.gameshop.REST;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.google.gson.Gson;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.gameshop.GameShopLogics;
import com.myforum.springframework.GameShopConsoleService;
import com.myforum.springframework.GameShopProductTypeService;
import com.myforum.springframework.GameShopService;
import com.myforum.tables.GameConsole;
import com.myforum.tables.Product;
import com.myforum.tables.ProductType;
import java.io.*;
import java.util.zip.*;
import java.nio.charset.*;


public class GameShopMobile extends WebPage{
	private static final long serialVersionUID = 1L;
	
	private int 		consoleId, typeId;
	
	private String 		title;
	
	private final int 	CONSOLELIST 	= -1;
	private final int 	PRODUCTTYPELIST = -1;
	
	@SpringBean
	private GameShopService 		gameShopService;
	
	@SpringBean
	private GameShopConsoleService	gameShopConsoleService;

	@SpringBean
	private GameShopProductTypeService	gameShopProductTypeService;

	// uses https://www.alvanklaveren.com/gameshopmobile/0/0
	public GameShopMobile(final PageParameters params){
		consoleId 		= ForumUtils.getParmInt(params,    "console",        0);
		typeId 			= ForumUtils.getParmInt(params,    "type",           0);
		title			= ForumUtils.getParmString(params, "searchtitle",   "");
	}

	@Override
    public final void renderPage() {

        WebResponse response = (WebResponse) getResponse();
        response.setContentType("text/plain");
        
		if(consoleId == CONSOLELIST){
			List<String> consoleList = createConsoleJsonList();
			response.write( concatJson(consoleList) );
		}else if(typeId == PRODUCTTYPELIST){
			List<String> productTypeList = createProductTypeJsonList();
			response.write( concatJson(productTypeList) );
		}else{
			List<String> productJsonList = createProductJsonList(title);
			response.write( concatJson(productJsonList) );
		}
    }


	private List<String> createProductJsonList(String title){
		List<String> productJsonList = new ArrayList<String>();
		
		// to prevent a retrieve of all, disable typeId while retrieving consoleId 0, which is recently added
		List<Product> productList = null;
		if(consoleId == 0){
			productList = gameShopService.getProducts(consoleId, 0, title);
		}else{
			productList = gameShopService.getProducts(consoleId, typeId, title);
		}

		//GsonBuilder gsonBuilder = new GsonBuilder();
		//gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		//Gson gson = gsonBuilder.create();
		Gson gson = new Gson();
		
		productJsonList.clear();
		
		
		for(Product product:productList){
			GsonProduct gsonProduct = new GsonProduct();
			
			// strip the rating website links from description 
			String productDescription = product.getDescription();
			
			if(productDescription.indexOf("[h:") > 0 ){
				productDescription = productDescription.substring(0, productDescription.indexOf("[h:"));
			}

			productDescription = StringLogics.prepareMessage(productDescription);
			
	    	gsonProduct.setCode(product.getCode());
	    	gsonProduct.setName(product.getName());
	    	gsonProduct.setDescription(productDescription);
	    	gsonProduct.setCompany(product.getCompany().getDescription());
	    	gsonProduct.setGameConsole(product.getGameConsole().getDescription());
	    	gsonProduct.setProductType(product.getProductType().getDescription());
	    	gsonProduct.setProductImage( compress( GameShopLogics.getImage(product) ) );

	    	String productJson = gson.toJson(gsonProduct);
	    	productJsonList.add(productJson);	
	    }	
		return productJsonList;
	}

	private List<String> createConsoleJsonList(){
		List<String> consoleJsonList = new ArrayList<String>();
		List<GameConsole> consoleList = gameShopConsoleService.getConsoles();
		
		//GsonBuilder gsonBuilder = new GsonBuilder();
		//gsonBuilder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
		//Gson gson = gsonBuilder.create();	
		Gson gson = new Gson();

		consoleJsonList.clear();
					
		for(GameConsole console:consoleList){
			GsonProduct gsonProduct = new GsonProduct();
			
	    	gsonProduct.setCode(console.getCode());
	    	gsonProduct.setDescription(console.getDescription());

	    	String productJson = gson.toJson(gsonProduct);
	    	consoleJsonList.add(productJson);	
	    }	
		return consoleJsonList;
	}

	private List<String> createProductTypeJsonList(){
		List<String> productTypeJsonList = new ArrayList<String>();
		List<ProductType> productTypeList = gameShopProductTypeService.getProductTypes();
		
		Gson gson = new Gson();

		productTypeJsonList.clear();

		GsonProduct gsonProduct = new GsonProduct();
    	gsonProduct.setCode(0);
    	gsonProduct.setDescription("All");
    	String productTypeJson = gson.toJson(gsonProduct);
    	productTypeJsonList.add(productTypeJson);
    	
		for(ProductType productType:productTypeList){
			gsonProduct = new GsonProduct();
			
	    	gsonProduct.setCode(productType.getCode());
	    	gsonProduct.setDescription(productType.getDescription());

	    	productTypeJson = gson.toJson(gsonProduct);
	    	productTypeJsonList.add(productTypeJson);	
	    }	
		return productTypeJsonList;
	}

	/*
	 * Generates a json array with products
	 */
	private String concatJson(List<String> jsonList){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
        for( String jsonString:jsonList){
        	sb.append(jsonString); 
        	sb.append(","); 
        }
        // next remove the last "," that was added. Of course, if the list is empty, then keep the "[" you started with
        if(jsonList.size() > 0){
        	sb.deleteCharAt(sb.length()-1);
        }
             
        sb.append("]");
        return sb.toString();
		
	}
	
	private byte[] compress(byte[] image){
		// when compression fails, make sure compressedImage already contains at least the image itself
		byte[] compressedImage = image;
		
		try
	    {
	      ByteArrayOutputStream byteStream =
	        new ByteArrayOutputStream(image.length);
	      try
	      {
	        GZIPOutputStream zipStream =
	          new GZIPOutputStream(byteStream);
	        try
	        {
	          zipStream.write(image);
	        }
	        finally
	        {
	          zipStream.close();
	        }
	      }
	      finally
	      {
	        byteStream.close();
	      }

	      compressedImage = byteStream.toByteArray();

	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
		
		return compressedImage;		
	}

}
