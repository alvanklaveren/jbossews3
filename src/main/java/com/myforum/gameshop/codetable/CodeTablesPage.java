package com.myforum.gameshop.codetable;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.ForumUtils;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.homepage.HomePage;

public class CodeTablesPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	private Logger log = LoggerFactory.getLogger(CodeTablesPage.class);
	
	public CodeTablesPage(PageParameters params) {
		super(EMenuItem.CodeTables);
		
		String codeTable = ForumUtils.getParmString(params, "codetable", "?");
		
		switch(codeTable.toLowerCase()) {
		case "company":			setResponsePage(CTCompanyPage.class); break;
		default:				
			// codetable doesn't exist. Return to home page
			log.error("Invalid/unknown codetable " + codeTable + " requested");
			setResponsePage(HomePage.class); 			
			break;
		}
	}
}
