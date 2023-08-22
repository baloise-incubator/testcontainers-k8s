package com.baloise.codecamp.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class BookService {

	public List<Book> list() {
		return Book.listAll();
	}

	public Book findById(Long id) {
		return Book.findById(id);
	}

}
