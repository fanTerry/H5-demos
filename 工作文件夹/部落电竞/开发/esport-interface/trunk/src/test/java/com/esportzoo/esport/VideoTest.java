//package com.esportzoo.esport;
//
//import com.alibaba.fastjson.JSON;
//
//import com.esportzoo.esport.constants.quiz.QuizRank;
//import com.esportzoo.esport.manager.hd.RankListComponent;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * @description:
// *
// * @author: Haitao.Li
// *
// * @create: 2019-08-22 16:38
// **/
//public class VideoTest {
//
//
//	@Autowired
//	private RankListComponent rankListComponent;
//
//	/**加数据*/
//	@Test
//	public void testAddRank(){
//		QuizRank quizRank = rankListComponent.updateRank(145656L, 100.01F);
//		QuizRank quizRank1 = rankListComponent.updateRank(145729L, 90.02F);
//		System.out.println("quizRank:"+ JSON.toJSONString(quizRank));
//		System.out.println("quizRank1:"+JSON.toJSONString(quizRank1));
//
//	}
//	/** 查前n排名情况*/
//	@Test
//	public void testQueryRank(){
//		List<QuizRank> topNRanks = rankListComponent.getTopNRanks(30);
//		System.out.println("topNRanks:"+JSON.toJSONString(topNRanks));
//
//	}
//
//	/** 查个人排名情况*/
//	@Test
//	public void testQueryUserRank(){
//		QuizRank rank = rankListComponent.getRank(145656L);
//		System.out.println("rank:"+JSON.toJSONString(rank));
//	}
//
//
//	@Test
//	public void  testCreate(){
//		System.out.println("9999");
//	}
//}
