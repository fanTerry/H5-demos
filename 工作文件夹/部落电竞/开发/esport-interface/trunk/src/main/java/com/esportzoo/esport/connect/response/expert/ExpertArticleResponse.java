package com.esportzoo.esport.connect.response.expert;

import java.io.Serializable;
import java.util.List;

/**
 * @author tingting.shen
 * @date 2019/05/10
 */
public class ExpertArticleResponse implements Serializable{

	private static final long serialVersionUID = 179792815621057196L;
	
	private List<ExpertArticleVo> articleList;
	
	private Long totalPages;

	public List<ExpertArticleVo> getArticleList() {
		return articleList;
	}

	public Long getTotalPages() {
		return totalPages;
	}

	public void setArticleList(List<ExpertArticleVo> articleList) {
		this.articleList = articleList;
	}

	public void setTotalPages(Long totalPages) {
		this.totalPages = totalPages;
	}

}
