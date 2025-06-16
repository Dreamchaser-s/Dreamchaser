package com.rubypaper.service;
import java.util.List;

import com.rubypaper.domain.Board;

public interface BoardService {	
	//CRUD
	void insertBoard(Board board);  //글삽입 
	void updateBoard(Board board);  //글수정
	void deleteBoard(Board board);  //글삭제
	Board getBoard(Board board);    //상세조회
	List<Board> getBoardList(Board board);//글목록조회
}