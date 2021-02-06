package com.algaworks.algafood.api;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ResourceUriHelper {

	public static void addUriInResponseHeader(Object resourceId) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(resourceId).toUri();
	
		HttpServletResponse response = ((ServletRequestAttributes)
				RequestContextHolder.getRequestAttributes()).getResponse();
		
		response.setHeader(HttpHeaders.LOCATION, uri.toString());
	}
	
	
}
