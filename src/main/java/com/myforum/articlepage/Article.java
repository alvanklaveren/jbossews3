package com.myforum.articlepage;

public class Article {

	private String 			articleRestId, articleDescription, htmlFileName;
	private Article			previousArticle, nextArticle;
	private String			articleGroup;
	
	public Article(String articleGroup, String articleRestId, String articleDescription, String htmlFileName){
		this.articleRestId 		= articleRestId;
		this.articleDescription = articleDescription;
		this.htmlFileName 		= htmlFileName;
		this.articleGroup		= articleGroup;
	}

	public String getHtmlFileName() {
		return htmlFileName;
	}

	public void setHtmlFileName(String htmlFileName) {
		this.htmlFileName = htmlFileName;
	}

	public String getArticleRestId() {
		return articleRestId;
	}

	public void setArticleRestId(String uniqueRestId) {
		this.articleRestId = uniqueRestId;
	}

	public String getArticleDescription() {
		return articleDescription;
	}
	
	public String getArticleGroupDescription(){
		return articleGroup;
	}

	public void setArticleDescription(String articleDescription) {
		this.articleDescription = articleDescription;
	}
	
	public Article getPreviousArticle() {
		return previousArticle;
	}

	public void setPreviousArticle(Article previousArticle) {
		this.previousArticle = previousArticle;
	}

	public Article getNextArticle() {
		return nextArticle;
	}

	public void setNextArticle(Article nextArticle) {
		this.nextArticle = nextArticle;
	}
}
