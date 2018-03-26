package com.myforum.security;

import com.myforum.articlepage.ArticlePage;
import com.myforum.base.BasePage;
import com.myforum.forumpages.ForgotPasswordPanel;
import com.myforum.forumpages.ForumAddMessagePanel;
import com.myforum.forumpages.ForumBasePage;
import com.myforum.forumpages.ForumCategoryPanel;
import com.myforum.forumpages.ForumHomePanel;
import com.myforum.forumpages.ForumMessagePanel;
import com.myforum.forumpages.MoveMessagePanel;
import com.myforum.forumpages.UserModifyAccountPage;
import com.myforum.forumpages.UserRegistrationPage;
import com.myforum.forumpages.administrator.ForumAdministratorPage;
import com.myforum.gameshop.AddCompanyPage;
import com.myforum.gameshop.AddGamePage;
import com.myforum.gameshop.GameShopPage;
import com.myforum.gameshop.ModifyGameRatingPage;
import com.myforum.gameshop.codetable.CTCompanyPage;
import com.myforum.gameshop.codetable.CTGameConsolePage;
import com.myforum.gameshop.codetable.CTProductTypePage;
import com.myforum.gameshop.codetable.CodeTablesPage;
import com.myforum.gameshop.codetable.ModifyCompanyPage;
import com.myforum.gameshop.codetable.ModifyGameConsolePage;
import com.myforum.gameshop.codetable.ModifyProductTypePage;
import com.myforum.homepage.AboutMePage;
import com.myforum.homepage.HomePage;
import com.myforum.sourcepages.SourceCategoryPage;
import com.myforum.sourcepages.SourceHomePage;
import com.myforum.sourcepages.SourceSubjectPage;

public class Administrator extends UserRole{

	public Administrator(){
		
		// windowPrivilege( windowId, canRead, canWrite, canOpen ;
		addPrivilege( BasePage.class,				true, true, true );
		
		addPrivilege( AboutMePage.class, 			true, true, true );
		addPrivilege( HomePage.class, 				true, true, true );

		addPrivilege( ArticlePage.class, 			true, true, true );

		addPrivilege( GameShopPage.class, 			true, true, true );
		addPrivilege( AddGamePage.class, 			true, true, true );
		addPrivilege( AddCompanyPage.class, 		true, true, true );
		
		addPrivilege( ForumBasePage.class, 			true, true, true );
		addPrivilege( ForgotPasswordPanel.class, 	true, true, true );
		addPrivilege( ForumAddMessagePanel.class, 	true, true, true );
		addPrivilege( ForumAdministratorPage.class, true, true, true );
		addPrivilege( ForumCategoryPanel.class, 	true, true, true );
		addPrivilege( ForumHomePanel.class, 		true, true, true );
		addPrivilege( ForumMessagePanel.class, 		true, true, true );
		addPrivilege( MoveMessagePanel.class, 		true, true, true );
		addPrivilege( UserModifyAccountPage.class, 	true, true, true );
		addPrivilege( UserRegistrationPage.class, 	true, true, true );

		addPrivilege( SourceCategoryPage.class, 	true, true, true );
		addPrivilege( SourceHomePage.class, 		true, true, true );
		addPrivilege( SourceSubjectPage.class, 		true, true, true );
		addPrivilege( ModifyGameRatingPage.class, 	true, true, true );

		addPrivilege( CodeTablesPage.class,			true, true, true );
		addPrivilege( CTCompanyPage.class, 			true, true, true );
		
		addPrivilege( CTGameConsolePage.class,		true, true, true );
		addPrivilege( CTProductTypePage.class,		true, true, true );
		addPrivilege( ModifyCompanyPage.class, 		true, true, true );
		addPrivilege( ModifyGameConsolePage.class, 	true, true, true );
		addPrivilege( ModifyProductTypePage.class,  true, true, true );

	}
		
}
