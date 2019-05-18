package com.myforum.forumpages.breadcrumb;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.StatelessLink;

import com.myforum.application.CookieLogics;
import com.myforum.base.AVKPage;
import com.myforum.dictionary.EText;
import com.myforum.dictionary.Translator;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.ForumBasePanel;
import com.myforum.framework.AVKLabel;

public class BreadCrumbItem implements Serializable{
	private static final long serialVersionUID = 1L;

	private String cookiesToDelete;
	private String displayText;
	boolean	isActive;
	private Class<? extends ForumBasePanel> panelClass;
	private Class<? extends AVKPage> pageClass;
   	//private static Logger log = LoggerFactory.getLogger(BreadCrumbItem.class);


	public BreadCrumbItem(String text) {
		isActive = false; // sb.append(" class=\"active\"");
		this.displayText = text;
	}

	public BreadCrumbItem setPanel(Class<? extends ForumBasePanel> panelClass){
		this.panelClass = panelClass;
		return this;
	}

	public BreadCrumbItem setPage(Class<? extends AVKPage> pageClass){
		this.pageClass = pageClass;
		return this;
	}

	public void setActive(boolean isActive){
		this.isActive = isActive;
	}

	private String id;
	public void setId(String id) {
		this.id = id;
	}

	public String getDisplayText() {
		return displayText;
	}

	public BreadCrumbItem setCookiesToDelete(String cookiesToDelete){
		this.cookiesToDelete = cookiesToDelete;
		return this;
	}
	
	public String id(){
		return id;
	}
	
	public StatelessLink<String> getStatelessLink(){
		StatelessLink<String> sLink = new StatelessLink<String>(id()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				CookieLogics.deleteCookies(cookiesToDelete);
				ForumBasePage parent = (ForumBasePage) getPage();
				if(panelClass != null){
					ForumBasePanel panel = getPanel((ForumBasePage) getPage());
					if(panel != null){
						parent.addOrReplace(panel);
					}
					return;
				}else if(pageClass != null){
					setResponsePage(pageClass);
					return;
				}
				return;
			}
			@Override
			public boolean isEnabled(){
				return !isActive;
			}
		};
		
		AVKLabel breadcrumbItemLabel = new AVKLabel("label", displayText); 
		
		if(isActive){ 
			sLink.add(new AttributeModifier("class", "active"));

		} else {
			sLink.add(new AttributeModifier("title", Translator.getInstance().translate(EText.CLICK_TO_NAVIGATE)));
			sLink.add(new AttributeModifier("style", "cursor:pointer;"));

			// need to add href="#" for the CSS to work. Does not override the clicked event so everything still works peachy
			breadcrumbItemLabel.add(new AttributeModifier("href", "#")); 
		}

		sLink.add(breadcrumbItemLabel);

		return sLink;
	}

	private ForumBasePanel getPanel(ForumBasePage parent){
		ForumBasePanel panel = null;
		try {
			Constructor<? extends ForumBasePanel> constructor = panelClass.getConstructor(new Class[]{ForumBasePage.class});
			panel = constructor.newInstance(new Object[]{parent});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return panel;
	}
}
