package yang.demo.mail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class showMail {
	private MimeMessage mimeMessage = null;
	private String saveAttachPath = "";
	private StringBuffer bodyText = new StringBuffer();
	private String dateFormat = "yy-MM-dd HH:mm";

	public showMail() {
	}

	public showMail(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public String getFrom() throws Exception {
		InternetAddress[] address = (InternetAddress[]) this.mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null) {
			from = "";
		}
		String personal = address[0].getPersonal();
		if (personal == null) {
			personal = "";
		}
		String fromAddr = null;
		if ((personal != null) || (from != null)) {
			fromAddr = personal + "<" + from + ">";
		}
		return fromAddr;
	}

	public String getMailAddress(String type) throws Exception {
		String mailAddr = "";
		String addType = type.toUpperCase();

		InternetAddress[] address = null;
		if ((addType.equals("TO")) || (addType.equals("CC")) || (addType.equals("BCC"))) {
			if (addType.equals("TO")) {
				address = (InternetAddress[]) this.mimeMessage.getRecipients(Message.RecipientType.TO);
			} else if (addType.equals("CC")) {
				address = (InternetAddress[]) this.mimeMessage.getRecipients(Message.RecipientType.CC);
			} else {
				address = (InternetAddress[]) this.mimeMessage.getRecipients(Message.RecipientType.BCC);
			}
			if (address != null) {
				for (int i = 0; i < address.length; i++) {
					String emailAddr = address[i].getAddress();
					if (emailAddr == null) {
						emailAddr = "";
					} else {
						emailAddr = MimeUtility.decodeText(emailAddr);
					}
					String personal = address[i].getPersonal();
					if (personal == null) {
						personal = "";
					} else {
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + emailAddr + ">";

					mailAddr = mailAddr + "," + compositeto;
				}
				mailAddr = mailAddr.substring(1);
			}
		} else {
			throw new Exception("错误的电子邮件类型!");
		}
		return mailAddr;
	}

	public String getSubject() throws MessagingException {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(this.mimeMessage.getSubject());
			if (subject == null) {
				subject = "";
			}
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		return subject;
	}

	public String getSentDate() throws Exception {
		Date sentDate = this.mimeMessage.getSentDate();

		SimpleDateFormat format = new SimpleDateFormat(this.dateFormat);
		String strSentDate = format.format(sentDate);

		return strSentDate;
	}

	public String getBodyText() {
		return this.bodyText.toString();
	}

	public void getMailContent(Part part, boolean onlyplain) throws Exception {
		String contentType = part.getContentType();

		int nameIndex = contentType.indexOf("name");

		boolean conName = false;
		if (nameIndex != -1) {
			conName = true;
		}
		if (onlyplain) {
			if ((part.isMimeType("text/plain")) && (!conName)) {
				this.bodyText.append((String) part.getContent());
			}
		} else if ((part.isMimeType("text/plain")) && (!conName)) {
			this.bodyText.append((String) part.getContent());
		} else if ((part.isMimeType("text/html")) && (!conName)) {
			this.bodyText.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i), true);
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent(), true);
		}
	}

	public boolean getReplySign() throws MessagingException {
		boolean replySign = false;

		String[] needReply = this.mimeMessage.getHeader("Disposition-Notification-To");
		if (needReply != null) {
			replySign = true;
		}
		return replySign;
	}

	public String getMessageId() throws MessagingException {
		String messageID = this.mimeMessage.getMessageID();

		return messageID;
	}

	public boolean isNew() throws MessagingException {
		boolean isNew = false;
		Flags flags = this.mimeMessage.getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isNew = true;
			}
		}
		return isNew;
	}

	public boolean isContainAttach(Part part) throws Exception {
		boolean attachFlag = false;
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mPart = mp.getBodyPart(i);
				String disposition = mPart.getDisposition();
				if ((disposition != null) && ((disposition.equals("attachment")) || (disposition.equals("inline")))) {
					attachFlag = true;
				} else if (mPart.isMimeType("multipart/*")) {
					attachFlag = isContainAttach(mPart);
				} else {
					String conType = mPart.getContentType();
					if (conType.toLowerCase().indexOf("application") != -1) {
						attachFlag = true;
					}
					if (conType.toLowerCase().indexOf("name") != -1) {
						attachFlag = true;
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachFlag = isContainAttach((Part) part.getContent());
		}
		return attachFlag;
	}

	public void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mPart = mp.getBodyPart(i);
				String disposition = mPart.getDisposition();
				if ((disposition != null) && ((disposition.equals("attachment")) || (disposition.equals("inline")))) {
					fileName = mPart.getFileName();
					if (fileName.toLowerCase().indexOf("gb2312") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mPart.getInputStream());
				} else if (mPart.isMimeType("multipart/*")) {
					saveAttachMent(mPart);
				} else {
					fileName = mPart.getFileName();
					if ((fileName != null) && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mPart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent());
		}
	}

	public void setAttachPath(String attachPath) {
		this.saveAttachPath = attachPath;
	}

	public void setDateFormat(String format) throws Exception {
		this.dateFormat = format;
	}

	public String getAttachPath() {
		return this.saveAttachPath;
	}

	/* Error */
	private void saveFile(String fileName, java.io.InputStream in) throws Exception {
		// Byte code:
		// 0: ldc_w 307
		// 3: invokestatic 309 java/lang/System:getProperty
		// (Ljava/lang/String;)Ljava/lang/String;
		// 6: astore_3
		// 7: aload_0
		// 8: invokevirtual 314 yang/demo/mail/showMail:getAttachPath
		// ()Ljava/lang/String;
		// 11: astore 4
		// 13: ldc 19
		// 15: astore 5
		// 17: aload_3
		// 18: ifnonnull +6 -> 24
		// 21: ldc 19
		// 23: astore_3
		// 24: aload_3
		// 25: invokevirtual 272 java/lang/String:toLowerCase ()Ljava/lang/String;
		// 28: ldc_w 316
		// 31: invokevirtual 171 java/lang/String:indexOf (Ljava/lang/String;)I
		// 34: iconst_m1
		// 35: if_icmpeq +31 -> 66
		// 38: ldc_w 318
		// 41: astore 5
		// 43: aload 4
		// 45: ifnull +13 -> 58
		// 48: aload 4
		// 50: ldc 19
		// 52: invokevirtual 92 java/lang/String:equals (Ljava/lang/Object;)Z
		// 55: ifeq +21 -> 76
		// 58: ldc_w 320
		// 61: astore 4
		// 63: goto +13 -> 76
		// 66: ldc_w 322
		// 69: astore 5
		// 71: ldc_w 324
		// 74: astore 4
		// 76: new 326 java/io/File
		// 79: dup
		// 80: new 58 java/lang/StringBuilder
		// 83: dup
		// 84: aload 4
		// 86: invokestatic 60 java/lang/String:valueOf
		// (Ljava/lang/Object;)Ljava/lang/String;
		// 89: invokespecial 66 java/lang/StringBuilder:<init> (Ljava/lang/String;)V
		// 92: aload 5
		// 94: invokevirtual 71 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 97: aload_1
		// 98: invokevirtual 71 java/lang/StringBuilder:append
		// (Ljava/lang/String;)Ljava/lang/StringBuilder;
		// 101: invokevirtual 77 java/lang/StringBuilder:toString ()Ljava/lang/String;
		// 104: invokespecial 328 java/io/File:<init> (Ljava/lang/String;)V
		// 107: astore 6
		// 109: aconst_null
		// 110: astore 7
		// 112: aconst_null
		// 113: astore 8
		// 115: new 329 java/io/BufferedOutputStream
		// 118: dup
		// 119: new 331 java/io/FileOutputStream
		// 122: dup
		// 123: aload 6
		// 125: invokespecial 333 java/io/FileOutputStream:<init> (Ljava/io/File;)V
		// 128: invokespecial 336 java/io/BufferedOutputStream:<init>
		// (Ljava/io/OutputStream;)V
		// 131: astore 7
		// 133: new 339 java/io/BufferedInputStream
		// 136: dup
		// 137: aload_2
		// 138: invokespecial 341 java/io/BufferedInputStream:<init>
		// (Ljava/io/InputStream;)V
		// 141: astore 8
		// 143: goto +15 -> 158
		// 146: aload 7
		// 148: iload 9
		// 150: invokevirtual 344 java/io/BufferedOutputStream:write (I)V
		// 153: aload 7
		// 155: invokevirtual 348 java/io/BufferedOutputStream:flush ()V
		// 158: aload 8
		// 160: invokevirtual 351 java/io/BufferedInputStream:read ()I
		// 163: dup
		// 164: istore 9
		// 166: iconst_m1
		// 167: if_icmpne -21 -> 146
		// 170: goto +36 -> 206
		// 173: astore 9
		// 175: aload 9
		// 177: invokevirtual 139 java/lang/Exception:printStackTrace ()V
		// 180: new 41 java/lang/Exception
		// 183: dup
		// 184: ldc_w 354
		// 187: invokespecial 126 java/lang/Exception:<init> (Ljava/lang/String;)V
		// 190: athrow
		// 191: astore 10
		// 193: aload 7
		// 195: invokevirtual 356 java/io/BufferedOutputStream:close ()V
		// 198: aload 8
		// 200: invokevirtual 359 java/io/BufferedInputStream:close ()V
		// 203: aload 10
		// 205: athrow
		// 206: aload 7
		// 208: invokevirtual 356 java/io/BufferedOutputStream:close ()V
		// 211: aload 8
		// 213: invokevirtual 359 java/io/BufferedInputStream:close ()V
		// 216: return
		// Line number table:
		// Java source line #358 -> byte code offset #0
		// Java source line #359 -> byte code offset #7
		// Java source line #360 -> byte code offset #13
		// Java source line #361 -> byte code offset #17
		// Java source line #362 -> byte code offset #21
		// Java source line #364 -> byte code offset #24
		// Java source line #365 -> byte code offset #38
		// Java source line #366 -> byte code offset #43
		// Java source line #367 -> byte code offset #58
		// Java source line #368 -> byte code offset #63
		// Java source line #369 -> byte code offset #66
		// Java source line #370 -> byte code offset #71
		// Java source line #372 -> byte code offset #76
		// Java source line #377 -> byte code offset #109
		// Java source line #378 -> byte code offset #112
		// Java source line #381 -> byte code offset #115
		// Java source line #382 -> byte code offset #133
		// Java source line #384 -> byte code offset #143
		// Java source line #385 -> byte code offset #146
		// Java source line #386 -> byte code offset #153
		// Java source line #384 -> byte code offset #158
		// Java source line #388 -> byte code offset #170
		// Java source line #389 -> byte code offset #175
		// Java source line #390 -> byte code offset #180
		// Java source line #391 -> byte code offset #191
		// Java source line #392 -> byte code offset #193
		// Java source line #393 -> byte code offset #198
		// Java source line #394 -> byte code offset #203
		// Java source line #392 -> byte code offset #206
		// Java source line #393 -> byte code offset #211
		// Java source line #395 -> byte code offset #216
		// Local variable table:
		// start length slot name signature
		// 0 217 0 this showMail
		// 0 217 1 fileName String
		// 0 217 2 in java.io.InputStream
		// 6 19 3 osName String
		// 11 74 4 storeDir String
		// 15 78 5 separator String
		// 107 17 6 storeFile java.io.File
		// 110 97 7 bos java.io.BufferedOutputStream
		// 113 99 8 bis java.io.BufferedInputStream
		// 146 3 9 c int
		// 164 3 9 c int
		// 173 3 9 exception Exception
		// 191 13 10 localObject Object
		// Exception table:
		// from to target type
		// 115 170 173 java/lang/Exception
		// 115 191 191 finally
	}

	public static Message[] receive(String username, String password, String host) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Store store = session.getStore("pop3");
		store.connect(host, username, password);

		Folder folder = store.getFolder("INBOX");
		folder.open(1);
		Message[] message = folder.getMessages();

		showMail re = null;

		return message;
	}

	public static void main(String[] args) throws Exception {
		String host = "pop.163.com";
		String username = "yangisboy@163.com";
		String password = "dxy13633528994";

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Store store = session.getStore("pop3");
		store.connect(host, username, password);

		Folder folder = store.getFolder("INBOX");
		folder.open(1);
		Message[] message = folder.getMessages();

		showMail re = null;
		for (int i = 0; i < message.length; i++) {
			re = new showMail((MimeMessage) message[i]);

			re.setDateFormat("yy年MM月dd日　HH:mm");

			re.setAttachPath("e:\\");
			re.saveAttachMent(message[i]);
		}
	}
}
