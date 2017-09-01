package com.myforum.application;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XmlElement {
	private String 				elementName, elementValue;
	private XmlElement 			parent;
	private List<XmlElement> 	childList;
	
	public XmlElement( String elementName, String elementValue ){
		init( elementName );
		this.elementValue	= elementValue;
	}

	public XmlElement( String elementName, int elementValue ){
		init( elementName );
		this.elementValue	= String.valueOf( elementValue );
	}

	public XmlElement( String elementName, Date elementValue ){
		init( elementName );
		this.elementValue	= new SimpleDateFormat("dd-MM-yyyy").format( elementValue );
	}

	private void init( String elementName ){
		this.elementName 	= elementName;	
		this.parent 		= this; // isRoot !!
		childList 			= new ArrayList<XmlElement>();
	}

	public void addChild( XmlElement child ){
		child.parent = this; // Child of this (parent)
		childList.add( child );
	}
	
	public String getName(){
		return elementName;
	}

	public String getValue(){
		return elementValue;
	}

	public XmlElement getParent(){
		return parent;
	}
	
	public boolean isRoot(){
		return ( parent == null );
	}

	public boolean isLeaf(){
		return ( childList.isEmpty() );
	}

	@Override
	public String toString(){
		StringBuilder xml = new StringBuilder();
		xml.append( '<' + elementName + '>' + elementValue );

		if( !isLeaf() ){
			for( XmlElement child : childList ){
				xml.append( child.toString() );
			}
		}
		
		xml.append( "</" + elementName + '>' );

		return xml.toString();
	}	
	
}
