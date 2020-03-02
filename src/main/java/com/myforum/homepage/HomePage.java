package com.myforum.homepage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;

import com.myforum.application.CookieLogics;
import com.myforum.application.StringLogics;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.dictionary.Translator;
import com.myforum.tables.Message;
import com.myforum.tables.MessageCategory;
import com.myforum.tables.dao.MessageCategoryDao;
import com.myforum.tables.dao.MessageDao;


public class HomePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	private final MessageCategoryDao 	messageCategoryDao 	= new MessageCategoryDao();
	private final SimpleDateFormat		dateFormat			= new SimpleDateFormat("yyyy/MM/dd");
	private List<Message> 				messageList 		= Collections.synchronizedList( new ArrayList<Message>() );
	
	//@SpringBean
	//protected HelloService helloService;

	public HomePage() {
		super(EMenuItem.Home);
	
		// First spring bean
		//System.out.println(helloService.getHelloWorldMessage());
		
		CookieLogics.deleteCookie("moveMessage");
		CookieLogics.deleteCookie("codeMessageCategory");
		CookieLogics.deleteCookie("codeMessage");

		//Label helloLabel = new AVKLabel( "hellolabel", "Hello Galaxy");

		//addOrReplace(helloLabel);
		//addOrReplace(new BookmarkablePageLink<ForumBasePage>("forum", ForumBasePage.class));
		//addOrReplace(new BookmarkablePageLink<SourceHomePage>("sourcepage", SourceHomePage.class));

		
		final MessageCategory messageCategory = messageCategoryDao.find( -1 );
		
		MessageDao messageDao = new MessageDao();
		messageList = messageDao.getMessages( messageCategory );
	    	    
	    ListView <Message> listView = new ListView <Message>("messagelist", messageList) {
			private static final long serialVersionUID = 1L;
			
			@Override
			public void populateItem(final ListItem<Message> listItem) {
				//final ForumUserDao forumUserDao = new ForumUserDao();
				
				Message message 		= listItem.getModelObject();
				//String 	displayName		= forumUserDao.find(message).getDisplayName();
				String 	messageDate 	= dateFormat.format(listItem.getModelObject().getMessageDate());
				String 	messagetext 	= StringLogics.prepareMessage(message.getMessageText());

				listItem.add( new Label("messagedate", messageDate) );
                listItem.add( new MultiLineLabel("messagetext", messagetext).setEscapeModelStrings(false) );
                
			}
        };
		
        addOrReplace( listView ); 
		
	}
	
    @Override
    protected String getPageTitle() {
    	return "AVK - " + Translator.getInstance().translate("Home");
    }
	
}
