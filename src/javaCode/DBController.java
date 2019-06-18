package javaCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javaCode.Response;

public class DBController {
	
	/**
	 * 连接数据库，获取statement
	 * @return Connection
	 */
	private Connection getConnection() {
		String conStr = "jdbc:mysql://172.18.187.10:3306/211ol_16337139"
				+"?autoReconnect=true&useUnicode=true"
				+"&characterEncoding=UTF-8";
		String user = "user";
		String pwd = "123";
		try {
			Connection con = DriverManager.getConnection(conStr,user,pwd);
			return con;
		}
		catch (Exception e){
			System.out.println("连接数据库出现异常");
			System.out.println(e.getStackTrace());
			return null;
		}
	}
	
	/**
	 * 注册新用户
	 * @param name
	 * @param password
	 * @param email
	 * @return 成功/失败
	 * @throws SQLException 
	 */
	public Response addNewUser(String name, String password, String email){
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
			return res;
		}
		else {
			String sql = "insert into user_info(name, password, email) values(?,?,?)";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setString(1, name);
				stat.setString(2, MD5Encode.getMd5Str(password));
				stat.setString(3, email);
				stat.executeUpdate();
				res.setStatus(true);
				res.setMsg("新增用户成功");
				stat.close();
				con.close();
			}
			catch (Exception e) {
				// 如果结果是false 要么格式问题 要么用户名/邮箱重名
				res.setStatus(false);
				res.setMsg("用户名已存在");
			}
			return res;
		}
	}
	
	/**
	 * 根据用户名查用户信息（除密码）
	 * @param name
	 * @return 失败 data=null/成功 data=HashMap(取数据时强转HashMap即可)
	 * @example {uid=6, name=135, email=1436@qf, register_time=2019-06-18 02:40:54.0}
	 */
	public Response showUserInfo(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
			return res;
		}
		else {
			String sql = "select uid, name, email, register_time from user_info "
					+ "where name = ?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setString(1, name);
				ResultSet rs = stat.executeQuery();
				res.setStatus(true);
				res.setMsg("用户信息如下");
				res.setData(Helper.ResultSet_to_Map(rs));
				rs.close();
				stat.close();
				con.close();
			}
			catch (Exception e) {
				// 如果结果是false 要么格式问题 要么用户名不存在
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			return res;
		}
	}
	
	/**
	 * 根据用户名和密码判断登录是否成功
	 * @param name
	 * @param password
	 * @return 成功/失败 data均=null
	 */
	public Response checkLogin(String name, String password) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
			return res;
		}
		else {
			String sql = "select uid from user_info where name = ? and password = ?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setString(1, name);
				stat.setString(2, MD5Encode.getMd5Str(password));
				ResultSet rs = stat.executeQuery();
				rs.last();
				if(rs.getRow() == 0) {
					res.setStatus(false);
					res.setMsg("用户名或密码不正确");
				}
				else {
					res.setStatus(true);
					res.setMsg("登录成功");
				}	
				rs.close();
				stat.close();
				con.close();
			}
			catch (Exception e) {
				res.setStatus(false);
				res.setMsg("用户名或密码不正确");
			}
			return res;
		}
	}
	
}
