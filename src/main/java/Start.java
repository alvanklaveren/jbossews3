
//import org.apache.wicket.util.time.Duration;
//import org.eclipse.jetty.server.Handler;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.server.bio.SocketConnector;
//import org.eclipse.jetty.server.handler.ContextHandler;
//import org.eclipse.jetty.server.handler.ContextHandlerCollection;
//import org.eclipse.jetty.server.ssl.SslSocketConnector;
//import org.eclipse.jetty.util.resource.Resource;
//import org.eclipse.jetty.util.ssl.SslContextFactory;
//import org.eclipse.jetty.webapp.WebAppContext;

//import com.myforum.application.DBHelper;
//import com.myforum.mobile.ForumHandler;

/*
 *		This is the JETTY server startup. Since everything has moved to Tomcat, this is sort of obsolete. 
 */
public class Start {
	
//	/* moved to bitbucket */
//	static int timeout;
//	static int selectedPort = 8085;
//	static Server server;
//	static SocketConnector connector;
//	static Resource keystore;
//	static WebAppContext webAppContext;
//	static ContextHandler mobileContextHandler;
//	static ContextHandlerCollection contexts;
//
//	public Start(){}
//	
//	public static void main(String[] args) throws Exception {            
//        if(args.length > 0){
//        	try{
//        		selectedPort = Integer.parseInt( args[0] );
//        	} catch ( Exception e ){ 
//        		System.out.println("arg0: "+args[0]);
//        		System.out.println("Usage: java Start [port]. Port is an integer e.g. java Start 80. If no port defined, default port used is 8080");
//        	}
//        }
//
//        timeout = (int) Duration.ONE_HOUR.getMilliseconds();
//
//        System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
//
//        // if it still running, first stop server
//    	if( server != null ){
//	        if( server.isRunning() ){ 
//	        	try {
//				server.stop();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//	        }
//    	}
//
//        StartServer();
//            
//        try {
//            System.in.read();
//            System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
//            server.stop();
//            server.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
//
//    public static void StartServer(){   	
//    	server = new Server();
//        connector = new SocketConnector();    
//
//        // Set some timeout options to make debugging easier.
//        connector.setMaxIdleTime(timeout);
//        connector.setSoLingerTime(-1);
//        connector.setPort(selectedPort);
//        server.addConnector(connector);
//
//        keystore = Resource.newClassPathResource("/keystore");
//        if (keystore != null && keystore.exists()) {
//            // if a keystore for a SSL certificate is available, start a SSL
//            // connector on port 8443.
//            // By default, the quickstart comes with a Apache Wicket Quickstart
//            // Certificate that expires about half way september 2021. Do not
//            // use this certificate anywhere important as the passwords are
//            // available in the source.
//
//            connector.setConfidentialPort(8443);
//
//            SslContextFactory factory = new SslContextFactory();
//            factory.setKeyStoreResource(keystore);
//            factory.setKeyStorePassword("wicket");
//            factory.setTrustStoreResource(keystore);
//            factory.setKeyManagerPassword("wicket");
//            SslSocketConnector sslConnector = new SslSocketConnector(factory);
//            sslConnector.setMaxIdleTime(timeout);
//            sslConnector.setPort(8443);
//            sslConnector.setAcceptors(4);
//            server.addConnector(sslConnector);
//
//            System.out.println("SSL access to the quickstart has been enabled on port 8443");
//            System.out.println("You can access the application using SSL on https://localhost:8443");
//            System.out.println();
//        }
//
//        webAppContext = new WebAppContext();
//        webAppContext.setServer(server);
//        webAppContext.setContextPath("/");
//        webAppContext.setWar("webapp");
//
//        // START JMX SERVER
//         //MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
//         //MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
//         //server.getContainer().addEventListener(mBeanContainer);
//         //mBeanContainer.start();
//        //server.setHandler(bb);
//      
//        // TODO: slashed out to try and fix openshift 
//        //bbMobile = new ContextHandler("/mobile");
//        //bbMobile.setHandler(new ForumHandler());
// 
//        contexts = new ContextHandlerCollection();
//        //contexts.setHandlers(new Handler[] { bb, bbMobile });
//        contexts.setHandlers(new Handler[] { webAppContext });
//        
//        server.setHandler(contexts);
//        
//        DBHelper.getInstance(); // starts the configuration build
//               
//        try {
//			server.start();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//    }
        
}
