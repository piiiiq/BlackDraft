package yang.demo.allPurpose;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class httpGet {
	/**
	 * 检查给定url是否可以访问
	 * @param url
	 * @return
	 */
	public static boolean checkUrl(String url) {
		boolean checkUrl = true;
		URL r = null;
		try {
			r = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection con = null;
		try {
			con = r.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			con.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return false;
		}
		return true;
	}
	public static String httpGet(String httpUrl) {
		BufferedReader input = null;
		StringBuilder sb = null;
		URL url = null;
		HttpURLConnection con = null;
		try {
			url = new URL(httpUrl);
			try {
				// trust all hosts
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection)url.openConnection();
				if (url.getProtocol().toLowerCase().equals("https")) {
					https.setHostnameVerifier(DO_NOT_VERIFY);
					con = https;
				} else {
					con = (HttpURLConnection)url.openConnection();
				}
				input = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
				sb = new StringBuilder();
				String s;
				while ((s = input.readLine()) != null) {
					sb.append(s).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} finally {
			// close buffered
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// disconnecting releases the resources held by a connection so they may be closed or reused
			if (con != null) {
				con.disconnect();
			}
		}

		return sb == null ? null : sb.toString();
	}

	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

													public boolean verify(String hostname, SSLSession session) {
														return true;
													}
												};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		final String TAG = "trustAllHosts";
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}


			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws java.security.cert.CertificateException {
				// TODO Auto-generated method stub
				
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
