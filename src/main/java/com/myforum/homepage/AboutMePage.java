package com.myforum.homepage;

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;

import com.myforum.application.ForumUtils;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.AVKLabel;

public class AboutMePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public AboutMePage() {
		super(EMenuItem.AboutMe);
		
		add( new AVKLabel("aboutme", "About Me") );
		
		Date startDate	= ForumUtils.createDate("01-04-2006", "dd-MM-yyyy"); // The day I started working at Maccs
		int workingYears = ForumUtils.yearsBetween(startDate, ForumUtils.todayNow());
		
		// Appears to be working on alvanklaveren.com, but does not work on localhost (not sure why...)
		ExternalLink pdfLink = new ExternalLink("pdf", "images/cv.pdf");
		add(pdfLink);
		
		String years = "many";
		
		if( !ForumUtils.isNullOrZero(workingYears)){
			years = String.valueOf(workingYears);
		}
		
		Label yearsWorkingLabel = new Label("yearsworking", years);
		add(yearsWorkingLabel);
	}
		
    @Override
    protected String getPageTitle() {
    	return "AVK - About Me";
    }

}
