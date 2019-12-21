package com.myforum.base.menu;

public class MenuItem{
	private String 		itemText, itemHRef;
	private boolean 	isActive = false, disabled = false;
		
	public MenuItem(EMenuItem item){
		init(item);
	}

	public MenuItem(EMenuItem item, int activeMenuItemId){
		init(item);
		if(item.id() == activeMenuItemId){ isActive = true; }
	}

	public MenuItem init(EMenuItem item){
		itemText = item.defaultText();
		itemHRef = item.hRef();	
		return this;
	}

	public MenuItem(String itemText, String itemHRef){
		this.itemText = itemText;
		this.itemHRef = itemHRef;	
	}

	public String toHtml(){
		return createDynamicHtml(itemText, itemHRef);
	};
	
	public MenuItem disable(){
		disabled = true;
		return this;
	}
	
	protected void setItemText(String itemText){
		this.itemText = itemText;
	}

	protected String getItemText(){
		return itemText;
	}

	private String createDynamicHtml(String displayText, String href) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("<li \" class=\"nav-item");

	    if(isActive) sb.append(" active" );
  
	    sb.append("\"><a class=\"nav-link\" ");

	    if(!disabled){
		    sb.append(getHRefHtml(href));
	    }
	    
	    sb.append(">");
	    sb.append(displayText);
	    sb.append("</a></li>");

	    return sb.toString(); 
	}
	
	public String getHRefHtml(String href){
	    StringBuilder sb = new StringBuilder();
		sb.append(" href=\"");
		sb.append( href );
		sb.append("\"" );
		return sb.toString();
	}

}
