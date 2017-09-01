//package com.myforum.gameshop.REST;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.wicket.spring.injection.annot.SpringBean;
//import org.wicketstuff.rest.annotations.MethodMapping;
//import org.wicketstuff.rest.annotations.ResourcePath;
//import org.wicketstuff.rest.contenthandling.IWebSerialDeserial;
//import org.wicketstuff.rest.contenthandling.mimetypes.RestMimeTypes;
//import org.wicketstuff.rest.resource.AbstractRestResource;
//import org.wicketstuff.rest.utils.http.HttpMethod;
//
//import com.myforum.springframework.GameShopService;
//import com.myforum.tables.Product;
//
//@ResourcePath("/products")
//public class GameShopResource extends AbstractRestResource<IWebSerialDeserial>{
//	private static final long serialVersionUID = 1L;
//
//	private List<Product> productList = new ArrayList<Product>();
//
//	@SpringBean
//	GameShopService gameShopService;
//	
//	// Enter using http://localhost:8080/gameshopmobile/
//	public GameShopResource(IWebSerialDeserial serialDeserial) {
//		super(serialDeserial);
//		// TODO Auto-generated constructor stub
//	}
//
//	@MethodMapping(value = "/products/{gameConsoleId}/{productTypeId}", httpMethod = HttpMethod.GET, produces = RestMimeTypes.APPLICATION_JSON)
//	public List<Product> getProducts(int gameConsoleId, int productTypeId){
//		productList = gameShopService.getProducts(gameConsoleId, productTypeId);
//
//		for(Product product:productList){
//	    	System.out.println(product.getName());
//	    }
//		return productList;
//	}
//
//	//@MethodMapping(value = "/products", httpMethod = HttpMethod.POST)
//	//      e.g. create new product public Product createProduct(@RequestBody Product product){
//	
//	//@MethodMapping(value = "/products/{gameConsoleId}/{productTypeId}", httpMethod = HttpMethod.DELETE)
//	
//}
