package com.myforum.framework;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigation;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;

public class StatefulPagingNavigator extends AjaxPagingNavigator{
	private static final long serialVersionUID = 1L;

	public StatefulPagingNavigator(String id, IPageable pageable) {
		super(id, pageable);
	}
	
	@Override
	protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		return new AjaxPagingNavigation(id, pageable, labelProvider) {
			private static final long serialVersionUID = 1L;

			@Override
			protected LoopItem newItem(int iteration) {
				LoopItem item = super.newItem(iteration);

				// add css for enable/disable link
				long pageIndex = getStartIndex() + iteration;
				item.add(new AttributeModifier("class", new PageLinkCssModel(pageable, pageIndex, "page-link active")));

				return item;
			}
		};
	}
}