package com.myforum.security;

import com.myforum.articlepage.ArticlePage;
import com.myforum.base.AVKPage;
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

public class Guest extends UserRole{

	public Guest(){
		
		// windowPrivilege( windowId, canRead, canWrite, canOpen ;
		addPrivilege( AVKPage.class,				true, false, true );
		
		addPrivilege( AboutMePage.class, 			true, false, true );
		addPrivilege( HomePage.class, 				true, false, true );

		addPrivilege( ArticlePage.class, 			true, false, true );

		addPrivilege( GameShopPage.class, 			true, false, true );
		addPrivilege( AddGamePage.class, 			true, false, false );
		addPrivilege( AddCompanyPage.class, 		true, false, false );

		addPrivilege( ForumBasePage.class, 			true, false, true );
		addPrivilege( ForgotPasswordPanel.class, 	true, false, true );
		addPrivilege( ForumAddMessagePanel.class, 	true, false, false );
		addPrivilege( ForumAdministratorPage.class, true, false, false );
		addPrivilege( ForumCategoryPanel.class, 	true, false, true );
		addPrivilege( ForumHomePanel.class, 		true, false, true );
		addPrivilege( ForumMessagePanel.class, 		true, false, true );
		addPrivilege( MoveMessagePanel.class, 		true, false, false );
		addPrivilege( UserModifyAccountPage.class, 	true, false, false );
		addPrivilege( UserRegistrationPage.class, 	true, true,  true );

		addPrivilege( SourceCategoryPage.class, 	true, false, true );
		addPrivilege( SourceHomePage.class, 		true, false, true );
		addPrivilege( SourceSubjectPage.class, 		true, false, true );
		addPrivilege( ModifyGameRatingPage.class, 	false, false, false );
		
		addPrivilege( CodeTablesPage.class,			false, false, false );
		addPrivilege( CTCompanyPage.class, 			false, false, false );
		addPrivilege( CTGameConsolePage.class,		false, false, false );
		addPrivilege( CTProductTypePage.class,		false, false, false );
		addPrivilege( ModifyCompanyPage.class, 		false, false, false );
		addPrivilege( ModifyGameConsolePage.class, 	false, false, false );
		addPrivilege( ModifyProductTypePage.class,  false, false, false );

	}
		
}
