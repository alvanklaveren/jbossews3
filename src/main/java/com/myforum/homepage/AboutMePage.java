package com.myforum.homepage;

import java.io.File;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;

import com.myforum.application.ForumUtils;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.AVKLabel;

public class AboutMePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	public AboutMePage() {
		super(EMenuItem.AboutMe);
		
		add( new AVKLabel("aboutme", "About Me") );
		
		
		File file = ForumUtils.getFileResource("documents","cv.pdf");
		DownloadLink downloadlink = new DownloadLink("cv", file, "cv.pdf");
		add(downloadlink);
		
		Date startDate	= ForumUtils.createDate("01-04-2006", "dd-MM-yyyy"); // The day I started working at Maccs
		int workingYears = ForumUtils.yearsBetween(startDate, ForumUtils.todayNow());
			
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
