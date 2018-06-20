package cn.fqy.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.fqy.goods.book.domain.Book;
import cn.fqy.goods.category.domain.Category;
import cn.fqy.goods.pager.Expression;
import cn.fqy.goods.pager.PageBean;
import cn.fqy.goods.pager.PageConstants;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 按图书id查询
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public Book findByBid(String bid) throws SQLException{
		String sql = "select * from t_book b, t_category c where b.cid=c.cid and b.bid=?";
		Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
		//book缺少cid
		Book book = CommonUtils.toBean(map, Book.class);
		//category只有cid写入
		Category category = CommonUtils.toBean(map, Category.class);
		//book被赋予cid
		book.setCategory(category);
		
		//创建父分类对象，将pid赋给它，将父分类设置到book对象
		if(map.get("pid") != null){//为二级分类
			Category parent = new Category();
			parent.setCid((String)map.get("pid"));
			category.setParent(parent);
		}
		return book;
	}
	
	/**
	 * 通用查询
	 * @throws SQLException 
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList, int pc) throws SQLException{
		/*
		 * 1.得到ps分页大小
		 * 2.得到tr总记录数
		 * 3.得到bookList查询的书
		 * 4.创建pageBean返回
		 */
		/*
		 * 1.ps分页大小
		 */
		int ps = PageConstants.BOOK_PAGE_SIZE;
		/*
		 * 2.获得where子句
		 * 例如：where 1=1 and bid = 1 and bname like %java% and edition is null;
		 */
		StringBuilder whereSql = new StringBuilder(" where 1=1");
		List<Object> params = new ArrayList<Object>();
		for(Expression expr:exprList){
			whereSql.append(" and "+expr.getName()+" "+expr.getOperator()+" ");
			if(!expr.getOperator().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
//		System.out.println(whereSql);
//		System.out.println(params);
		/*
		 * 3.tr总记录数
		 */
		String sql = "select count(*) from t_book"+whereSql;
		Number number = qr.query(sql, new ScalarHandler(), params.toArray());
		int tr = number.intValue();
		/*
		 * 4.bookList查询书
		 */
		sql = "select * from t_book"+whereSql+"order by orderBy limit ?,?";
		params.add((pc-1)*ps);//从哪条记录开始查
		params.add(ps);//每页查询数量
		List<Book> bookList = qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
		/*
		 * 5.创建pageBean,并返回
		 */
		PageBean<Book> pb = new PageBean<Book>();
		/*
		 * 没有设置url项，此由servlet完成
		 */
		pb.setBeanList(bookList);
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		
		return pb;
		
	}
	
	/**
	 * 按分类查询
	 * @param cid
	 * @param pc
	 * @return
	 * @throws SQLException 
	 */
	public PageBean<Book> findByCategory(String cid, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid","=",cid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 按作者模糊查询
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author","like","%"+author+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 按出版社模糊查询
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press","like","%"+press+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 多条件组合查询
	 * @param criteria
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCombination(Book criteria, int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
		exprList.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
		exprList.add(new Expression("press","like","%"+criteria.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/*
	 * 测试用main方法
	 *  where 1=1 and bid = ? and bname like ? and edition is null 
	 *	[1, %java%]
	 */
	public static void main(String[] args){
		BookDao bookDao = new BookDao();
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bid","=","1"));
		exprList.add(new Expression("bname","like","%java%"));
		exprList.add(new Expression("edition","is null",null));
		
//		bookDao.findByCriteria(exprList, 10);
	}
	
	/**
	 * 根据分类编号查询此分类下有多少图书
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public int findBookCountByCategory(String cid) throws SQLException{
		String sql = "select count(*) from t_book where cid=?";
		Number cnt = qr.query(sql, new ScalarHandler(), cid);
		return cnt == null ? 0 : cnt.intValue();
	}
	/**
	 * 添加图书
	 * @param book
	 * @throws SQLException
	 */
	public void add(Book book) throws SQLException{
		String sql = "insert into t_book(bid, bname, author, price, " +
				"currPrice, discount, press, publishtime, edition, " +
				"pageNum, wordNum, printtime, booksize, paper, cid, " +
				"image_w, image_b) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(), book.getBname(), book.getAuthor(), 
				book.getPrice(), book.getCurrPrice(), book.getDiscount(), book.getPress(),
				book.getPublishtime(), book.getEdition(), book.getPageNum(), book.getWordNum(),
				book.getPrinttime(), book.getBooksize(), book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(), book.getImage_b()};
		qr.update(sql, params);
	}
	/**
	 * 修改图书信息
	 * @param book
	 * @throws SQLException 
	 */
	public void edit(Book book) throws SQLException{
		String sql = "update t_book set bname=?, author=?, price=?, currPrice=?," +
				"discount=?, press=?, publishtime=?, edition=?, pageNum=?, wordNum=?," +
				"printtime=?, booksize=?, paper=?, cid=? where bid=?";
		Object[] params = {book.getBname(), book.getAuthor(), 
				book.getPrice(), book.getCurrPrice(), book.getDiscount(), book.getPress(),
				book.getPublishtime(), book.getEdition(), book.getPageNum(), book.getWordNum(),
				book.getPrinttime(), book.getBooksize(), book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		qr.update(sql, params);
	}
	/**
	 * 删除图书
	 * @param bid
	 * @throws SQLException
	 */
	public void delete(String bid) throws SQLException{
		String sql = "delete from t_book where bid=?";
		qr.update(sql, bid);
	}
	
	
	
	
}
