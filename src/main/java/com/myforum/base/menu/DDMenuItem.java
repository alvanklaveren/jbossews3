package com.myforum.base.menu;

public class DDMenuItem{
	private String 		itemText, itemHRef;
	private boolean 	isActive = false, disabled = false;
		
	public DDMenuItem(EMenuItem item){
		init(item);
	}

	public DDMenuItem(EMenuItem item, int activeMenuItemId){
		init(item);
		if(item.id() == activeMenuItemId){ isActive = true; }
	}

	public DDMenuItem init(EMenuItem item){
		itemText = item.defaultText();
		itemHRef = item.hRef();	
		return this;
	}
	
	public String toHtml(){
		return createDynamicHtml(itemText, itemHRef);
	};
	
	public DDMenuItem disable(){
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
	    sb.append("<a \" class=\"dropdown-item\" ");

	    if(!disabled){
		    sb.append(getHRefHtml(href));
	    }
	    
	    sb.append(">");
	    sb.append(displayText);
	    sb.append("</a>");

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
