package com.myforum.articlepage;

import java.util.HashMap;

public final class ArticleFactory {

	static 	ArticleFactory 				singleton;
			HashMap<String, Article> 	articleMapper = new HashMap<String, Article>();
			Article 					defaultArticle, previousArticle, nextArticle;
	
	
	private ArticleFactory(){
		defaultArticle 	= new Article(EArticleGroup.INDEX, "", "Articles", "Article-Index.html");
		previousArticle = defaultArticle;
		nextArticle 	= defaultArticle;
		
		createArticles();
	}
	
	private void createArticles(){
		// you can re-order the articles by simply moving the addArticle-line to the position you want	
		addArticle(new Article(EArticleGroup.JAVA, "REST-EASY", "Using Rest-Easy for Restful services", "REST-EASY.html"));
		addArticle(new Article(EArticleGroup.JAVA, "OPENSHIFT-TOMCAT7", "How to install a Java EE/Tomcat7 webapplication on OpenShift", "openshift-tomcat7.html"));
		addArticle(new Article(EArticleGroup.JAVA, "Spring & Wicket", "How to add Spring to your existing Wicket application Part 1: Setup", "SpringWicket.html"));
		addArticle(new Article(EArticleGroup.JAVA, "OPENSHIFTV3-WILDFLY", "How to install a Java EE/Wildfly webapplication on OpenShift v3", "openshift-wildfly.html"));
	}
	
	// prevent creating this factory more than once.
	public static ArticleFactory getInstance(){
		if(singleton == null){
		 singleton = new ArticleFactory();
		}
		return singleton;
	}
		
	private void addArticle(Article article){
		article.setPreviousArticle(previousArticle);
		article.setNextArticle(nextArticle);	
	
		previousArticle.setNextArticle(article);
		
		articleMapper.put(article.getArticleRestId(), article);
		
		previousArticle = article;		
	}
	
	public Article getDefaultArticle(){
		return defaultArticle;
	}
	
	public Article getArticle(String articleRestId){
		if( !articleMapper.containsKey(articleRestId) ){
			return defaultArticle;
		}
		return articleMapper.get(articleRestId);
	};
	
	public String getArticleIndex(){
		Article article 	= ArticleFactory.getInstance().getDefaultArticle();
		StringBuilder sb 	= new StringBuilder();

		do{
			String htmlArticleLine= "";
			article = article.getNextArticle();
			
			if( !article.getArticleGroupDescription().equals(article.getPreviousArticle().getArticleGroupDescription()) ){
				htmlArticleLine += "<h3 class=\".h3\">" + article.getArticleGroupDescription() + "</h3>";
			}		

			htmlArticleLine += "<ul class=\"blog-ul\">"; 
			htmlArticleLine += "<li><a ";
			htmlArticleLine += "href=\"/articles/" + article.getArticleRestId() + "\">" + article.getArticleDescription();
			htmlArticleLine += "</a></li>";
			htmlArticleLine += "</ul>";
			sb.append(htmlArticleLine);
			
		} while(article.getNextArticle() != defaultArticle);
		
		return sb.toString(); 
	}

}
