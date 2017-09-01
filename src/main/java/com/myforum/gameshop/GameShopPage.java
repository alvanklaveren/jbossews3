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

import com.myforum.application.DBHelper;
import com.myforum.application.ForumUtils;
import com.myforum.application.StringLogics;
import com.myforum.base.BasePage;
import com.myforum.base.dictionary.EText;
import com.myforum.base.menu.EMenuItem;
import com.myforum.framework.ErrorLabel;
import com.myforum.framework.ResponseFormButton;
import com.myforum.framework.StatefulPagingNavigator;
import com.myforum.gameshop.DDC.CompanyDDC;
import com.myforum.gameshop.DDC.GameConsoleDDC;
import com.myforum.gameshop.DDC.ProductTypeDDC;
import com.myforum.tables.GameConsole;
import com.myforum.tables.Product;
import com.myforum.tables.ProductImage;
import com.myforum.tables.ProductRating;
import com.myforum.tables.ProductType;
import com.myforum.tables.dao.GameConsoleDao;
import com.myforum.tables.dao.ProductTypeDao;

public class GameShopPage extends BasePage {
	private static final long serialVersionUID = 1L;

	private 		int	   			consoleId, typeId, numberOfItems;
	private 		String 			title 				= null;
	private final 	DDCSelectModel 	selectModel 		= new DDCSelectModel();
	private 		List<Integer> 	numberOfItemsList 	= new ArrayList<Integer>();
	
	private 		ModalWindow		modalAddRatingUrl;
	
	public GameShopPage(PageParameters params) {
		super(EMenuItem.GameShop);

		numberOfItemsList.add(15);
		numberOfItemsList.add(25);
		numberOfItemsList.add(50);
		numberOfItemsList.add(75);
		numberOfItemsList.add(100);

		addOrReplace( new ErrorLabel() );
		addOrReplace( new GameShopPanelLeft("sidepanelleft") );
		
		consoleId 		= ForumUtils.getParmInt(params,    "console",        0);
		typeId 			= ForumUtils.getParmInt(params,    "type",           0);
		title			= ForumUtils.getParmString(params, "searchtitle",   "");
		numberOfItems	= ForumUtils.getParmInt(params,    "numberofitems", 15);

		if( !numberOfItemsList.contains(numberOfItems) ){ numberOfItems = 15; }
		
		// when looking for a title, ignore the gameConsole and productType selection in the dropdownlists
		if(!StringLogics.isEmpty(title)){
	        getPageParameters().remove("searchtitle");
	        consoleId 	= 0;
	        typeId 		= 0;
		}

		selectModel.setGameConsoleId(consoleId);
		selectModel.setProductTypeId(typeId);

		// START ADDING TO UI
		final ModalWindow modalAddCompany = ForumUtils.createModalWindow( "modalAddCompany", this, new AddCompanyPage(GameShopPage.this.getPageReference(), this) );
		add(modalAddCompany);

		final ModalWindow modalAddRatingUrl = ForumUtils.createModalWindow( "modalAddRatingUrl", this, new AddRatingUrlPage(GameShopPage.this.getPageReference(), this) );
		add(modalAddRatingUrl);

		// Form for add message button and refresh button
        final Form<String> addForm = new Form<String>("addform");
        addForm.add( createFormModalButton( "addcompany",   translator.translate("Add Company"), modalAddCompany) );
        addForm.add( createFormModalButton( "addratingurl", translator.translate("Add Rating URL"), modalAddRatingUrl) );
        
        addForm.add( createFormButton( "addproduct", 	  translator.translate("Add Product"), AddGamePage.class) );
        addForm.add( createFormButton( "refresh",    	  translator.translate("Refresh"), 	   GameShopPage.class) );
        addOrReplace(addForm);
	    
        final Form<DDCSelectModel> searchForm 	= new Form<DDCSelectModel>("searchform", new CompoundPropertyModel<DDCSelectModel>(selectModel));
	    final TextField<String> searchTitle 	= new TextField<String>("searchtitle", new Model<String>(title));
	    final Label gameConsolesLabel 			= new Label("gameconsoleslabel", new Model<String>(translator.translate("Game Console")));
	    final Label productTypesLabel 			= new Label("producttypeslabel", new Model<String>(translator.translate("Product Type")));
	    final Label numberofitemslabel 			= new Label("numberofitemslabel", new Model<String>(translator.translate("Number of Items")));

	    DropDownChoice<GameConsole> ddcGameConsoles = createSelectGameConsolesDDC(consoleId);
	    DropDownChoice<ProductType> ddcProductTypes = createSelectProductTypesDDC(typeId);

	    // better use the left side menu to navigate instead of these dropdowns
	    gameConsolesLabel.setVisible(false);
	    productTypesLabel.setVisible(false);
	    ddcProductTypes.setVisibilityAllowed(false);
	    ddcGameConsoles.setVisibilityAllowed(false);

	    searchForm.addOrReplace(searchTitle);
	    searchForm.addOrReplace( ddcGameConsoles );
	    searchForm.addOrReplace( ddcProductTypes );
	    searchForm.addOrReplace( createSelectnumberOfItemsDDC(numberOfItems) );

	    searchForm.addOrReplace(gameConsolesLabel);
	    searchForm.addOrReplace(productTypesLabel);
	    searchForm.addOrReplace(numberofitemslabel);
	    
	    final Button searchButton = new Button( "searchbutton"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				getPageParameters().set("searchtitle", ForumUtils.getInput(searchTitle));
				getPageParameters().set("numberofitems", numberOfItems);
				setResponsePage( new GameShopPage(getPageParameters()) );
				return;
			}
	    };    
	    final Label searchLabel = new Label( "searchlabel", new Model<String>(translator.translate("Search")) );
	    searchButton.addOrReplace(searchLabel);
	    searchForm.addOrReplace(searchButton);
	    
