package com.cos.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

/**
 * 단위 테스트 (Service와 관련된 애들만 메모리에 띄우면 됨)
 * BookRepository => 가짜 객체로 만들 수 있음.
 *
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

	@InjectMocks // BookService객체가 만들어질 때 해당 파일에 @Mock로 등록된 모든 애들을 주입 받는다.
	private BookService bookservice;
	
	@Mock
	private BookRepository bookRepository;
	
	@Test
	public void save_테스트() {
		//given
		Book book = new Book();
		book.setTitle("책제목1");
		book.setAuthor("책저자1");
		
		//when
		when(bookRepository.save(book)).thenReturn(book);
		
		Book bookEntity = bookservice.저장하기(book);
		
		assertEquals(bookEntity, book);
	}
}
