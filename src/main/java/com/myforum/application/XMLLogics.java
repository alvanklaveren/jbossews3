package com.myforum.application;

import java.io.File;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XMLLogics{
	
	static{ new XMLLogics(); }

	private static Logger LOG = LoggerFactory.getLogger(XMLLogics.class);
   	
	public static List<Node> asList(NodeList n) {
		return n.getLength()==0?Collections.<Node>emptyList(): new NodeListWrapper(n);
	}
	
	public static Document toDocument(String filepath, String filename) {
		try {
			File file = ForumUtils.getFileResource(filepath, filename);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
	        Document document = dBuilder.parse(file);
	        document.getDocumentElement().normalize();
	        return document;
		} catch (Exception e) {
			
			LOG.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	static final class NodeListWrapper extends AbstractList<Node> implements RandomAccess {
		private final NodeList list;
		NodeListWrapper(NodeList l) {
			list=l;
		}
		public Node get(int index) {
			return list.item(index);
		}
		public int size() {
			return list.getLength();
		}
	}	
   	
}
   	