	    addOrReplace(searchForm);

		// Add header text
	    addOrReplace( createHeaderLabel() );
			
		// Add list of products
		final PageableListView<Product> productListView = createProductListView(consoleId, typeId, title);
		addOrReplace(productListView);

		// Add results counter
		addOrReplace( createResultCounter(productListView) );        
        
		addOrReplace(new StatefulPagingNavigator( "navigatortop", productListView ));       
		addOrReplace(new StatefulPagingNavigator( "navigator",    productListView ));       
        
	}

	/*
	 *  Creates a PageableListView containing all the products requested by console and type, or title
	 */
	private PageableListView<Product> createProductListView(int gameConsoleId, int productTypeId, String searchTitle){
		// first, gather all products belonging to the search arguments gameConsoleId, productTypeId, and/or title
		List<Product> productList = GameShopLogics.getProductList(gameConsoleId, productTypeId, searchTitle);

		PageableListView<Product> productListView = new PageableListView<Product>( "productlist", productList, numberOfItems ) {
			private static final long serialVersionUID = 1L;

			@Override
            public void populateItem(final ListItem<Product> listItem) {
				final Product	product		 	= listItem.getModelObject();

       			listItem.add( ForumUtils.loadImage(GameShopLogics.getImage(product), "image") );
				
       			CompoundPropertyModel<Product> productModel = new CompoundPropertyModel<Product>(product);
      			listItem.setModel(productModel);
       			
                final MultiLineLabel developer 	 = new MultiLineLabel( "company.description" );
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

    			final Label  ratingTextLabel	 = new Label( "ratingText", translator.translate("Rating") + " #" );
    			final Label	 ratingLabel 		 = new Label( "rating", rating + " - " );
    			final ExternalLink ratingUrlLink = new ExternalLink( "ratingUrl", url, url );
    			
    			if(rating == 0){
    				ratingTextLabel.setVisible(false);
    				ratingLabel.setVisible(false);
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

		productInfoForm.add( new CompanyDDC(	"companies", 	product, "company", 	true  /*isAutoSave*/ ).create() );
		productInfoForm.add( new ProductTypeDDC("producttypes", product, "productType", true /*isAutoSave*/ ).create() );
		productInfoForm.add( new GameConsoleDDC("gameconsoles", product, "gameConsole", true /*isAutoSave*/ ).create() );

		productInfoForm.add( createFormModalButton( "addratingurl", translator.translate("Add Rating URL"), modalAddRatingUrl ) );
	
		Button modifyRatingButton = new Button( "modifyratingbutton" ){
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
		modifyRatingButton.setDefaultModel( new Model<String>(translator.translate("Modify Rating")) );
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
			}
		};
		uploadButton.setDefaultFormProcessing( false );
		uploadButton.setOutputMarkupId(true);
		return uploadButton;
	}

	private Button createSaveEditButton(final Form<Product> form, final ListItem<Product> listItem){
		final Product product = listItem.getModelObject();
		Button saveEditButton = new Button( "saveedit" ) {
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("unchecked")
			public void onSubmit() {
				final TextField<String> editNameTF	 		= (TextField<String>) form.get("editname");
				final TextArea<String> editDescriptionTF	= (TextArea<String>) form.get("editdescription");

				String editName		 	= ForumUtils.getInput( editNameTF ); 
				String editDescription	= ForumUtils.getInput( editDescriptionTF );
				
				product.setName(editName);
				product.setDescription(editDescription);
				
				DBHelper.saveAndCommit(product);
			}
	    };   
	    saveEditButton.setDefaultFormProcessing( false );
	    saveEditButton.setOutputMarkupId(true);
	    return saveEditButton;
	}
	
	private Button createDeleteButton(final Product product){
        Button deleteButton = new Button("deleteproductbutton"){
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

	private DropDownChoice<ProductType> createSelectProductTypesDDC(int productTypeId){
		List<ProductType> productTypes = new ProductTypeDao().list();
		final IModel<ProductType> myModel = new Model<ProductType>(new ProductTypeDao().find(productTypeId));
		DropDownChoice<ProductType> productTypesDDC = new DropDownChoice<ProductType>( "producttypes", myModel, productTypes );
		productTypesDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();
				params.set("console", 		consoleId);
				params.set("numberofitems", numberOfItems);

				ProductType productType = (ProductType) myModel.getObject();
				params.set("type", productType.getCode());			
				setResponsePage( new GameShopPage(params) );
				return;
			}
		});
		productTypesDDC.setOutputMarkupId(true);
		return productTypesDDC;
	}
	
	private DropDownChoice<GameConsole> createSelectGameConsolesDDC(int gameConsoleId){
		List<GameConsole> gameConsoles = new GameConsoleDao().list();
		final IModel<GameConsole> myModel = new Model<GameConsole>(new GameConsoleDao().find(gameConsoleId));
		DropDownChoice<GameConsole> gameConsolesDDC = new DropDownChoice<GameConsole>( "gameconsoles", myModel, gameConsoles );
		gameConsolesDDC.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target) {
				PageParameters params = new PageParameters();
				params.set("type", 			typeId);
				params.set("numberofitems", numberOfItems);
				
				GameConsole gameConsole = (GameConsole) myModel.getObject();
				params.set("console", gameConsole.getCode());
				setResponsePage( new GameShopPage(params) );
				return;
			}
		});
		gameConsolesDDC.setOutputMarkupId(true);
		return gameConsolesDDC;
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

				Integer numberOfItems = (Integer) myModel.getObject();
				params.set("numberofitems", numberOfItems);			
				setResponsePage( new GameShopPage(params) );
				return;
			}
		});
		
		numberOfItemsDDC.setOutputMarkupId(true);
		return numberOfItemsDDC;
	}
	
	/*
	 * The below function creates form buttons, like 'Add Product'. 
	 * @Arguments: id - name in html, like addproductbutton, but without the 'button' text, so we can reuse addproduct for the label  
	 */
	private ResponseFormButton createFormButton( String id, String buttonText, Class<? extends BasePage> destinationPage ){
        ResponseFormButton formButton = new ResponseFormButton(id + "button", destinationPage, getPageParameters(), isAdministrator(getActiveUser()));
        formButton.setOutputMarkupId(true);

        final Label buttonLabel = new Label( id + "label", new Model<String>(translator.translate(buttonText)) );
        formButton.add(buttonLabel);

        return formButton;
	}

	/*
	 * The below function creates form buttons, like 'Add Product'. 
	 * @Arguments: id - name in html, like addproductbutton, but without the 'button' text, so we can reuse addproduct for the label  
	 */
	private ResponseFormModalButton createFormModalButton( String id, String buttonText, ModalWindow modal ){
        ResponseFormModalButton formButton = new ResponseFormModalButton(id + "button", modal, isAdministrator(getActiveUser()));
        formButton.setOutputMarkupId(true);

        final Label buttonLabel = new Label( id + "label", new Model<String>(translator.translate(buttonText)) );
        formButton.add(buttonLabel);

        return formButton;
	}

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
				
				return translator.translateFullSentence(sb.toString());
			}
			
		};
		Label resultCounterLabel = new Label("resultcounter", resultCounterModel);
		return resultCounterLabel;
	}
}
