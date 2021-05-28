package cn.winfxk.help.yhserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.winfxk.help.yhserver.tool.Config;
import cn.winfxk.help.yhserver.tool.MyMap;
import cn.winfxk.help.yhserver.tool.Tool;

/**
 * @Createdate 2021/05/27 20:58:07
 * @author Winfxk
 */
public class DisServier extends Thread implements FilenameFilter {
	private Socket socket;
	private DataOutputStream Output;
	private DataInputStream Input;

	public DisServier(Socket socket) {
		this.socket = socket;
		Log.info("链接入站.");
	}

	@Override
	public void run() {
		try {
			Output = new DataOutputStream(socket.getOutputStream());
			Input = new DataInputStream(socket.getInputStream());
			Map<String, Object> map;
			try {
				map = Config.yaml.loadAs(Input.readUTF(), Map.class);
				if (map == null || map.size() <= 0)
					throw new Exception("客户端请求数据为空！");
				try {
					Log.info("开始处理请求.");
					Switch(map);
					Log.info("请求处理结束.");
				} catch (Exception e) {
					e.printStackTrace();
					Log.warning("处理客户端请求中出现错误！Msg: " + e.getMessage());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.error("无法读取客户端数据内容！");
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.error("无法建立输入输出连接！");
			return;
		} finally {
			try {
				Output.close();
				Input.close();
				socket.close();
				Log.info("链接关闭.");
			} catch (Exception e2) {
			}
		}
	}

	private boolean Switch(Map<String, Object> map) throws Exception {
		String Key = String.valueOf(map.get("Key"));
		if (Key == null || Key.isEmpty())
			throw new Exception("无法获取用户请求类型！");
		String ID = Tool.objToString(map.get("Name"));
		String Pass = Tool.objToString(map.get("Pass")), Sign;
		File file;
		Config config;
		if (Key.toLowerCase().equals("sign")) {
			Log.info("收到注册请求.");
			if (ID == null || ID.isEmpty() || ID.length() >= 20)
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "ID不合法(过长或未输入)"));
			if (Pass == null || Pass.isEmpty() || Pass.length() >= 30)
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "密码不合法(过长或未输入)"));
			Sign = Tool.objToString(map.get("Sign"));
			if (Sign == null || Sign.isEmpty() || Sign.length() >= 10)
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "昵称不合法(过长或未输入)"));
			file = new File(Main.PlayerDir, ID);
			if (file.exists())
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "用户已存在！请更换申请ID！"));
			config = new Config(file);
			config.set("Name", ID);
			config.set("Sign", Sign);
			config.set("Pass", Pass);
			config.set("Admin", false);
			config.set("Item", new HashMap<>());
			config.save();
			return sendMessage(MyMap.make("ret", (Object) true).add("Text", "注册成功！"));
		}
		if (!isUser(ID, Pass))
			return sendMessage(MyMap.make("ret", (Object) false).add("Text", "用户不存在或密码错误！"));
		Map<String, Object> data;
		Object object;
		switch (Key.toLowerCase()) {
		case "add":
			Log.info("添加用户体温.");
			String string = Tool.objToString(map.get("C"));
			if (string == null || string.isEmpty() || string.length() >= 20 || !Tool.isInteger(string))
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "输入的体温不合法！"));
			file = new File(Main.PlayerDir, ID);
			config = new Config(file);
			object = config.get("Item");
			data = object != null && object instanceof Map ? (HashMap<String, Object>) object : new HashMap<>();
			if (data.containsKey(Tool.getDate()))
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "当日已提交！不能重复提交！"));
			data.put(Tool.getDate(), MyMap.make("Heat", (Object) Tool.objToDouble(string)).add("Date",
					Tool.getDate() + " " + Tool.getTime()));
			config.set("Item", data);
			config.save();
			return sendMessage(MyMap.make("ret", (Object) true).add("Text", "提交成功！当前您已有" + data.size() + "个体温数据."));
		case "get":
			Log.info("收到数据查询请求.");
			String toID = Tool.objToString(map.get("ID"));
			if (toID == null || toID.isEmpty())
				toID = ID;
			config = new Config(new File(Main.PlayerDir, ID));
			if (!ID.equals(toID) && !config.getBoolean("Admin"))
				return sendMessage(MyMap.make("ret", (Object) false).add("Text", "你没有权限查看该用户数据！"));
			config = new Config(new File(Main.PlayerDir, toID));
			object = config.get("Item");
			data = object != null && object instanceof Map ? (HashMap<String, Object>) object : new HashMap<>();
			return sendMessage(MyMap.make("ret", (Object) true).add("Text", config.getString("Sign") + "的体温数据")
					.add("Sign", config.getString("Sign")).add("Item", data).add("Admin", config.getBoolean("Admin"))
					.add("List", config.getBoolean("Admin") ? Main.PlayerDir.list(this) : new ArrayList<>()));
		case "login":
			Log.info("收到登录请求.");
			config = new Config(new File(Main.PlayerDir, ID));
			return sendMessage(MyMap.make("ret", (Object) true).add("Text", "欢迎尊敬的" + config.getString("Sign")));
		}
		return false;
	}

	private <T, V> boolean sendMessage(Map<T, V> map) {
		try {
			Output.writeUTF(Config.yaml.dump(map));
			return Tool.ObjToBool(map.get("ret"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected static boolean isUser(String ID, String Pass) {
		if (ID == null || ID.isEmpty() || Pass == null || Pass.isEmpty())
			return false;
		File file = new File(Main.PlayerDir, ID);
		if (!file.exists())
			return false;
		Config config = new Config(file);
		String P = config.getString("Pass");
		if (P == null || P.isEmpty())
			return false;
		return P.equals(Pass);
	}

	protected static boolean isUser(File file, String ID, String Pass) {
		if (ID == null || ID.isEmpty() || Pass == null || Pass.isEmpty())
			return false;
		if (!file.exists())
			return false;
		Config config = new Config(file);
		if (!config.getFile().exists())
			return false;
		String P = config.getString("Pass");
		if (P == null || P.isEmpty())
			return false;
		return P.equals(Pass);
	}

	protected static boolean isUser(Config config, String ID, String Pass) {
		if (ID == null || ID.isEmpty() || Pass == null || Pass.isEmpty())
			return false;
		if (!config.getFile().exists())
			return false;
		String P = config.getString("Pass");
		if (P == null || P.isEmpty())
			return false;
		return P.equals(Pass);
	}

	@Override
	public boolean accept(File dir, String name) {
		return new File(dir, name).isFile();
	}
}
