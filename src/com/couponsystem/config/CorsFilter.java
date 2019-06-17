package com.couponsystem.config;

import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class CorsFilter implements ContainerResponseFilter{

	public ContainerResponse filter(ContainerRequest req, ContainerResponse crunchifyContainerResponse) {
		 
        ResponseBuilder crunchifyResponseBuilder = Response.fromResponse(crunchifyContainerResponse.getResponse());
        
        // *(allow from all servers) OR https://crunchify.com/ OR http://example.com/
        crunchifyResponseBuilder.header("Access-Control-Allow-Origin", "*")
        
        // As a part of the response to a request, which HTTP methods can be used during the actual request.
        .header("Access-Control-Allow-Methods", "GET, POST")
        
        // How long the results of a request can be cached in a result cache.
        .header("Access-Control-Max-Age", "151200")
        
        // As part of the response to a request, which HTTP headers can be used during the actual request.
        .header("Access-Control-Allow-Headers", "x-requested-with,Content-Type");
 
        String crunchifyRequestHeader = req.getHeaderValue("Access-Control-Request-Headers");
 
        if (null != crunchifyRequestHeader && !crunchifyRequestHeader.equals(null)) {
            crunchifyResponseBuilder.header("Access-Control-Allow-Headers", crunchifyRequestHeader);
        }
 
        crunchifyContainerResponse.setResponse(crunchifyResponseBuilder.build());
        return crunchifyContainerResponse;
    }
}