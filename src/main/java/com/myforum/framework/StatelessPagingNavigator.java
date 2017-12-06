package com.myforum.framework;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class StatelessPagingNavigator extends PagingNavigator
{
    private static final long serialVersionUID = 8219151328688000388L;

    public static final String PAGING_PAGE_PARAMETER = "page";

    private final PageParameters parameters;

    public StatelessPagingNavigator(String id, PageParameters parameters, long pageNumber, IPageable pageable)
    {
        super(id, pageable);

        this.parameters = new PageParameters(parameters);

        pageable.setCurrentPage(pageNumber);
    }
    
    @Override
    protected AbstractLink newPagingNavigationIncrementLink(String id, IPageable pageable, int increment)
    {
        long pageNumber = pageable.getCurrentPage() + increment;
        return newPageLink(id, pageNumber);
    }

    @Override
    protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber)
    {
        // The base PagingNavigator uses -1 to refer to the last page
        if (pageNumber < 0)
        {
            pageNumber += pageable.getPageCount();
        }
        return newPageLink(id, pageNumber);
    }

    @Override
    protected PagingNavigation newNavigation(final String id, final IPageable pageable, final IPagingLabelProvider labelProvider) {
        return new PagingNavigation(id, pageable, labelProvider){
			private static final long serialVersionUID = 1L;

			@Override
            protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, long pageNumber)
            {
                return newPageLink(id, pageNumber);
            }
        };
    }

    @Override
    protected boolean getStatelessHint(){
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private AbstractLink newPageLink(String id, long pageNumber){
        PageParameters withPaging = new PageParameters(parameters);
        withPaging.set(PAGING_PAGE_PARAMETER, pageNumber);

        return new BookmarkablePageLink(id, getPage().getPageClass(), withPaging);
    }
}