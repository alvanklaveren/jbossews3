package com.myforum.homepage;

import java.io.File;
import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;

import com.myforum.application.ForumUtils;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.dictionary.EText;
import com.myforum.dictionary.Translator;

public class AboutMePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	private Translator translator = Translator.getInstance();

	public AboutMePage() {
		super(EMenuItem.AboutMe);
	
		File file = ForumUtils.getFileResource("documents","cv.pdf");
		DownloadLink downloadlink = new DownloadLink("cv", file, "cv.pdf");
		add(downloadlink);
		
		Date startDate	= ForumUtils.createDate("01-04-2006", "dd-MM-yyyy"); // The day I started working at Maccs
		int workingYears = ForumUtils.yearsBetween(startDate, ForumUtils.todayNow());
			
		String years = "many";
		
		if( !ForumUtils.isNullOrZero(workingYears)){
			years = String.valueOf(workingYears);
		}

		switch( Translator.getInstance().getDefaultLanguage()) {
		case English:
			downloadlink.add(new Label("cvtext", "Click here to download my Curriculum Vitae"));
			break;
		case Dutch:
			downloadlink.add(new Label("cvtext", "Klik hier om mijn Curriculum Vitae te downloaden"));
			break;
		default:
			downloadlink.add(new Label("cvtext", "Click here to download my Curriculum Vitae"));
		}

		String markupText = translator.translate(EText.ABOUT_ME_TEXT).replace("<yearsworking>", years);
		add(new Label("markup", markupText).setEscapeModelStrings(false));	
	}
		
    @Override
    protected String getPageTitle() {
    	return "AVK - " + Translator.getInstance().translate(EText.ABOUT_ME);
    }

}
