package cn.fqy.goods.pager;

import java.util.List;

/**
 * 分页bean,会在各层之间传递 
 */
public class PageBean<T> {
	private int pc;//pageCode当前页
	private int tr;//totalResult总记录数
	private int ps;//pageSize分页数
	private String url;//分页数的数字的请求路径 /goods/xxxServlet?method=findByxxx&cid=xx
	private List<T> beanList;//展示对象
	
	//获得总页数
	public int getTp(){
		int tp = tr / ps;
		return tr % ps == 0 ? tp : tp + 1;
	}
	
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<T> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
	
	
}
