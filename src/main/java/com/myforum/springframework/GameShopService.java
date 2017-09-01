package com.myforum.springframework;

import java.util.List;

import com.myforum.tables.Product;

public interface GameShopService {
	
	List<Product> getProducts(int gameConsoleId, int productTypeId, String title);
}
