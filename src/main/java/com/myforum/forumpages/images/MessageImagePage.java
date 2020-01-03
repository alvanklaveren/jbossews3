package com.myforum.forumpages.images;

import java.util.List;

import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import com.myforum.application.ForumUtils;
import com.myforum.base.BasePage;
import com.myforum.framework.ModalPage;
import com.myforum.tables.MessageImage;
import com.myforum.tables.dao.MessageImageDao;

public class MessageImagePage extends ModalPage {
	private static final long serialVersionUID = 1L;

	int numberOfItems = 10000;
	
	public MessageImagePage(final PageReference modalWindowPageRef, final BasePage originPage) {
		super(modalWindowPageRef, originPage);

		// show max number of items... because pagingnavigator needs to be adjusted to support modal windows
		numberOfItems = 10000;
		
		// Add list of ALL images
		final PageableListView<MessageImage> imageListView = createImageListView();
		addOrReplace(imageListView);
	}

	/*
	 *  Creates a PageableListView containing all the images used in messages
	 */
	private PageableListView<MessageImage> createImageListView(){
		
		// first, gather all products belonging to the search arguments gameConsoleId, productTypeId, and/or title
		List<MessageImage> imageList = new MessageImageDao().list();

		PageableListView<MessageImage> imageListView = new PageableListView<MessageImage>( "imagelist", imageList, numberOfItems ) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<MessageImage> listItem) {
				final MessageImage	messageImage = listItem.getModelObject();

				WebMarkupContainer imageDiv = new WebMarkupContainer("imagediv");
				imageDiv.add(new AjaxEventBehavior("onclick") {
					private static final long serialVersionUID = 1L;
					@Override
       		        protected void onEvent(AjaxRequestTarget target) {
       		            return;
       		        }
       		    });
				
       			imageDiv.setOutputMarkupId(true);		
				
				NonCachingImage productImage = ForumUtils.loadImage(messageImage.getImage(), "image");
       				      			     			
				imageDiv.add( productImage );
       			listItem.add(imageDiv);
       			
       			CompoundPropertyModel<MessageImage> imageModel = new CompoundPropertyModel<MessageImage>(messageImage);
      			listItem.setModel(imageModel);
       			
      			String id = "[i:" + messageImage.getCode() + "]";
      			
                final Label messageId = new Label( "messageid", id );
                messageId.setEscapeModelStrings(false);
                
                listItem.add(messageId);
			}
        };   
        
        // setReuseItems( true ) is very important. Each time a page renders, the listview is rebuild with new IDs,  
        // making it impossible to address the items for, say, visibility. 
        imageListView.setReuseItems( true ); 

		return imageListView;
	}
}
