package com.myforum.springframework;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myforum.gameshop.GameShopLogics;
import com.myforum.tables.Product;

@Service
public class GameShopServiceImpl implements GameShopService{

	@Override
	public List<Product> getProducts(int gameConsoleId, int productTypeId, String title){   	
	    List<Product> productList = GameShopLogics.getProductList(gameConsoleId, productTypeId, title);
	        
	    return productList;
    }
}