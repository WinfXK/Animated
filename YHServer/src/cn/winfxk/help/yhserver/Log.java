package cn.winfxk.help.yhserver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Createdate 2021/05/27 20:59:11
 * @author Winfxk
 */
public class Log {
	private static final SimpleDateFormat time = new SimpleDateFormat("[HH:mm:ss]");

	public static void info(String Message) {
		System.out.println(time.format(new Date()) + "[Info]" + Message);
	}

	public static void error(String Message) {
		System.err.println(time.format(new Date()) + "[Error]" + Message);
	}

	public static void warning(String Message) {
		System.out.println(time.format(new Date()) + "[Warning]" + Message);
	}
}
