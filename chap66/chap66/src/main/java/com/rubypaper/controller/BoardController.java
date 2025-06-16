package com.rubypaper.controller;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.rubypaper.domain.Board;
import com.rubypaper.domain.Member;
import com.rubypaper.service.BoardService;
import com.rubypaper.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {	
   /*
	@GetMapping("/getBoardList")
	 public String getBoardList( Model model) {	
	 List<Board> boardList =new ArrayList<Board>();	  
	  for(int i =1; i<=5; i++) { 
		  Board board = new Board(); 
		  board.setSeq(i);	 
	      board.setTitle("title"+i); 
	      board.setWriter("writer"+i);
	      board.setContent(i+"번째 게시글"); 
	      board.setCreateDate(new Date());
	      board.setCnt(0); 
	      boardList.add(board); 
	   }
	   model.addAttribute("boardList", boardList);   //"boardList" 로 보냄
	   return "getBoardList";	
	 }		*/
	@Autowired
	private BoardService boardService;	
	
	@RequestMapping("/getBoardList")	//1. 글 목록 
	public String getBoardList1(Board board, Model model) {				
		model.addAttribute("boardList", boardService.getBoardList(board));     //List<Board>  boardList = boardService.getBoardList(board);
	    return "getBoardList";
	}	
	
	
	//--------------------------
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/getBoardList")
	public String getboardlist() {
		return "getBoardList";
	}	
	@RequestMapping("/loginview")
	public String loginview(){
		return "login";
	}
	/*
	//로그인인증처리 
	@PostMapping("/login")
	public String login(Member member) {
		
		Member findMember =memberService.getMember(member);
		if(findMember != null && 
				findMember.getPassword().equals(member.getPassword())) {
			return "forward:getBoardList";
		}else {
			return "forward:loginview";
		}		
	}
	*/
	
	//로그인인증처리 
		@PostMapping("/login")
		public String login(Member member,HttpSession session) {
			Member findMember =memberService.getMember(member);
			if(findMember != null && 
			   findMember.getPassword().equals(member.getPassword())) {
				
				//로그인 성공한 회원만 
				session.setAttribute("user", findMember);                //getBoardList.html
				
				return "forward:getBoardList";
			}else {
				return "forward:loginview";
			}		
		}
	
	//Logout 처리
		@GetMapping("/logout")
		public String logout(SessionStatus status) {
			status.setComplete();
					
			return "index";
		}
		
	
	//2. 글 등록화면으로 이동
	@GetMapping("/insertBoard")
	public String insertBoardView() {		
		return "insertBoard";
	}	
	/*3. 글 등록 처리
	@PostMapping("/insertBoard")
	public String insertBoard(Board board) {                                           
		boardService.insertBoard(board);
		return "forward:getBoardList";   //1.글목록으로 
	}	*/
	
	//3. 글 등록 +uploadfile  처리
		@PostMapping("/insertBoard")
		public String insertBoard(Board board, HttpSession session,
				@RequestBody MultipartFile uploadfile) throws Exception{ 
			
			//작성자 
			   Member loginUser = (Member)session.getAttribute("user");
			    if (loginUser != null) {
			        board.setMember(loginUser); // 작성자 설정
			    }			
			
			
			//fileupload 처리
			String filename =uploadfile.getOriginalFilename();
			System.out.println("filename------------>"+filename);			
			uploadfile.transferTo(new File("c:/spring1/"+filename));			
			
			boardService.insertBoard(board);
			return "forward:getBoardList";   
		}	
	
	//4. 글상세 구현_조회 
	@GetMapping("/getBoard")
	public String getBoard(Board board, Model model) {		
		model.addAttribute("board", boardService.getBoard(board));
		return "getBoard";
	}
	
	//5. 글 수정처리
	@PostMapping("/updateBoard")
	public String updateBoard(Board board) {
		
		boardService.updateBoard(board);  //service로 처리맡김
		
		return "forward:getBoardList";
	}
	
	//6. 글삭제 처리
	@GetMapping("/deleteBoard")
	public String deleteBoard(Board board) {
		
		boardService.deleteBoard(board);
		
		return "forward:getBoardList";
	}	
}


		
