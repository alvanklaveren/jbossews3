package com.myforum.forumpages.header;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.base.AVKPage;
import com.myforum.forumpages.breadcrumb.BreadCrumbPanel;
import com.myforum.framework.AVKPanel;
import com.myforum.tables.ForumUser;

public class HeaderPanel extends AVKPanel {
	private static final long serialVersionUID = 1L;
	private ForumUser activeUser;

	public HeaderPanel() {
        super("headerpanel");
        
        setOutputMarkupId(true);

        addOrReplace( new Label("forumname", new Model<String>("AVK Forum")) );			
			
		addOrReplace( new BreadCrumbPanel("breadcrumbs") );
    }

    @Override
	protected void onBeforeRender() {
    	activeUser = ((AVKPage) getPage()).getActiveUser();
		// Form for login credentials
		addOrReplace( new LoginForm( "loginForm", activeUser ) );
		addOrReplace( new LogoutForm( "logoutForm", activeUser ) );

    	super.onBeforeRender();
    }

	
}
