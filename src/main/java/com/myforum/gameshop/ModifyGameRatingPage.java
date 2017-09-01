package com.myforum.gameshop;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.ForumUtils;
import com.myforum.base.BasePage;
import com.myforum.framework.ErrorLabel;
import com.myforum.tables.Product;
import com.myforum.tables.ProductRating;
import com.myforum.tables.dao.ProductDao;
import com.myforum.tables.dao.ProductRatingDao;

public class ModifyGameRatingPage extends BasePage {
	private static final long serialVersionUID = 1L;

	public ModifyGameRatingPage(PageParameters params) {
		super();

		int codeProduct = ForumUtils.getParmInt(params, "codeProduct", 0);
		
		Product product = new ProductDao().find(codeProduct);
		
		Label gameNameLabel = new Label( "gamename", product.getName());
		add(gameNameLabel);
		
		addOrReplace( new ErrorLabel() );

		final ProductRating productRating = new ProductRatingDao().findOrCreateByProduct(codeProduct);

		// Form for product rating info
		final Form<ProductRating> productRatingForm = new ModifyGameRatingForm( productRating );
		add(productRatingForm);
	}

}
