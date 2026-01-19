package de.thws.Adapters.web_in;

import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public final class LinkHeaderSupport {
    private LinkHeaderSupport() {
    }

    public static List<Link> resourceLinks(UriInfo uriInfo, String resourcePath, Long id) {
        URI resourceUri = resourceUri(uriInfo, resourcePath, id);
        List<Link> links = new ArrayList<>(3);
        links.add(buildLink(resourceUri, "self"));
        links.add(buildLink(resourceUri, "update"));
        links.add(buildLink(resourceUri, "delete"));
        return links;
    }

    public static List<Link> actionLinks(UriInfo uriInfo, String resourcePath, Long id) {
        URI resourceUri = resourceUri(uriInfo, resourcePath, id);
        List<Link> links = new ArrayList<>(2);
        links.add(buildLink(resourceUri, "update"));
        links.add(buildLink(resourceUri, "delete"));
        return links;
    }

    public static List<Link> collectionLinks(
            UriInfo uriInfo,
            int page,
            int size,
            boolean hasNext,
            boolean hasPrev) {
        List<Link> links = new ArrayList<>(3);
        links.add(buildLink(uriInfo.getRequestUriBuilder().build(), "self"));
        if (hasNext) {
            links.add(buildLink(pagedUri(uriInfo, page + 1, size), "next"));
        }
        if (hasPrev) {
            links.add(buildLink(pagedUri(uriInfo, page - 1, size), "prev"));
        }
        return links;
    }

    public static String resourceHref(UriInfo uriInfo, String resourcePath, Long id) {
        return resourceUri(uriInfo, resourcePath, id).toString();
    }

    private static Link buildLink(URI uri, String rel) {
        return Link.fromUri(uri).rel(rel).type(MediaType.APPLICATION_JSON).build();
    }

    private static URI resourceUri(UriInfo uriInfo, String resourcePath, Long id) {
        return uriInfo.getBaseUriBuilder()
                .path(resourcePath)
                .path(String.valueOf(id))
                .build();
    }

    private static URI pagedUri(UriInfo uriInfo, int page, int size) {
        UriBuilder builder = uriInfo.getRequestUriBuilder()
                .replaceQueryParam("page", page)
                .replaceQueryParam("size", size);
        return builder.build();
    }
}
