package com.myforum.application;

public enum ETheme {

	  Light(1, "Light", "body{ background:#F3F3FC; color:#303030; } #centerpanel{background:#FFFFFF;} ")
	, Dark(2, "Dark", "body{ background:#000000; color:#F3F3FC } #centerpanel{ background:#303030; color:#F3F3FC } #gamelistitem{background:#202020; color:#F3F3FC} td {background:#202020; color:#F3F3FC} .breadcrumb { background:#202020; } code{ background:#404040 } pre{background:#303030; color:#F3F3FC}")
  ;

	int		id;
	String	description;
	String  style;
	
	public static String cookieName = "defaultTheme";
	
	ETheme(int id, String description, String style){
		
		this.id = id;
		this.description = description;
		this.style = style;
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getStyle() {
		return style;
	}
	
	public String toString(){
		return description;
	}
	
	public static ETheme getActiveTheme() {
		int id = CookieLogics.getCookieInt(cookieName);
		for(ETheme value: values()) {
			if(value.getId() == id) {
				return value;
			}
		}
		// none found? Default to Light
		return ETheme.Light;
	}
}
