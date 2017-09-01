package com.myforum.springframework;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myforum.gameshop.GameShopLogics;
import com.myforum.tables.ProductType;

@Service
public class GameShopProductTypeServiceImpl implements GameShopProductTypeService{

	@Override
	public List<ProductType> getProductTypes(){   	
	    return GameShopLogics.getProductTypeList();
    }
}