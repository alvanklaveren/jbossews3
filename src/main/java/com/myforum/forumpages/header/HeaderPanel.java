package com.myforum.forumpages.header;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.myforum.base.AVKPage;
import com.myforum.base.breadcrumb.BreadCrumbPanel;
import com.myforum.framework.AVKPanel;
import com.myforum.framework.PanelErrorLabel;
import com.myforum.framework.PanelNotificationLabel;
import com.myforum.tables.ForumUser;

public class HeaderPanel extends AVKPanel {
	private static final long serialVersionUID = 1L;
	private ForumUser activeUser;

	public HeaderPanel() {
        super("headerpanel");
        
		// Not all panels will show error messages, but the ones that do (and should) have different "ideal" locations to show this error,
		// so we allow putting it in each panel manually, but we do not want to explicitly repeat the code for it, hence the below try-catch
		try {
				addOrReplace( new PanelErrorLabel() );
				addOrReplace( new PanelNotificationLabel() );
		}catch(Exception e) {
			// do nothing... it is okay if it did not succeed
		}

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
