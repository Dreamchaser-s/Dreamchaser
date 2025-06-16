package com.rubypaper.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rubypaper.domain.Board;
import com.rubypaper.persistence.BoardRepository;

@Service
public class BoardServiceImpl implements BoardService{
	@Autowired
	private BoardRepository boardRepository;
	@Override                 
	public List<Board> getBoardList(Board board){
		List<Board>  listboard=boardRepository.findAll();
		return listboard;
	}
    @Override
	public void insertBoard(Board board){//3. 글 등록 처리
		boardRepository.save(board);    	}
    
    
    @Override                                  
	public Board getBoard(Board board){  	//글상세 조회			
		return boardRepository.findById(board.getSeq()).get();                //66			
    }
     
    
    
	@Override
	public void updateBoard(Board board){  //5. 글수정
		Board findBoard 
		     =boardRepository.findById(board.getSeq()).get();  //영속상태에 있어야 함
		
		findBoard.setTitle(board.getTitle());
		findBoard.setContent(board.getContent());
		
		boardRepository.save(findBoard);                        //merge와 같음
	}	
	
	@Override
	public void deleteBoard(Board board){    //글삭제
		boardRepository.deleteById(board.getSeq());
		
	}
  }






