package com.richong.arch.web;

import java.util.List;

@SuppressWarnings("rawtypes")
public class Page {
	/**
	 * 每页记录条数
	 */
	private int pageSize = 20;

	/**
	 * 记录总和
	 */
	private int totalCount;
	
	
	/**
	 * 总页数 
	 */
	private int totalPages ;
	
	/**
	 * 起始记录
	 */
	private int startIndex;
	
	/**
	 * 结果集
	 */
	private List result;
	

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}
	
	
}
