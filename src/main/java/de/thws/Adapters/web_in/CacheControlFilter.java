package de.thws.Adapters.web_in;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CacheControlFilter implements ContainerResponseFilter {
    private static final String CACHE_CONTROL_VALUE = "private, max-age=60";

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        if (!"GET".equalsIgnoreCase(requestContext.getMethod())) {
            return;
        }
        responseContext.getHeaders().putSingle(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_VALUE);
    }
}
