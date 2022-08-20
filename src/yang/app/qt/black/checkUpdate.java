package yang.app.qt.black;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public abstract class checkUpdate {
	static Process exec;
	/**
	 * 更新大小
	 */
	static String updateSize;
	/**
	 * 更新下载比例
	 */
	static String progress = "0%";
	/*
	 * 更新信息
	 */
	String updateInfo;
	/**
	 * 完整更新下载地址
	 */
	String updateALLDownloadLink;
	/**
	 * 增量更新下载地址
	 */
	String updateAddsDownLoadLink;
	/**
	 * 更新版本
	 */
	String updateVersion;
	private black b;
	timerInfo showUpdateInfo;
	public checkUpdate(black b) {
		this.b = b;
		b.ba.tempData.put("checkUpdate", this);
		b.removeAllTimer(988);
		check();
	}
	public checkUpdate() {
	}
    /*请求url获取返回的内容*/
    public static String httpsGet(String url) throws IOException{
        URL serverUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-type", "application/json");
        //必须设置false，否则会自动redirect到重定向后的地址
        connection.setInstanceFollowRedirects(false);
        connection.connect();
        StringBuffer buffer = new StringBuffer();
        //将返回的输入流转换成字符串
        try(InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);){
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            connection.disconnect();
            String result = buffer.toString();
            return result;
        }
    }
    /**
	 * description: 使用url 下载远程文件
	 * @param urlPath  --- url资源
	 * @param targetDirectory --- 目标文件夹
	 * @throws Exception
	 * @return void
	 * @version v1.0
	 * @author w
	 * @date 2019年9月3日 下午8:29:01
	 */
	public static void download(String urlPath , String targetDirectory) throws Exception {
		// 解决url中可能有中文情况
		System.out.println("url:"+ urlPath);
		URL url = new URL(urlPath);
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setConnectTimeout(3000);		
		// 设置 User-Agent 避免被拦截 
		http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
		String contentType = http.getContentType();
		System.out.println("contentType: "+ contentType);
		// 获取文件大小 
		long length = http.getContentLengthLong();
		System.out.println("文件大小："+(length / 1024)+"KB");
		// 获取文件名
		String fileName = getFileName(http , urlPath);
		InputStream inputStream = http.getInputStream();
		byte[] buff = new byte[1024*10];
		OutputStream out = new FileOutputStream(new File(targetDirectory,fileName));
		int len ;
		int count = 0; // 计数
		while((len = inputStream.read(buff)) != -1) {
			out.write(buff, 0, len);
			out.flush();
			++count ;
		}
		System.out.println("count:"+ count);
		// 关闭资源
		out.close();
		inputStream.close();
		http.disconnect();
	}
    /**
	 * description: 获取文件名 
	 * @param http
	 * @param urlPath
	 * @throws UnsupportedEncodingException
	 * @return String
	 * @version v1.0
	 * @author w
	 * @date 2019年9月3日 下午8:25:55
	 */
	private static String getFileName(HttpURLConnection http , String urlPath) throws UnsupportedEncodingException {
		String headerField = http.getHeaderField("Content-Disposition");
		String fileName = null ;
		if(null != headerField) {
			String decode = URLDecoder.decode(headerField, "UTF-8");
			fileName = decode.split(";")[1].split("=")[1].replaceAll("\"", "");
			System.out.println("文件名是： "+ fileName);
		}
		if(null == fileName) {
			// 尝试从url中获取文件名
			String[] arr  = urlPath.split("/");
			fileName = arr[arr.length - 1];
			System.out.println("url中获取文件名："+ fileName);
		}
		return fileName;
	}
	void actionDownloadDone() {
		File file = new File("./update/"+updateVersion);
		File newfile = new File("./update/"+updateVersion+".zip");
		if(file.exists()) {
			while(!file.renameTo(newfile)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//先解压缩一次，将setUpdate之类的不需要退出程序就能部署的文件名部署好
		try {
			FileZip.ZipUncompress(newfile.getAbsolutePath().toString(), "./");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showUpdateInfo.timerName = "重启后开始部署更新";
		showUpdateInfo.showProgress = false;
		exec = null;
		b.ba.tempData.remove("checkUpdate");
		whenDownloadDone();
	}
	public StringBuffer read(InputStream inputStream, PrintStream out, String encode) {
		StringBuffer sb = new StringBuffer();
		try {
			String enco = "gbk";
			if (encode != null)
				enco = encode;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, enco));
			
			String line;
			while ((line = reader.readLine()) != null) {
				out.println(line+"\n");
				//断点续传发现文件数据已经下载完成，只是文件名后缀没有.zip字样
				if(line.indexOf("The file is already fully retrieved") != -1) {
					actionDownloadDone();
				}
				TextRegion subString0 = cheakDocument.subString(line, "Length: ", " [applic");
				if(subString0 != null) {
					updateSize = cheakDocument.subString(subString0.text, "(", ")").text;
					System.out.println("更新包大小："+updateSize);
					String timerMessage = "下载更新"+progress+"(共"+updateSize+")";
					showUpdateInfo.timerName = timerMessage;
					whenKnowUpdateSize();
				}
				String subString1 = cheakDocument.subStringByClose(line, "% ", " ");
				if(subString1 != null) {
					progress = subString1+"%";
					System.out.println("已下载完成："+progress);
					if(b != null) {
						String timerMessage = "下载更新"+progress+"(共"+updateSize+")";
						
						showUpdateInfo.timerName = timerMessage;
						
					}
					if(progress.equals("100%")) {
						actionDownloadDone();
					}
				}
				
				sb.append(line+"\n");
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
	public abstract void whenKnowUpdateSize();
	public abstract void whenDownloadDone();
	public static void main(String args[]) {
		new checkUpdate() {

			@Override
			public void whenKnowUpdateSize() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void whenDownloadDone() {
				// TODO Auto-generated method stub
				
			}
			
		}.check();;
	}
	void check() {
		String url = "https://api.github.com/repos/piiiiq/BlackDraft/releases/latest";
//	String url = "https://www.baidu.com";
	boolean stil = true;
	do {
		String json = null;
		try {
			json = httpsGet(url);
			stil = false;
		}catch(java.net.UnknownHostException e) {
			b.p("检查更新失败，因为无法访问目标主机（github）");
			return;
		}catch(java.net.SocketException e) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}catch(IOException ee) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(json != null) {
			if(b != null)
			b.debugPrint(json);
			TextRegion ver = cheakDocument.subString(json, "\"tag_name\":\"", "\"");
			Float v = Float.valueOf(ver.text);
			if(b != null) {
				b.p("服务器(Github)最新版本"+ver.text+"，当前程序版本"+appInfo.appVersion);
			}
			if(v <= Float.valueOf(appInfo.appVersion)) {
				exec = null;
				b.ba.tempData.remove("checkUpdate");
				return;
			}
			
			TextRegion info = cheakDocument.subString(json, "\"body\":\"","\"");
			if(b != null)
			b.debugPrint(info.text);
			int a = info.text.indexOf("\\r\\n完整包下载地址：");
			String infoMessage = info.text.substring(0, a);
			TextRegion allLink = cheakDocument.subString(info.text, "完整包下载地址：", "\\r\\n");
			TextRegion addsLink = cheakDocument.checkCommand(info.text, "增量更新包下载地址：");
//			TextRegion deployToolLink = cheakDocument.subString(info.text, "部署模块下载地址：","\\r\\n");
//			TextRegion deployToolLink = cheakDocument.checkCommand(info.text,"\\r\\n部署模块下载地址：");
//			System.out.println("部署模块地址："+deployToolLink.text);
			updateVersion = ver.text;
			updateInfo = infoMessage;
			updateALLDownloadLink = allLink.text;
			updateAddsDownLoadLink = addsLink.text;
			System.out.println(
					ver.text+"\n"
					+info.text+"\n"
//					+link.text
					+"更新信息："+infoMessage+"\n"
					+"完整包下载地址："+allLink.text+"\n"
					+"增量更新包下载地址："+addsLink.text+"\n"
					);
			System.out.println("正在下载更新文件...");	
			
			showUpdateInfo = new timerInfo(true, "尝试下载更新", null);
			showUpdateInfo.type = 988;
			showUpdateInfo.showProgress = true;
			b.addTimer(showUpdateInfo);
			
			try {
//				download(addsLink.text, "./update");
				File updateFile = new File("./update/"+ver.text);
				new Thread(new Runnable() {
					

					@Override
					public void run() {
						try {
							exec = Runtime.getRuntime().exec("./Tools/wget.exe -c  -t 0 -T 120 -O \""+updateFile.getAbsolutePath().toString()+"\" "+addsLink.text);
							try {
								exec.waitFor();
								if(!progress.equals("100%")) {
									//下载没有完成wget就退出，判定为下载失败
									updateDownloadBad();
								}
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				while(exec == null) {
					Thread.sleep(1000);
				}
				System.out.println("开始读取wget输出");
				StringBuffer read = read(exec.getErrorStream(), System.err,null);
				read.append(read(exec.getInputStream(), System.out,null));
				System.out.println(read);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}while(stil);

	}
	/**
	 * 更新下载出错调用此方法
	 */
	void updateDownloadBad(){
		showUpdateInfo.timerName = "更新下载失败！";
		showUpdateInfo.showProgress = false;
		exec = null;
		b.ba.tempData.remove("checkUpdate");
	}
}
