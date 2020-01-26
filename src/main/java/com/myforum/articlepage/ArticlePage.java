package com.myforum.articlepage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myforum.application.ForumUtils;
import com.myforum.base.AVKPage;
import com.myforum.base.menu.EMenuItem;
import com.myforum.dictionary.Translator;

public class ArticlePage extends AVKPage {
	private static final long serialVersionUID = 1L;

	private static Logger LOG = LoggerFactory.getLogger(ArticlePage.class); 
	
	public ArticlePage(final PageParameters params) {
		super(EMenuItem.Articles);
		String articleRestId = ForumUtils.getParmString(params, "id", "");
		
		Article article = ArticleFactory.getInstance().getArticle(articleRestId);

		String htmlFileName	= article.getHtmlFileName();
		
		String htmlContent = "<html><body><H1>Article not found !!</H1></body></html>";
		try {
			File file = ForumUtils.getFileResource("articlepages", htmlFileName);
			FileInputStream fileInputStream;
			fileInputStream = new FileInputStream(file);
			htmlContent = IOUtils.toString(fileInputStream);
		} catch (FileNotFoundException e) {
			
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
		
		Label navigatorLabel = null;
		
		// only for the default article, dynamically add the articles to form an index
		if(article.equals(ArticleFactory.getInstance().getDefaultArticle())){
			// refresh article setup based on articles.xml
			ArticleFactory.createArticles();
			
			htmlContent = htmlContent.replace("<titlelabel/>", translator.translate("Articles") );
			htmlContent = htmlContent.replace("<descriptionlabel/>", translator.translate("Firsthand experience with Languages, Frameworks and IDEs") );
			htmlContent = htmlContent.replace("<articleindex/>", ArticleFactory.getInstance().getArticleIndex() );

			navigatorLabel = new Label("navigator", "");
		}else{

			navigatorLabel = new Label("navigator", getNavigatorHTML(article));
		}
	
		Label htmlContentLabel = new Label("htmlcontent", htmlContent);
		htmlContentLabel.setEscapeModelStrings(false);
		addOrReplace(htmlContentLabel);

		navigatorLabel.setEscapeModelStrings(false); 
		addOrReplace(navigatorLabel);

	
	}

	private String getNavigatorHTML(Article article){
		StringBuilder sb = new StringBuilder();
		if(!article.getPreviousArticle().getArticleRestId().equals("")){
			sb.append("<span style=\"float:left\"><a class=\"btn btn-outline-primary\" href=\"" + article.getPreviousArticle().getArticleRestId() + "\">Previous</a></span>");
		}
		
		if(!article.getNextArticle().getArticleRestId().equals("")){
			sb.append("<span style=\"float:right\"><a class=\"btn btn-outline-primary\" href=\"" + article.getNextArticle().getArticleRestId() + "\">Next</a></span>");
		}
		return sb.toString();
	}
	
    @Override
    protected String getPageTitle() {
    	return "AVK - " + Translator.getInstance().translate("Articles");
    }
}
