package com.rubypaper.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString(exclude="boardList")
public class Member {
   @Id
   private String id;
   private String password;
   private String name;
   private String role;
   
   @OneToMany(mappedBy="member",//member  주인반대쪽에 
		   fetch=FetchType.LAZY,
		   cascade=CascadeType.ALL) //영속성전이
   private List<Board> boardList = new ArrayList<Board>();
   //한명의 사용자가 여려개의 게시글을 입력
   
   
   /*
   //1:1 한명의 사용자가 한개의 게시글만 입력하는 방식
   private Board board;    
   
	public Board getBoard() {
	return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	
	*/
	public String getId() {
		return id;
	}	
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "Member [id=" + id + ", password=" + password + ", name=" + name + ", role=" + role + ", boardList="
				+ boardList + "]";
	}	 
}
