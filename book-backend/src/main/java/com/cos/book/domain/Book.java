package com.cos.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 서버 실행시에 테이블이 생성됨
public class Book {
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 자동증가
	private Long id;
	
	private String title;
	private String author;
}
