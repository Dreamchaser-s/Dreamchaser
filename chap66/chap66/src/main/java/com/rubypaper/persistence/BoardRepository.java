package com.rubypaper.persistence;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rubypaper.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Integer>{    //64

	/*
	 * @Query("SELECT b FROM Board b JOIN FETCH b.member") List<Board>
	 * findAllWithMember();
	 */

}