package com.lei2j.douyu.web.response;

import java.util.List;
/**
 * @author lei2j
 * @param <T>
 */
public class Pagination<T,P> {

	private static Integer MAX_LIMIT = 100;
	private Integer pageNum=1;
	private int total = 0;
	private Integer offset=0;
	private Integer limit=30;

	private Integer totalPage=0;

	/**
	 * result
	 */
	private List<T> items;
	/**
	 * 查询参数
	 */
	private P params;

	public Pagination() {
	}



	public Pagination(Integer limit,Integer pageNum) {
		this.setLimit(limit);
		this.setPageNum(pageNum);
	}

	public Pagination(Integer limit,Integer pageNum, P params) {
		this(limit,pageNum);
		this.params = params;
	}

	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		if(pageNum==null||pageNum<1){
			return;
		}
		this.pageNum = pageNum;
		this.offset = (pageNum-1)*limit;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		if(total<=0){
			return;
		}
		this.totalPage = ((total+limit-1)/limit);
		this.total = total;
	}
	public Integer getOffset() {
		return offset;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		if(limit==null||limit>MAX_LIMIT){return;}
		this.limit = limit;
	}

	public P getParams() {
		return params;
	}

	public void setParams(P params) {
		this.params = params;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pagination [pageNum=");
		builder.append(pageNum);
		builder.append(", total=");
		builder.append(total);
		builder.append(", offset=");
		builder.append(offset);
		builder.append(", limit=");
		builder.append(limit);
		builder.append(", totalPage=");
		builder.append(totalPage);
		builder.append(", items=");
		builder.append(items);
		builder.append(", params=");
		builder.append(params);
		builder.append("]");
		return builder.toString();
	}

}
