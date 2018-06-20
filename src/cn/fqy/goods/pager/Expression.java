package cn.fqy.goods.pager;

/**
 * 表达式类
 * 要生成无参和有参构造器
 * @author acer
 *
 */
public class Expression {
	private String name;//名称
	private String operator;//操作符
	private String value;//值
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Expression(String name, String operator, String value) {
		super();
		this.name = name;
		this.operator = operator;
		this.value = value;
	}
	public Expression() {
		super();
	}
	
}
