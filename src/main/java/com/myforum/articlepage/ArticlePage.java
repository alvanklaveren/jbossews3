package com.myforum.articlepage;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;

import com.myforum.application.ForumUtils;
import com.myforum.application.IoLogics;
import com.myforum.base.BasePage;
import com.myforum.base.menu.EMenuItem;

public class ArticlePage extends BasePage {
	private static final long serialVersionUID = 1L;

	public ArticlePage(PageParameters params) {
		super(EMenuItem.Articles);
		String articleRestId = ForumUtils.getParmString(params, "id", "");
		
		Article article = ArticleFactory.getInstance().getArticle(articleRestId);

		String htmlFileName	= article.getHtmlFileName();
		String filePath		= "/com/myforum/articlepage/articles/";
		String htmlContent	= IoLogics.getFileContent(getClass(), filePath + htmlFileName);
		
		// only for the default article, dynamically add the articles to form an index
		if(article == ArticleFactory.getInstance().getDefaultArticle()){
			htmlContent = htmlContent.replace("<articleindex/>", ArticleFactory.getInstance().getArticleIndex() );
			Label navigatorLabel = new Label("navigator", "");
			navigatorLabel.setVisible(false);
			addOrReplace(navigatorLabel);
		}else{
			Label navigatorLabel = new Label("navigator", getNavigatorHTML(article));
			navigatorLabel.setEscapeModelStrings(false); 
			addOrReplace(navigatorLabel);
		}

		htmlContent = htmlContent.replace("<titlelabel/>", translator.translate("Articles") );
		htmlContent = htmlContent.replace("<descriptionlabel/>", translator.translate("Firsthand experience with Languages, Frameworks and IDEs") );
		
		Label htmlContentLabel = new Label("htmlcontent", htmlContent);
		htmlContentLabel.setEscapeModelStrings(false);
		addOrReplace(htmlContentLabel);
		
	
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
    	return "AVK - Articles";
    }

	@Override
	public void renderHead(IHeaderResponse response) {
	    response.render(CssHeaderItem.forReference(new CssResourceReference(ArticlePage.class, "css/blog.css")));
	}
}
