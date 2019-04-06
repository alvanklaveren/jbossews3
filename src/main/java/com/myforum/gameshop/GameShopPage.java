package com.myforum.gameshop;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.lang.Generics;

import com.googlecode.wicket.jquery.ui.form.autocomplete.AutoCompleteTextField;
import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.dictionary.EText;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.AVKButton;
import com.myforum.framework.AVKLabel;
import com.myforum.framework.ResponseFormButton;
import com.myforum.framework.StatelessPagingNavigator;
import com.myforum.gameshop.DDC.CompanyDDC;
import com.myforum.gameshop.DDC.GameConsoleDDC;
import com.myforum.gameshop.DDC.ProductTypeDDC;
import com.myforum.tables.Product;
import com.myforum.tables.ProductImage;
import com.myforum.tables.ProductRating;
import com.myforum.tables.dao.ProductDao;

public class GameShopPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private 		int	   			consoleId, typeId, numberOfItems;
	private 		String 			title 				= null;
	private			String			sortOrder			= null;
	private			long			pageNumber			= 0;
	private final 	DDCSelectModel 	selectModel 		= new DDCSelectModel();
	private 		List<Integer> 	numberOfItemsList 	= new ArrayList<Integer>();
	private 		List<String> 	sortOrderList	 	= new ArrayList<String>();

	
	public GameShopPage(PageParameters params) {
		super(EMenuItem.GameShop);

		numberOfItemsList.add(12);
		numberOfItemsList.add(24);
		numberOfItemsList.add(36);

		sortOrderList.add(ESortOrder.AZ.getDescription());
		sortOrderList.add(ESortOrder.ZA.getDescription());
		sortOrderList.add(ESortOrder.Rating.getDescription());
		
		addOrReplace( new GameShopPanelLeft("sidepanelleft") );
		
		title			= ForumUtils.getParmString(	params, "searchtitle",	 ""	);
		consoleId 		= ForumUtils.getParmInt(	params, "console",        0	);
		typeId 			= ForumUtils.getParmInt(	params, "type",           0	);
		numberOfItems	= ForumUtils.getParmInt(	params, "numberofitems", 12	);
		pageNumber		= ForumUtils.getParmInt(	params, "page", 		  0	);
		sortOrder		= ForumUtils.getParmString(	params, "sortorder", 	 ESortOrder.AZ.getDescription()	);
		
		// reset title when user has chosen to go to a particular console or type (games/accessories/etc).
		if( consoleId != 0 || typeId != 0 ){ title = ""; }

		if( !numberOfItemsList.contains(numberOfItems) ){ numberOfItems = 12; }
		if( !sortOrderList.contains(sortOrder) ){ sortOrder = ESortOrder.AZ.getDescription(); }
		
		selectModel.setGameConsoleId(consoleId);
		selectModel.setProductTypeId(typeId);

		// START ADDING TO UI
		final ModalWindow modalAddCompany = ForumUtils.createModalWindow( "modalAddCompany", this, new AddCompanyPage(GameShopPage.this.getPageReference(), this) );
		add(modalAddCompany);

		final ModalWindow modalAddRatingUrl = ForumUtils.createModalWindow( "modalAddRatingUrl", this, new AddRatingUrlPage(GameShopPage.this.getPageReference(), this) );
		add(modalAddRatingUrl);

		// Form for add message button and refresh button
        final Form<String> addForm = new Form<String>("addform");
        addForm.add( createFormModalButton( "addcompany",   "Add Company", 	  modalAddCompany) );
        addForm.add( createFormModalButton( "addratingurl", "Add Rating URL", modalAddRatingUrl) );
       
        addForm.add( createFormButton( "addproduct", 	  "Add Product",   AddGamePage.class) );
        addForm.add( createFormButton( "refresh",    	  "Refresh", 	   GameShopPage.class) );

        addOrReplace(addForm);
	    
        final Form<DDCSelectModel> configForm = new Form<DDCSelectModel>("configform", new CompoundPropertyModel<DDCSelectModel>(selectModel));

        configForm.addOrReplace( createSelectnumberOfItemsDDC(numberOfItems) );
        configForm.addOrReplace(new AVKLabel("numberofitemslabel", EText.NUMBER_OF_ITEMS));

        configForm.addOrReplace( createSelectSortorderDDC(sortOrder) );
        configForm.addOrReplace(new AVKLabel("sortorderlabel", EText.SORT_BY));

        addOrReplace(configForm);
        
        final Form<String> searchForm = new Form<String>("searchform");
        final AutoCompleteTextField<String> searchTitle = new AutoCompleteTextField<String>("searchtitle", new Model<String>()) {
        	private static final long serialVersionUID = 1L;

			@Override
			protected List<String> getChoices(String input)
			{
				List<String> choices = Generics.newArrayList();
				
				int count = 0;
				for (Product product : new ProductDao().listUniqueNames())
				{
					if (product.getName().toLowerCase().contains(input.toLowerCase()))
					{				
						choices.add(product.getName());

						// limits the number of results
						if (++count == 30)
						{
							break;
						}
					}
				}

				return choices;
			}
			

//			@Override
//			protected void onSelected(AjaxRequestTarget target)
//			{
//				String name = this.getModelObject();
//			} 
			
		};

        //searchTitle.add( new AttributeModifier("placeholder", new Model<String>( translator.translate("Search Title") )) );
		searchForm.add( searchTitle );
	    
	    final Button searchButton = new AVKButton("searchbutton", "Search"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				PageParameters params = getPageParameters();

				String search = searchTitle.getInput().trim();
				// remove characters that are not accepted in a SEARCH
				search = search.replace(':', ' ');

				params.set("searchtitle", search);
				params.set("numberofitems", numberOfItems);
				params.set("sortorder", sortOrder);

				// reset console and type, to make sure only results for the search title are shown
				params.set("console", 0);
				params.set("type",    0);
				setResponsePage( GameShopPage.class, params );
				return;
			}
	    };    
	    searchForm.addOrReplace(searchButton);
	    
        addOrReplace(searchForm);

		// Add header text
	    addOrReplace( createHeaderLabel() );
		
	    Label CIBLabel = new AVKLabel("ciblabel", EText.CIB_LABEL );
	    addOrReplace(CIBLabel);
	    
		// Add list of products
		final PageableListView<Product> productListView = createProductListView(consoleId, typeId, title, ESortOrder.getSortOrder(sortOrder));
		addOrReplace(productListView);

		// Add results counter
		addOrReplace( createResultCounter(productListView) );        
        
        addOrReplace( new StatelessPagingNavigator( "navigator", params, pageNumber, productListView ) );       
       
	}

	/*
	 *  Creates a PageableListView containing all the products requested by console and type, or title
	 */
	private PageableListView<Product> createProductListView(int gameConsoleId, int productTypeId, String searchTitle, ESortOrder sortOrder){
		// first, gather all products belonging to the search arguments gameConsoleId, productTypeId, and/or title
		List<Product> productList = GameShopLogics.getProductList(gameConsoleId, productTypeId, searchTitle, sortOrder);

		PageableListView<Product> productListView = new PageableListView<Product>( "productlist", productList, numberOfItems ) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<Product> listItem) {
				final Product	product		 	= listItem.getModelObject();

       			listItem.add( ForumUtils.loadImage(GameShopLogics.getImage(product), "image") );
				
       			CompoundPropertyModel<Product> productModel = new CompoundPropertyModel<Product>(product);
      			listItem.setModel(productModel);
       			
      			String companyAndYear = product.getCompany().getDescription();
      			if (product.getYear() != 0) {
      				companyAndYear +=  " (" + product.getYear() + ")";
      			}
      			
                final MultiLineLabel developer 	 = new MultiLineLabel( "companyandyear", companyAndYear );
                final MultiLineLabel gameConsole = new MultiLineLabel( "gameConsole.description" );
                final MultiLineLabel name 		 = new MultiLineLabel( "name" );
        		final MultiLineLabel description = new MultiLineLabel( "htmlDescription" );
        		description.setEscapeModelStrings(false);

        		int rating = 0;
        		String url = "";
        		
    			if(product.getProductRatings().size() > 0){
    				ProductRating productRating = product.getProductRatings().iterator().next();
    				rating = productRating.getRating();
        			url = productRating.getRatingUrl().getUrl();   				
    			};

    			final Label  ratingTextLabel	 = new AVKLabel( "ratingText", "Rating #" );
    			final Label	 ratingLabel 		 = new Label( "rating", rating + " - " );
    			final ExternalLink ratingUrlLink = new ExternalLink( "ratingUrl", url, url );
    			
    			if(rating == 0){
    				ratingTextLabel.setVisible(false);
    				ratingLabel.setVisible(false);
    				ratingUrlLink.setVisible(false);
    			}
    			
    			// when administrator, all the data is shown as editable fields, so hide the non-editable labels
    			// as it only uses up space (which is annoying when you are mass editing a lot of data
    			if(isAdministrator(getActiveUser())) {
    				developer.setVisible(false);
    				gameConsole.setVisible(false);
    				name.setVisible(false);
    				description.setVisible(false);
    				ratingLabel.setVisible(false);
    				ratingTextLabel.setVisible(false);
    				ratingUrlLink.setVisible(false);
    			}
    			
    			listItem.add(developer);
                listItem.add(gameConsole);
                listItem.add(name);
                listItem.add(description);
                listItem.add(ratingLabel);
                listItem.add(ratingTextLabel);
                listItem.add(ratingUrlLink);
        		
        		// Form for productImage
        		listItem.add(createImageUploadForm(listItem));

        		// Form for product info
        		listItem.add(createProductInfoForm(this, listItem));
        		
			}
        };   
        
        // setReuseItems( true ) is very important. Each time a page renders, the listview is rebuild with new IDs, making it impossible 
        // to address the items for, say, visibility. 
        productListView.setReuseItems( true ); 

		return productListView;
	}

	/*
	 * Creates a Form for modifying the product in listItem
	 */
	private Form<Product> createProductInfoForm(PageableListView<Product> view, ListItem<Product> listItem){
		final Product product = listItem.getModelObject();

		Form<Product> productInfoForm = new Form<Product>("productinfoform");

        final TextField<String> editName = new TextField<String>( "editname" );
        editName.setModel( new PropertyModel<String>(product, "name") );
        editName.setOutputMarkupId(true);
        productInfoForm.add(editName);
        
		final TextArea<String> editDescription = new TextArea<String>( "editdescription" );
		editDescription.setModel( new PropertyModel<String>(product, "description") );
		editDescription.setOutputMarkupId(true);
		productInfoForm.add(editDescription);

        final TextField<String> editYear = new TextField<String>( "edityear" );
        editYear.setModel( new PropertyModel<String>(product, "year") );
        editYear.setOutputMarkupId(true);
        productInfoForm.add(editYear);

		productInfoForm.add( new CompanyDDC(	"companies", 	product, "company", 	true  /*isAutoSave*/ ).create() );
		productInfoForm.add( new ProductTypeDDC("producttypes", product, "productType", true /*isAutoSave*/ ).create() );
		productInfoForm.add( new GameConsoleDDC("gameconsoles", product, "gameConsole", true /*isAutoSave*/ ).create() );

		Button modifyRatingButton = new AVKButton( "modifyratingbutton", "Modify Rating" ){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(){
				int codeProduct = product.getCode();
				PageParameters params = new PageParameters();
				params.set( "codeProduct", codeProduct);
				setResponsePage( ModifyGameRatingPage.class, params );
				return;
			}
		};
		productInfoForm.add(modifyRatingButton);
				
		productInfoForm.add(createDeleteButton(product));
		productInfoForm.add(createSaveEditButton(productInfoForm, listItem));
        productInfoForm.setVisible(isAdministrator(getActiveUser()));

        return productInfoForm;
	}
	
	/*
	 * Creates a from for uploading an image in a listItem
	 */
	private Form<ProductImage> createImageUploadForm(ListItem<Product> listItem){
		// Form for productImage
		final Form<ProductImage> uploadForm = new Form<ProductImage>( "uploadform" );
		uploadForm.setOutputMarkupId(true);
		
		final FileUploadField imageFile = new FileUploadField( "productImage" );
		imageFile.setOutputMarkupId(true);
		uploadForm.add(imageFile);

		Button uploadButton 			= createUploadButton(listItem, imageFile);
		uploadForm.add(uploadButton);

		uploadForm.setVisible(isAdministrator(getActiveUser()));
		return uploadForm;
	}
	
	
	private Button createUploadButton(final ListItem<Product> listItem, final FileUploadField imageFile){
		Button uploadButton = new Button( "uploadbutton" ){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				if( !GameShopLogics.uploadFile(listItem, imageFile)){
					setErrorMessage( EText.UPLOAD_FAILED );
				};
				setResponsePage(GameShopPage.class, getPageParameters());
				return;
			}
		};
		uploadButton.setDefaultFormProcessing( false );
		uploadButton.setOutputMarkupId(true);
		return uploadButton;
	}

	private Button createSaveEditButton(final Form<Product> form, final ListItem<Product> listItem){
		final Product product = listItem.getModelObject();
		Button saveEditButton = new AVKButton( "saveedit", "Save" ) {
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("unchecked")
			public void onSubmit() {
				final TextField<String> editNameTF	 		= (TextField<String>) form.get("editname");
				final TextArea<String> editDescriptionTF	= (TextArea<String>) form.get("editdescription");
				final TextField<String> editYearTF	 		= (TextField<String>) form.get("edityear");

				String editName		 	= ForumUtils.getInput( editNameTF ); 
				String editDescription	= ForumUtils.getInput( editDescriptionTF );
				String editYear		 	= ForumUtils.getInput( editYearTF ); 
				
				product.setName(editName);
				product.setDescription(editDescription);
				
				int year = 0;
				try {
					year = Integer.parseInt(editYear);
				} catch (NumberFormatException e) {
					String errorMessage = "Rating should be a number";
					getSession().setAttribute( "errormessage", errorMessage );
					return; // necessary, because otherwise the below lines will also be triggered.
				}
				
				product.setYear(year);
								
				DBHelper.saveAndCommit(product);
			}
	    };   
	    saveEditButton.setDefaultFormProcessing( false );
	    saveEditButton.setOutputMarkupId(true);
	    return saveEditButton;
	}
	
	private Button createDeleteButton(final Product product){
        Button deleteButton = new AVKButton("deleteproductbutton", "Delete Product"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				GameShopLogics.deleteAllImages(product);
				setResponsePage(GameShopPage.class);
				return;
		    }
        };
        deleteButton.setVisible(isAdministrator(getActiveUser()));
        deleteButton.setDefaultFormProcessing( false );
        deleteButton.setOutputMarkupId(true);
        return deleteButton;
	}

	private DropDownChoice<Integer> createSelectnumberOfItemsDDC(int numberOfItems){
		final IModel<Integer> myModel   = new Model<Integer>(numberOfItems);

		DropDownChoice<Integer> numberOfItemsDDC = new DropDownChoice<Integer>( "numberofitems", myModel, numberOfItemsList );
		numberOfItemsDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();

				params.set("type", 	  typeId);
				params.set("console", consoleId);
				params.set("sortorder", sortOrder);

				Integer numberOfItems = (Integer) myModel.getObject();
				params.set("numberofitems", numberOfItems);			

				setResponsePage( new GameShopPage(params) );
				return;
			}
		});
		
		numberOfItemsDDC.setOutputMarkupId(true);
		return numberOfItemsDDC;
	}

	private DropDownChoice<String> createSelectSortorderDDC(String sortOrder){
		final IModel<String> myModel   = new Model<String>(sortOrder);

		DropDownChoice<String> sortOrderDDC = new DropDownChoice<String>( "sortorder", myModel, sortOrderList );
		sortOrderDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();

				params.set("type", 	  typeId);
				params.set("console", consoleId);
				params.set("numberofitems", numberOfItems);

				String sortOrder = (String) myModel.getObject();
				params.set("sortorder", sortOrder);

				setResponsePage( new GameShopPage(params) );
				return;
			}
		});
		
		sortOrderDDC.setOutputMarkupId(true);
		return sortOrderDDC;
	}

	/*
	 * The below function creates form buttons, like 'Add Product'. 
	 * @Arguments: id - name in html, like addproductbutton, but without the 'button' text, so we can reuse addproduct for the label  
	 */
	private ResponseFormButton createFormButton( String id, String buttonText, Class<? extends BasePage> destinationPage ){
        ResponseFormButton formButton = new ResponseFormButton(id + "button", buttonText, destinationPage, getPageParameters(), isAdministrator(getActiveUser()));
        formButton.setOutputMarkupId(true);

        final Label buttonLabel = new AVKLabel( id + "label", buttonText );
        formButton.add(buttonLabel);

        return formButton;
	}

	/*
	 * The below function creates form buttons, like 'Add Company', which will open a MODAL WINDOW when clicked. 
	 * @Arguments: id - name in html, like addproductbutton, but without the 'button' text, so we can reuse addproduct for the label  
	 */
	private ResponseFormModalButton createFormModalButton( String id, String buttonText, ModalWindow modal ){
        ResponseFormModalButton formButton = new ResponseFormModalButton(id + "button", buttonText, modal, isAdministrator(getActiveUser()));

        final Label buttonLabel = new AVKLabel( id + "label", buttonText );
        formButton.add(buttonLabel);

        return formButton;
	}

	/**
	 * Returns a label containing the header info, which can be a different text depending on the current selections in the window
	 * 
	 *  @param	none
	 *  @return	An initialized Wicket Label
	 */
	private Label createHeaderLabel(){
		Model<String> headerModel = new Model<String>(){
			private static final long serialVersionUID = 1L;
			
			@Override 
			public String getObject(){
				if(consoleId == 0 && typeId == 0 && StringLogics.isEmpty(title)){
					return translator.translate( EText.RECENTLY_ADDED );
				}

				if(consoleId == 0 && typeId == 0 && !StringLogics.isEmpty(title)){
					return translator.translate( EText.SEARCH_RESULTS );
				}

				return "";
			}
			
		};
		
		Label headerLabel = new Label("headerlabel", headerModel);
		return headerLabel;
	}

	/**
	 * Creates a result counter, showing something in the line of "item x to y of z" 
	 * 
	 *  @param	none
	 *  @return	An initialized Wicket Label
	 */
	private Label createResultCounter(final PageableListView<Product> resultListView){
		Model<String> resultCounterModel = new Model<String>(){
			private static final long serialVersionUID = 1L;
			
			@Override 
			public String getObject(){
				long start 			 	  = resultListView.getFirstItemOffset();
				long numberOfResults 	  = resultListView.getItemsPerPage();
				long totalNumberOfResults = resultListView.getItemCount();
				
				long end				  = start + numberOfResults;
				if( (start + numberOfResults) > totalNumberOfResults ){
					end = totalNumberOfResults;
				}
				
				StringBuilder sb = new StringBuilder();
				String space = " ";
				sb.append( String.valueOf(start + 1) )				.append(space)
				  .append("to")										.append(space)
				  .append( String.valueOf(end))						.append(space)
				  .append("of")										.append(space)
				  .append( String.valueOf(totalNumberOfResults) );
				
				return translator.translate(sb.toString());
			}
			
		};
		Label resultCounterLabel = new Label("resultcounter", resultCounterModel);
		return resultCounterLabel;
	}
	
    @Override
    protected String getPageTitle() {
    	return "AVK - Game Collection";
    }

}
