package cn.winfxk.help.yhserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import cn.winfxk.help.yhserver.tool.Config;

/**
 * @Createdate 2021/05/27 20:57:42
 * @author Winfxk
 */
public class Main extends Thread {
	public static final int Port = 19132;
	/**
	 * 存储玩家数据的文件夹名称
	 */
	public static final String PlayerDirName = "Player";
	/**
	 * 配置文件存放路径
	 */
	public static final File DataDir = new File("Config");
	/**
	 * 存放玩家数据的文件夹文件对象
	 */
	public static final File PlayerDir = new File(DataDir, PlayerDirName);
	/**
	 * 系统配置文件的文件对象
	 */
	public static final File ConfigFile = new File(DataDir, "Config.yml");
	/**
	 * 系统配置文件
	 */
	public static final Config config = new Config(ConfigFile);
	static {
		if (!PlayerDir.exists())
			PlayerDir.mkdirs();
		File file = new File(PlayerDir, "10086");
		if (!file.exists()) {
			Log.info("创建默认数据...");
			Config config = new Config(file);
			config.set("Admin", true);
			config.set("Pass", "1008611");
			config.set("Sign", "系统管理员");
			config.save();
		}
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(Port);
			while (true)
				try {
					Log.info("准备新请求...");
					new DisServier(serverSocket.accept()).start();
				} catch (Exception e) {
					Log.warning("已跳过一个错误请求！");
				}
		} catch (IOException e) {
			e.printStackTrace();
			Log.error("无法启动服务器！");
		}
	}

	public static void main(String[] args) {
		Log.info("服务器启动！");
		new Main().start();
	}

}
