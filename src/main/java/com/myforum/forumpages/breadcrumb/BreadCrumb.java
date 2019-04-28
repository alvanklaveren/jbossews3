package com.myforum.forumpages.breadcrumb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.repeater.RepeatingView;

public class BreadCrumb implements Serializable{
	private static final long serialVersionUID = 1L;

	private int idNumber = 0;
	private List<BreadCrumbItem> items;
		
	public BreadCrumb(){
		items = new ArrayList<BreadCrumbItem>();
	}

	public List<BreadCrumbItem> getItems(){
		return items;
	}
	
	public void addItem(BreadCrumbItem item){
		idNumber++;
		item.setId(String.valueOf(idNumber));
		items.add(item);
		setLastCrumbActive();
	}

	public String id(){
		return String.valueOf(idNumber);
	}
	
	public void setLastCrumbActive(){
		int lastItemId = items.size()-1;
		if(lastItemId < 0){ 
			return; 
		}

		if(lastItemId > 0){
			items.get(lastItemId-1).setActive(false); // set previous to false (because it was true in the previous addItem()
		}
		
		items.get(lastItemId).setActive(true);
		return; 
	}
	
	public RepeatingView getRepeatingView(){
		RepeatingView view = new RepeatingView("breadcrumbs");

		for( BreadCrumbItem item:getItems()){
			view.add(item.getStatelessLink());
		};
		return view;
	}
		
}
