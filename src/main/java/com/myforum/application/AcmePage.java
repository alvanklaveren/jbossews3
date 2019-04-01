package com.myforum.application;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebResponse;



public class AcmePage extends WebPage {
	private static final long serialVersionUID = 1L;
	
	public AcmePage() {
		// this page technically has a pageparameter called "acme_address", defined in AVKApplication.class
		// but we technically do not need it, because we fake the file name (stored in acme_address), and return
		// the so said content of that file using the constant stored in "acme"
	}

	@Override
    public final void renderPage() {
        WebResponse response = (WebResponse) getResponse();
        response.setContentType("text/plain");
        //response.write("1LXCE9UAw5-3ciPOFvsnLvexpQLtIQRc3KJYIpt9amU.I0A7SKECn5ad74BvNrjI_eA_5t1BVZGU6gwbqrZl8Zw");
        
        
        // acme in constants contains the verification code from the HTTPS provider to verify that I am who I say I am
        String acme = AllConstants.getConstantStringValue("acme");
        if(StringLogics.isEmpty(acme)){
        	acme = "ID Lost";
        }
        
        response.write(acme);
    }	
}
