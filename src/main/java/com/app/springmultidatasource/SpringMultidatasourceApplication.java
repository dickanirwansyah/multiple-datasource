package com.app.springmultidatasource;

import com.app.springmultidatasource.entity.book.Book;
import com.app.springmultidatasource.entity.users.Users;
import com.app.springmultidatasource.repository.book.BookRepository;
import com.app.springmultidatasource.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringMultidatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMultidatasourceApplication.class, args);
	}

	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private BookRepository bookRepository;

	@PostConstruct
	public void addData2DBMySQLAndPostgreSQL(){

		usersRepository.saveAll(Stream.of(
				Users.builder().name("dicka").build(),
				Users.builder().name("muhammad").build(),
				Users.builder().name("nirwansyah").build())
				.collect(Collectors.toList()));

		bookRepository.saveAll(Stream.of(
				Book.builder().name("Java 11").build(),
				Book.builder().name("The power of lambda").build())
				.collect(Collectors.toList()));
	}
}
