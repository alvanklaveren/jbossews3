package com.myforum.tables.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.myforum.tables.Product;
import com.myforum.tables.ProductImage;

public class ProductImageDao extends HibernateDao<ProductImage, Integer>{

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public List<ProductImage> list( Product product ){
		Session session = prepareTransaction();

    	Criteria criteria = session.createCriteria( daoType );
    	criteria.add(Restrictions.eqOrIsNull( "product.code", product.getCode() ) );
    	criteria.addOrder( Order.asc( "code" ) );
    	
        return (List<ProductImage>) criteria.list(); 
    }
	
	public ProductImage getPrimaryImage( Product product ){
		List<ProductImage> imageList = list(product);
        if( imageList == null || imageList.size() <= 0 ){
        	return null;
        }; 
        return imageList.get(0);
    }

	public void setPrimaryImage( Product product, ProductImage image ){
		List<ProductImage> imageList = list(product);
        if( imageList == null ){
        	imageList = new ArrayList<ProductImage>();
        }
        if( imageList.size() <= 0 ){
        	imageList.add(image);
        }else{ 
        	imageList.set(0, image);
        }
    }


}
