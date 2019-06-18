package javaCode;

public class Response {
	
	private Boolean status;
	private String msg;
	private Object data;
	
	public Boolean getStatus() {
		return this.status;
	}
	
	public String getMsg() {
		return this.msg;
	}

	public Object getData() {
		return this.data;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}
