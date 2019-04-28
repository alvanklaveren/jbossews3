package com.myforum.forumpages.breadcrumb;

import org.apache.wicket.markup.html.panel.Panel;

import com.myforum.application.CookieLogics;
import com.myforum.application.StringLogics;
import com.myforum.forumpages.ForumCategoryPanel;
import com.myforum.forumpages.ForumHomePanel;
import com.myforum.forumpages.ForumMessagePanel;
import com.myforum.homepage.HomePage;
import com.myforum.tables.Message;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;

public class BreadCrumbPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	public BreadCrumbPanel(String id){
		super(id);
		
		BreadCrumb breadCrumb = new BreadCrumb();
		BreadCrumbItem item = new BreadCrumbItem("Home");
		item.setCookiesToDelete("codeMessageCategory;codeMessage");
		item.setPage(HomePage.class);
		breadCrumb.addItem(item);
		
		item = new BreadCrumbItem("Categories");
		item.setCookiesToDelete("codeMessageCategory;codeMessage");
		item.setPanel(ForumHomePanel.class);
		breadCrumb.addItem(item);

		String categoryDescription 	= "";	
		int codeMessageCategory = CookieLogics.getCookieInt("codeMessageCategory");
		if( codeMessageCategory != 0 ){
			categoryDescription = new MessageCategoryDao().find( codeMessageCategory ).getDescription();

			item = new BreadCrumbItem(categoryDescription);
			item.setCookiesToDelete("codeMessage");
			item.setPanel(ForumCategoryPanel.class);
			breadCrumb.addItem(item);
		}
		       
		int codeMessage = CookieLogics.getCookieInt("codeMessage");
		if( codeMessage > 0 ){
			Message message = new MessageDao().find( codeMessage );

			// message can be null when it is just deleted by you or somebody else.
			if(message != null){
		        String messageDescription = "";	
				messageDescription = message.getDescription();
			
				if( !StringLogics.isEmpty(messageDescription) && messageDescription.length() + categoryDescription.length() > 60 ){
					messageDescription = messageDescription.substring( 0, 60 - categoryDescription.length() ) + "...";
				}
				
				item = new BreadCrumbItem(messageDescription);
				item.setCookiesToDelete(null);
				item.setPanel(ForumMessagePanel.class);
				breadCrumb.addItem(item);
			}

		}
	
		addOrReplace(breadCrumb.getRepeatingView());
	
	}   
}
