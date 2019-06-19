package javaCode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javaCode.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class DBController {
	
	private static final Map<Integer, Integer> target_score;
	private static final Map<Integer, Integer> next_level;
	static
	{
		target_score = new HashMap<Integer,Integer>();
		target_score.put(3, 800);
		target_score.put(4, 1000);
		target_score.put(5, 20000);
		target_score.put(6, 20000);
		target_score.put(7, 40000);
		target_score.put(8, 20000);
		next_level = new HashMap<Integer,Integer>();
		next_level.put(3, 5);
		next_level.put(4, 6);
		next_level.put(5, 7);
		next_level.put(6, 8);
		next_level.put(7, 9);
		next_level.put(8, 2);
	};	
	
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
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(conStr,user,pwd);
			return con;
		}
		catch (Exception e){
			System.out.println("连接数据库出现异常");
			System.out.println(e.getMessage());
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
				res.setStatus(false);
				res.setMsg(e.getMessage());
			}
		}
		return res;
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
				res.setStatus(false);
				res.setMsg(e.getMessage());
			}
		}
		return res;
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
				res.setMsg(e.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * 根据用户名新增登录记录
	 * @param name
	 * @return
	 */
	public Response addLoginRecord(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "insert into login_record(uid) values(?)";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					stat.executeUpdate();
					res.setStatus(true);
					res.setMsg("新增登录记录成功");
					stat.close();
					con.close();
				}
				catch(Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}
				return res;
			}
		}
		return res;
	}
	
	/**
	 * 添加新的游戏记录
	 * @param name
	 * @param level
	 * @param score
	 * @return
	 */
	public Response addPlayRecord(String name, Integer level, Integer score) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "insert into play_record(uid,level,score) values(?,?,?)";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					stat.setInt(2, level);
					stat.setInt(3, score);
					stat.executeUpdate();
					res.setStatus(true);
					stat.close();
				}
				catch(Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}
				Integer target = target_score.get(level);
				if(score >= target) {
					Integer n_level = next_level.get(level);
					sql = "select * from highest_score where uid = ? and level = ?";
					try {
						PreparedStatement stat = con.prepareStatement(sql);
						stat = con.prepareStatement(sql);
						stat.setInt(1, user_id);
						stat.setInt(2, n_level);
						ResultSet rs = stat.executeQuery();
						rs.last();
						if(rs.getRow() == 0) {
							sql = "insert into highest_score(uid, level, rcid) values(?,?,null)";
							stat = con.prepareStatement(sql);
							stat.setInt(1, user_id);
							stat.setInt(2, n_level);
							stat.executeUpdate();
							res.setMsg("保存纪录成功，解锁新地图");
						}
						else {
							res.setMsg("保存纪录成功，新地图已解锁过");
						}
						rs.close();
						stat.close();
					}
					catch(Exception e) {
						res.setMsg(e.getMessage());
					}
				}
				else {
					res.setMsg("保存纪录成功，本次记录未能解锁新地图");
				}
				try {
					con.close();
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return res;
	}
	
	/**
	 * 添加留言
	 * @param name
	 * @param text
	 * @return
	 */
	public Response addMessage(String name, String text) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "insert into message(msg_text, uid) values(?,?)";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setString(1, text);
					stat.setInt(2, user_id);
					stat.executeUpdate();
					res.setMsg("留言成功");
				}
				catch(Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}
			}
		}
		return res;
	}
	
	/**
	 * 根据用户名筛选一定数量的游戏记录
	 * @param name 用户名
	 * @param pgno 第几页
	 * @param pgcnt 一页多少个记录
	 * @return data = List<Map<String,Object>>
	 */
	public Response showPlayerRecord(String name, Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "select level, score, playtime from "
						+ "play_record where uid = ? "
						+ "order by playtime DESC "
						+ "limit ?,?";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					stat.setInt(2, pgno*pgcnt);
					stat.setInt(3, pgcnt);
					ResultSet rs = stat.executeQuery();
					List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
					while(rs.next()) {
						Map<String,Object> item = new HashMap<String,Object>();
						item.put("level", rs.getString("level"));
						item.put("score", rs.getString("score"));
						item.put("playtime", rs.getTimestamp("playtime"));	
						data.add(item);
					}
					res.setData(data);
					res.setMsg("游戏记录如下");
					res.setStatus(true);
					rs.close();
					stat.close();
					con.close();
				}
				catch(Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}
			}
		}
		return res;
	}
	
	/**
	 * 查找所有玩家的留言
	 * @param pgno 第几页
	 * @param pgcnt 一页多少个信息
	 * @return data = List<Map<String, Object>>
	 */
	public Response showMessage(Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			String sql = "select name, msg_text, msg_time from "
					+ "message inner join user_info on message.uid = user_info.uid "
					+ "order by msg_time desc "
					+ "limit ?,?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setInt(1, pgno*pgcnt);
				stat.setInt(2, pgcnt);
				ResultSet rs = stat.executeQuery();
				List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
				while(rs.next()) {
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("name", rs.getString("name"));
					item.put("msg_text", rs.getString("msg_text"));
					item.put("msg_time", rs.getTimestamp("msg_time"));	
					data.add(item);
				}
				res.setStatus(true);
				res.setMsg("留言记录如下");
				res.setData(data);
				rs.close();
				stat.close();
				con.close();
			}
			catch(Exception e) {
				res.setStatus(false);
				res.setMsg(e.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * 查询单个玩家最佳得分
	 * @param name
	 * @return data = List<Map<String, Object>>
	 */
	public Response showPersonalHighScore(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "select highest_score.level, score, playtime from "
						+ "play_record inner join highest_score on play_record.rcid = highest_score.rcid "
						+ "where highest_score.uid = ? order by highest_score.level asc";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					ResultSet rs = stat.executeQuery();
					List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
					while(rs.next()) {
						Map<String,Object> item = new HashMap<String,Object>();
						item.put("level", rs.getString("level"));
						item.put("score", rs.getString("score"));
						item.put("playtime", rs.getTimestamp("playtime"));	
						data.add(item);
					}
					res.setData(data);
					res.setMsg("个人最佳游戏记录如下");
					res.setStatus(true);
					rs.close();
					stat.close();
					con.close();
				}
				catch (Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}	
			}
		}
		return res;
	}
	
	/**
	 * 查看某个玩家开了多少个地图
	 * @param name
	 * @return data = List<Integer>
	 */
	public Response showLevel(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "select level from highest_score "
						+ "where uid = ? order by level asc";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					ResultSet rs = stat.executeQuery();
					List<Integer> data = new ArrayList<Integer>();
					while(rs.next()) {
						data.add(rs.getInt("level"));
					}
					res.setData(data);
					res.setMsg("个人最佳游戏记录如下");
					res.setStatus(true);
					rs.close();
					stat.close();
					con.close();
				}
				catch (Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}	
			}
		}
		return res;
	}
	
	/**
	 * 展示得分榜
	 * @param level
	 * @return data = List<Map<String, Object>>
	 */
	public Response showScoreBoard(Integer level) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			String sql = "select name, score, playtime from "
					+ "play_record inner join user_info on user_info.uid = play_record.uid "
					+ "where level = ? order by score desc";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setInt(1, level);
				ResultSet rs = stat.executeQuery();
				List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
				while(rs.next()) {
					Map<String,Object> item = new HashMap<String,Object>();
					item.put("name", rs.getString("name"));
					item.put("score", rs.getString("score"));
					item.put("playtime", rs.getTimestamp("playtime"));	
					data.add(item);
				}
				res.setData(data);
				res.setMsg("得分榜");
				res.setStatus(true);
				rs.close();
				stat.close();
				con.close();
			}
			catch(Exception e) {
				res.setStatus(false);
				res.setMsg(e.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * 查个人的登录记录
	 * @param name
	 * @return data = List<Date>
	 */
	public Response showLoginRecord(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("连接数据库失败");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("用户名不存在");
			}
			else {
				String sql = "select login_time from login_record "
						+ "where uid = ? order by login_time desc";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					ResultSet rs = stat.executeQuery();
					List<Date> data = new ArrayList<Date>();
					while(rs.next()) {
						data.add(rs.getTimestamp("login_time"));
					}
					res.setData(data);
					res.setMsg("登录记录如下");
					res.setStatus(true);
					rs.close();
					stat.close();
					con.close();
				}
				catch (Exception e) {
					res.setStatus(false);
					res.setMsg(e.getMessage());
				}	
			}
		}
		return res;
	}
	
	/**
	 * 根据name获取uid
	 * @param name
	 * @return uid
	 */
	public Integer getUserIdByName(String name) {
		Integer res = null;
		Connection con = getConnection();
		if(con == null) {
			System.out.println("getUserIdByName - 连接数据库失败");
			return null;
		}
		else {
			String sql = "select uid from user_info where name = ?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setString(1, name);
				ResultSet rs = stat.executeQuery();
				rs.last();
				if(rs.getRow() > 0) {
					res = rs.getInt("uid");
				}
				rs.close();
				stat.close();
				con.close();
			}
			catch(Exception e) {
				System.out.println("getUserIdByName - " + e.getMessage());
			}
			return res;
		}
	}
}
