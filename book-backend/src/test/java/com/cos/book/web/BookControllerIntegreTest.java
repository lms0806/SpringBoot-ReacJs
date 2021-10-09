package com.cos.book.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 통합 테스트 (모든 Bean들을 똑같이 IoC 올리고 테스트하는 것) WebEnvironment.MOCK = 실제 톰캣을 올리는게 아니라,
 * 다른 톰캣으로 테스트 WebEnvironment.RANDOM_POR = 실제 톰캣으로 테스트
 * 
 * @AutoConfigureMockMvc MockMvc를 IoC에 등록해줌
 * @Transactional 각각의 테스트 함수가 종료될 때마다 트랜잭션을 rollback 해주는 어노테이션
 */

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class BookControllerIntegreTest {

	@Autowired
	private MockMvc mockMvc; // 컨트롤러의 주소로 테스트를 해볼수 있는 것

	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@BeforeEach
	public void init() {
		entityManager.createNativeQuery("Alter Table book Alter Column id Restart with 1").executeUpdate();
		//entityManager.createNativeQuery("Alther Table book Auto_Increment = 1").executeUpdate(); mysql
	}
	
	// BDDMockito 패턴 given, when, then
	@Test
	public void save_테스트() throws Exception {
		// given(테스트를 하기 위한 준비)
		Book book = new Book(null, "스프링 따라하기", "임스");
		String content = new ObjectMapper().writeValueAsString(book);

		// when (테스트 실행)
		ResultActions resultActions = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content).accept(MediaType.APPLICATION_JSON_UTF8));

		// then (검증)
		resultActions
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("스프링 따라하기"))
				.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findAll_테스트() throws Exception{
		//given
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트 따라하기", "임스"));
		books.add(new Book(null, "리액트 따라하기", "임스"));
		books.add(new Book(null, "JUnit 따라하기", "임스"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", Matchers.hasSize(3)))
			.andExpect(jsonPath("$.[2].title").value("JUnit 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void findById_테스트() throws Exception {
		// given
		Long id = 2L;
		
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트 따라하기", "임스"));
		books.add(new Book(null, "리액트 따라하기", "임스"));
		books.add(new Book(null, "JUnit 따라하기", "임스"));
		bookRepository.saveAll(books);
		
		//when
		ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		//then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("리액트 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception {
		// given
		Long id = 3L;
		List<Book> books = new ArrayList<>();
		books.add(new Book(null, "스프링부트 따라하기", "임스"));
		books.add(new Book(null, "리액트 따라하기", "임스"));
		books.add(new Book(null, "JUnit 따라하기", "임스"));
		bookRepository.saveAll(books);
		
		Book book = new Book(null, "c++ 따라하기", "임스");
		String content = new ObjectMapper().writeValueAsString(book);
		
		//when
		ResultActions resultActions = mockMvc.perform(put("/book/{id}", id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
				
		//then
		resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(3L))
			.andExpect(jsonPath("$.title").value("c++ 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
}
