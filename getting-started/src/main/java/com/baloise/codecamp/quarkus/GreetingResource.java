package com.baloise.codecamp.quarkus;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @Inject
    BookService books;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        int bookscount = books.list().size();
        return String.format("Hello from RESTEasy Reactive - there are %s books in the store", bookscount);
    }
}
