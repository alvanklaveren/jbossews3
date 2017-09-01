package com.myforum.base.menu;

public enum EMenuItem {
	DUMMY			(0, "", ""),
	Home			(1, "Home", "/"),
	AboutMe 		(2, "About Me", "/aboutme"),
	Articles		(3, "Articles", "/articles"),
	GameShop		(4, "My Game Collection", "/gameshop"),
	Applications	(5, "Applications", "#"),
	Forum			(6, "Forum", "/forum"),
	Sources			(7, "Sources", "/sources"),
	Divider			(8, "<Divider>", "#"),
	Header			(9, "<Header not initialised>", "#"),
	AVKOS			(10, "AVK OS", "#"),
	AppCreator		(11, "App Creator", "#")
	;
	
	private int		id;
	private String 	defaultText;
	private String 	hRef;
	
	private EMenuItem(int id, String defaultText, String href){
		this.id = id;
		this.defaultText = defaultText;
		this.hRef = href;
	}
	
	public int id(){
		return id;
	}
	
	public String hRef(){
		return hRef;
	}
	
	public String defaultText(){
		return defaultText;
	}
}
