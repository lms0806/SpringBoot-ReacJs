import React, { useEffect, useState } from 'react';
import Bookitem from '../../components/Bookitem';

const Home = () => {
  const [books, setBooks] = useState([]);

  // 함수 실행시 최초 한번 실행
  useEffect(() => {
    fetch('http://localhost:8080/book')
      .then((res) => res.json())
      .then((res) => {
        setBooks(res);
      }); // 비동기 함수
  }, []);

  return (
    <div>
      {books.map((book) => (
        <Bookitem key={book.id} book={book} />
      ))}
    </div>
  );
};

export default Home;
