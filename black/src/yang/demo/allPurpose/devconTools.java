package yang.demo.allPurpose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class devconTools {
	String path;
	public devconTools(String devconPath) {
		path = devconPath;
	}

	public boolean enableDev(String hwID) {
		try {
			Process exec = Runtime.getRuntime().exec(path+" enable "+hwID);
			StringBuilder rd = readProcessOutput(exec);
			if(rd.indexOf("device(s) enabled") != -1) {
				return true;
			}else return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean disableDev(String hwID) {
		try {
			Process exec = Runtime.getRuntime().exec(path+" disable "+hwID);
			StringBuilder rd = readProcessOutput(exec);
			if(rd.indexOf("device(s) disabled") != -1) {
				return true;
			}else return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public StringBuilder getDevStatus(String hwID) {
		try {
			Process exec = Runtime.getRuntime().exec(path+" status "+hwID);
			return readProcessOutput(exec);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean devIsEnable(String hwID) {
		StringBuilder devStatus = getDevStatus(hwID);
		if(devStatus.indexOf("Device is disabled") != -1) {
			return false;
		}else return true;
	}
	static StringBuilder readProcessOutput(Process process) {
		StringBuilder sb = new StringBuilder();
		sb.append(read(process.getInputStream(), System.out));
		sb.append(read(process.getErrorStream(), System.err));
		return sb;
	}

	// 读取输入流
	private static StringBuilder read(InputStream inputStream, PrintStream out) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = reader.readLine()) != null) {
				// out.println(line);
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb;
	}
}
