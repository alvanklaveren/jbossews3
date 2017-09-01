package com.myforum.JAXRS.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/message")
public class HelloPojo {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/{param}")
    public Response sendMessage(@PathParam("param") String name) {
		String result = "Hello " + name;
		if(name.isEmpty()){
			result = "Hello galaxy!";
		}
		System.out.println("doet ie het ?"); 

        return Response.status(200).entity(result).build();
    }

}
