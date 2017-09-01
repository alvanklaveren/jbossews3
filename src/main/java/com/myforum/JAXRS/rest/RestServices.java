package com.myforum.JAXRS.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/services")
public class RestServices extends Application {
    private Set<Object> singletons = new HashSet<Object>();

    public RestServices() {
        singletons.add(new HelloPojo());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
