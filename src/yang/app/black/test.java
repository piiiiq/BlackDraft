package yang.app.black;

import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import yang.demo.allPurpose.autoDO;
import yang.demo.mail.SendMailBySSL;
import yang.demo.mail.sendMail;

public class test {

 public static void main(String[] args){
	
 }
 
 
 public static void testarraylist(){
	
 }
 public static void a(){
	 long a = 3600L*1000L*24L*60L;
		Calendar cal = Calendar.getInstance();
		long l = cal.getTimeInMillis();
		Time t = new Time(l);
		long newtime = a+l;
		System.out.println(newtime);
		Date d = new Date(newtime);
		System.out.println(d);
 }
 public static void b(){
	 ArrayList<c> al = new ArrayList<c>();
	 al.add(new c("hello world"));
	 File f = new File("d:\\a");
	 FileOutputStream fos = null;
	try {
		fos = new FileOutputStream(f);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 try {
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(al);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
 }
public static void readobject(){
	 ArrayList<c> al = null;
	 File f = new File("D:\\a");
	 FileInputStream fis = null;
	try {
		fis = new FileInputStream(f);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 ObjectInputStream ois = null;
	try {
		ois = new ObjectInputStream(fis);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 try {
		al = (ArrayList<c>)ois.readObject();
		System.out.println(al.get(0).name);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }
}
class c implements Serializable{
	String name;
	public c(String name){
		this.name = name;
	}
}
class testarraylist{
	String name,value;
	public testarraylist(String name, String value){
		this.name = name;
		this.value = value;
	}
}