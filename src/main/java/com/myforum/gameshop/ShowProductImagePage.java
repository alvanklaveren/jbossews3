package com.myforum.gameshop;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.NonCachingImage;

import com.myforum.application.ForumUtils;
import com.myforum.base.BasePage;
import com.myforum.framework.ModalPage;
import com.myforum.tables.Product;

public class ShowProductImagePage extends ModalPage {
	private static final long serialVersionUID = 1L;

	final Product product;

	public ShowProductImagePage(final PageReference modalWindowPageRef, final BasePage originPage) {
		super(modalWindowPageRef, originPage);
		product = null;

		add(new Label("productname", ""));
		add(new Label("image", ""));
	}

	public ShowProductImagePage(final PageReference modalWindowPageRef, final BasePage originPage, final Product product) {
		super(modalWindowPageRef, originPage);
		this.product = product;
		
		NonCachingImage productImage = ForumUtils.loadImage(GameShopLogics.getImage(product), "image");

		add(new Label("productname", product.getName()));

		productImage.setOutputMarkupId(true);
		add(productImage);
	
	}

	@Override
	protected String getTitle(){
			return "";
	}

}
