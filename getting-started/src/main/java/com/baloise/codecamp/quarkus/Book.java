package com.baloise.codecamp.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import jakarta.persistence.*;

@Entity
@Table(name = "book")
public class Book extends PanacheEntityBase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	public String title;
	public String author;

	public static void add(String title, String author) {
		Book book = new Book();
		book.title = title;
		book.author = author;
		book.persist();
	}

}
