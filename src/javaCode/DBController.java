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
	 * �������ݿ⣬��ȡstatement
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
			System.out.println("�������ݿ�����쳣");
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * ע�����û�
	 * @param name
	 * @param password
	 * @param email
	 * @return �ɹ�/ʧ��
	 * @throws SQLException 
	 */
	public Response addNewUser(String name, String password, String email){
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
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
				res.setMsg("�����û��ɹ�");
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
	 * �����û������û���Ϣ�������룩
	 * @param name
	 * @return ʧ�� data=null/�ɹ� data=HashMap(ȡ����ʱǿתHashMap����)
	 * @example {uid=6, name=135, email=1436@qf, register_time=2019-06-18 02:40:54.0}
	 */
	public Response showUserInfo(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			String sql = "select uid, name, email, register_time from user_info "
					+ "where name = ?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setString(1, name);
				ResultSet rs = stat.executeQuery();
				res.setStatus(true);
				res.setMsg("�û���Ϣ����");
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
	 * �����û����������жϵ�¼�Ƿ�ɹ�
	 * @param name
	 * @param password
	 * @return �ɹ�/ʧ�� data��=null
	 */
	public Response checkLogin(String name, String password) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
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
					res.setMsg("�û��������벻��ȷ");
				}
				else {
					res.setStatus(true);
					res.setMsg("��¼�ɹ�");
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
	 * �����û���������¼��¼
	 * @param name
	 * @return
	 */
	public Response addLoginRecord(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
			}
			else {
				String sql = "insert into login_record(uid) values(?)";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					stat.executeUpdate();
					res.setStatus(true);
					res.setMsg("������¼��¼�ɹ�");
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
	 * ����µ���Ϸ��¼
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
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
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
				if(target != null && score >= target) {
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
							res.setMsg("�����¼�ɹ��������µ�ͼ");
						}
						else {
							res.setMsg("�����¼�ɹ����µ�ͼ�ѽ�����");
						}
						rs.close();
						stat.close();
					}
					catch(Exception e) {
						res.setMsg(e.getMessage());
					}
				}
				else {
					res.setMsg("�����¼�ɹ������μ�¼δ�ܽ����µ�ͼ");
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
	 * �������
	 * @param name
	 * @param text
	 * @return
	 */
	public Response addMessage(String name, String text) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
			}
			else {
				String sql = "insert into message(msg_text, uid) values(?,?)";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setString(1, text);
					stat.setInt(2, user_id);
					stat.executeUpdate();
					res.setMsg("���Գɹ�");
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
	 * �����û���ɸѡһ����������Ϸ��¼
	 * @param name �û���
	 * @param pgno �ڼ�ҳ
	 * @param pgcnt һҳ���ٸ���¼
	 * @return data = List<Map<String,Object>>
	 */
	public Response showPlayerRecord(String name, Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
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
					res.setMsg("��Ϸ��¼����");
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
	 * ����������ҵ�����
	 * @param pgno �ڼ�ҳ
	 * @param pgcnt һҳ���ٸ���Ϣ
	 * @return data = List<Map<String, Object>>
	 */
	public Response showMessage(Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			String sql = "select message.uid, name, msg_text, msg_time from "
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
					item.put("uid", rs.getInt("uid"));
					item.put("name", rs.getString("name"));
					item.put("msg_text", rs.getString("msg_text"));
					item.put("msg_time", rs.getTimestamp("msg_time"));	
					data.add(item);
				}
				res.setStatus(true);
				res.setMsg("���Լ�¼����");
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
	 * ��ѯ���������ѵ÷�
	 * @param name
	 * @return data = List<Map<String, Object>>
	 */
	public Response showPersonalHighScore(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
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
					res.setMsg("���������Ϸ��¼����");
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
	 * �鿴ĳ����ҿ��˶��ٸ���ͼ
	 * @param name
	 * @return data = List<Integer>
	 */
	public Response showLevel(String name) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
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
					res.setMsg("���������Ϸ��¼����");
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
	 * չʾ�÷ְ�
	 * @param level
	 * @return data = List<Map<String, Object>>
	 */
	public Response showScoreBoard(Integer level, Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			String sql = "select name, score, playtime from "
					+ "play_record inner join user_info on user_info.uid = play_record.uid "
					+ "where level = ? order by score desc "
					+ "limit ?,?";
			try {
				PreparedStatement stat = con.prepareStatement(sql);
				stat.setInt(1, level);
				stat.setInt(2, pgno*pgcnt);
				stat.setInt(3, pgcnt);
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
				res.setMsg("�÷ְ�");
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
	 * ����˵ĵ�¼��¼
	 * @param name
	 * @return data = List<Date>
	 */
	public Response showLoginRecord(String name, Integer pgno, Integer pgcnt) {
		Response res = new Response();
		Connection con = getConnection();
		if(con == null) {
			res.setStatus(false);
			res.setMsg("�������ݿ�ʧ��");
		}
		else {
			Integer user_id = getUserIdByName(name);
			if(user_id == null) {
				res.setStatus(false);
				res.setMsg("�û���������");
			}
			else {
				String sql = "select login_time from login_record "
						+ "where uid = ? order by login_time desc "
						+ "limit ?,?";
				try {
					PreparedStatement stat = con.prepareStatement(sql);
					stat.setInt(1, user_id);
					stat.setInt(2, pgno*pgcnt);
					stat.setInt(3, pgcnt);
					ResultSet rs = stat.executeQuery();
					List<Date> data = new ArrayList<Date>();
					while(rs.next()) {
						data.add(rs.getTimestamp("login_time"));
					}
					res.setData(data);
					res.setMsg("��¼��¼����");
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
	 * ����name��ȡuid
	 * @param name
	 * @return uid
	 */
	public Integer getUserIdByName(String name) {
		Integer res = null;
		Connection con = getConnection();
		if(con == null) {
			System.out.println("getUserIdByName - �������ݿ�ʧ��");
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
