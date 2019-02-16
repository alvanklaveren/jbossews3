package com.myforum.gameshop;


import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.models.AddProduct;
import com.myforum.tables.dao.CompanyDao;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductTypeDao;

public class AddGamePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public AddGamePage(PageParameters params) {
		super();

		String 	name		= ForumUtils.getParmString(params, "name",        "");
		String 	description	= ForumUtils.getParmString(params, "description", "");
		String 	yearString	= ForumUtils.getParmString(params, "year", 		  "0");
		int		consoleId	= ForumUtils.getParmInt(   params, "console",      0);
		int		typeId		= ForumUtils.getParmInt(   params, "type",         0);
		int		companyId	= ForumUtils.getParmInt(   params, "company",      0);
		
		AddProduct newProduct = new AddProduct();
	
		if (!StringLogics.isEmpty(name))		{ newProduct.setName(name); }
		if (!StringLogics.isEmpty(description))	{ newProduct.setDescription(description); }
		if (consoleId != 0)						{ newProduct.setGameConsole(new GameConsoleDao().find(consoleId)); }
		if (typeId != 0)						{ newProduct.setProductType(new ProductTypeDao().find(typeId)); }
		if (companyId != 0)						{ newProduct.setCompany(new CompanyDao().find(companyId)); }

		if (!StringLogics.isEmpty(yearString))	{
			int year = 0;
			try {
				year = Integer.parseInt(yearString);
			} catch (NumberFormatException e) {
				// too bad. Stick to zero
			}
			newProduct.setYear(year); 
		}

		// Form for game info
		final Form<AddProduct> addGameForm = new AddGameForm(newProduct);
		add(addGameForm);
	}
}

