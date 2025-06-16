package com.rubypaper.domain;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.ToString;
@Data
@Entity
//@ToString(exclude="Member")
@SequenceGenerator(name="boardSeq",sequenceName="SEQUENCE_Seq",
                   initialValue =1, allocationSize =1) 
public class Board {   
	@Id   
	@GeneratedValue(strategy=GenerationType.SEQUENCE,
	                generator ="boardSeq")  
	private int seq;	
	private String title;		
	//private String writer;
	@ManyToOne(fetch=FetchType.LAZY)   //오류시 Transactional   //outerJoin
	//@JoinColumn(name="ID")  //fk
	@JoinColumn(name="ID",nullable=false)  //innerJoin 반드시 값을 갖음 성능 좋음
	private Member member;   //fk  연관관계주인
	            //member
	private String content;		
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	private Date createDate=new Date();	
	private int cnt=0;		
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * public String getWriter() { return writer; }
	 */
	/*
	 * public void setWriter(String writer) { this.writer = writer; }
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	@Override
	public String toString() {
		return "Board [seq=" + seq + ", title=" + title + ", member=" + member + ", content=" + content
				+ ", createDate=" + createDate + ", cnt=" + cnt + "]";
	}
	
}
