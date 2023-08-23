package com.baloise.codecamp.quarkus;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {
	@Inject
	BookService bookService;

	@GET
	public List<Book> list() {
		List<Book> list = bookService.list();
		return list;
	}

	@GET
	@Path("{id}")
	public Book findById(@PathParam("id") Long id) {
		return bookService.findById(id);
	}

}
