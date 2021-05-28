package cn.winfxk.android.help.abhor.tool;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

/**
 * @Createdate 2021/04/18 21:41:02
 * @author Winfxk
 */
public class Http {
	/**
	 * 通用Key
	 */
	public static final String General = "general";
	public static final String Host = "152.136.126.117";
	public static final int Port = 19132;

	/**
	 * 请求一个数据
	 * 
	 * @param map 数据内容
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> get(Map<String, Object> map) throws Exception {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(Host, Port), 5000);
		DataInputStream Input = new DataInputStream(socket.getInputStream());
		DataOutputStream Output = new DataOutputStream(socket.getOutputStream());
		map.put("General", General);
		Output.writeUTF(Config.yaml.dump(map));
		String string = Input.readUTF();
		try {
			Input.close();
			Output.close();
			socket.close();
		} catch (Exception e) {
		}
		return Config.yaml.loadAs(string, Map.class);
	}

	public static void Return(Map<String, Object> map, HttpReturn return1) {
		Map<String, Object> data = null;
		try {
			data = get(map);
		} catch (Exception e) {
			e.printStackTrace();
			return1.Return(data, e);
			return;
		}
		return1.Return(data, null);
	}
}
