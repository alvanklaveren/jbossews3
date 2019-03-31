package com.myforum.articlepage;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.myforum.application.XMLLogics;

public final class ArticleFactory {

	private static Logger LOG = LoggerFactory.getLogger(ArticleFactory.class);
	
	static 	ArticleFactory 				singleton;
	static  HashMap<String, Article> 	articleMapper = new HashMap<String, Article>();
	static  Article 					defaultArticle, previousArticle, nextArticle;
	
	
	private ArticleFactory(){
		defaultArticle 	= new Article("", "Index", "Articles", "Article-Index.html");
			
		createArticles();
	}
	
	/**
	 * To allow modification of articles (and index page), always run 
	 * createArticles again when opening the articles page
	 * 
	 */
	public static void createArticles(){
		articleMapper = new HashMap<String, Article>();
		
		previousArticle = defaultArticle;
		nextArticle 	= defaultArticle;
		defaultArticle.setPreviousArticle(defaultArticle);
		defaultArticle.setNextArticle(defaultArticle);
	
		Document doc = XMLLogics.toDocument("articlepages", "articles.xml");
        
        if (!doc.getDocumentElement().getNodeName().equals("avkarticles")) {
        	
        	LOG.error("articles.xml does not start with <avkarticles>");
        	return;
        };
         
        NodeList nodes = doc.getElementsByTagName("articlegroup");
        for(Node node:XMLLogics.asList(nodes)) {
        	
        	Element element = (Element) node;
        	String articleGroupTitle = element.getElementsByTagName("title").item(0).getTextContent();
        	       	
        	for(Node articleNode: XMLLogics.asList(element.getElementsByTagName("article"))) {
        		
        		Element articleElement = (Element) articleNode;
        		String articleRestId = articleElement.getElementsByTagName("id").item(0).getTextContent();
    			String articleTitle = articleElement.getElementsByTagName("title").item(0).getTextContent();
    			String articleDocument = articleElement.getElementsByTagName("document").item(0).getTextContent();

    			addArticle(new Article(articleGroupTitle, articleRestId, articleTitle, articleDocument));
        	}
        }	
	}
	
	// prevent creating this factory more than once.
	public static ArticleFactory getInstance(){
		if(singleton == null){
		 singleton = new ArticleFactory();
		}
		return singleton;
	}
		
	private static void addArticle(Article article){
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
		if(articleRestId == null || articleRestId.trim().isEmpty() || articleRestId.equals("Index")) 
			return defaultArticle;
		
		if( !articleMapper.containsKey(articleRestId) )
			return defaultArticle;
		
		return articleMapper.get(articleRestId);
	}
	
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
