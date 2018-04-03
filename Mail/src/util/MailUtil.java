package util;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 邮件工具类
 */
public class MailUtil {
	static Properties prop = null;
	private static String FROM = null;
	private static String Name = null;
	private static String MailPassword = null;
	/**
	 * 获取配置文件参数
	 */
	static {
		// Properties类用于读取.properties文件
		prop = new Properties();
		//文件输入流
		FileInputStream file = null;
		try {
			//在web环境下获得根目录下的config.properties
			String path=Thread.currentThread().getContextClassLoader().getResource("").getPath()+"config.properties";
			file = new FileInputStream(path.substring(1));
			// new FileInputStream("config.properties"); 在java Project中获得目录下的
			//加载到Properties对象中进行解析
			prop.load(file);
			//关流
			file.close();
			//利用键得到值
			FROM = prop.getProperty("FROM").trim();
			Name = prop.getProperty("nickName").trim();
			MailPassword = prop.getProperty("MailPassword").trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 创建邮件对象
	 * @author JayGoal
	 * 2018年3月26日上午10:06:13
	 * @return
	 */
	private static Message getMessage() {
		// 创建连接对象 连接到邮件服务器
				Properties properties = new Properties();
				// 设置发送邮件的基本参数
				// 发送邮件服务器
				properties.put("mail.smtp.host", "smtp.163.com");
				// 发送端口
				properties.put("mail.smtp.port", "25");
				properties.put("mail.smtp.auth", "true");
				// 设置发送邮件的账号和密码
				Session session = Session.getInstance(properties, new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						// 两个参数分别是发送邮件的账户和密码
						return new PasswordAuthentication(FROM,MailPassword);
					}
				});

				// 创建邮件对象
				Message message = new MimeMessage(session);
				return message;
	}
	/**
	 * 
	 *2017年11月9日下午4:42:55
	 * @param to 
	 * @param text
	 * @param theme
	 * @throws MessagingException
	 */
	private static void send_mail(String to, String text,String theme) throws MessagingException {
		
		Message message = getMessage();
		// 设置发件人
		message.setFrom(new InternetAddress(FROM));
		// 设置收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//设置自定义发件人昵称    
        String nick="";    
        try {    
            nick=javax.mail.internet.MimeUtility.encodeText(Name);    
        } catch (UnsupportedEncodingException e) {    
            e.printStackTrace();    
        }     
        message.setFrom(new InternetAddress(nick+" <"+FROM+">"));
		// 设置主题
		message.setSubject(theme);
		// 设置邮件正文 第二个参数是邮件发送的类型
		message.setContent(text, "text/html;charset=UTF-8");
		// 发送一封邮件
		Transport.send(message);
	}
	
	/**
	 * 发送一组图片
	 * @author JayGoal
	 * 2018年3月26日上午10:28:44
	 * @param to
	 * @param text
	 * @param theme
	 * @param picPath
	 * @throws AddressException
	 * @throws MessagingException 
	 * @throws UnsupportedEncodingException
	 */
	private static void sendMailWithPic(String to, String text,String theme,String... picPath) throws AddressException, MessagingException, UnsupportedEncodingException {
		Message message = getMessage();
		// 设置发件人
		message.setFrom(new InternetAddress(FROM));
		// 设置收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//设置自定义发件人昵称    
        String nick="";    
        try {    
            nick=javax.mail.internet.MimeUtility.encodeText(Name);    
        } catch (UnsupportedEncodingException e) {    
            e.printStackTrace();    
        }     
        message.setFrom(new InternetAddress(nick+" <"+FROM+">"));
		// 设置主题
		message.setSubject(theme);
		// 设置邮件正文 第二个参数是邮件发送的类型
		//message.setContent(text, "text/html;charset=UTF-8");
		MimeBodyPart mailText = new MimeBodyPart();
		mailText.setContent(text,"text/html;charset=UTF-8");
		
		int picNums = picPath.length;
		MimeBodyPart[] pics = new MimeBodyPart[picNums];
		MimeMultipart mm = new MimeMultipart();
		mm.addBodyPart(mailText);
		//循环创建附件
		for(int i=0;i<picNums;i++) {
			pics[i] = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(picPath[i]));  
			pics[i].setDataHandler(dh);  
	        String filename = dh.getName();  
	        // MimeUtility 是一个工具类，encodeText（）用于处理附件字，防止中文乱码问题  
	        pics[i].setFileName(MimeUtility.encodeText(filename));
	        mm.addBodyPart(pics[i]);
		}
		mm.setSubType("mixed");// 设置正文与附件之间的关系  
		  
        message.setContent(mm);  
        message.saveChanges(); // 保存修改
		// 发送一封邮件
		Transport.send(message);
	}
	/**
	 * 发送邮件
	 *2017年11月9日下午4:43:05
	 * @param aimsEmail 目标邮箱
	 * @param text 内容
	 * @param theme 主题
	 */
	public static void sendMail(String aimsEmail, String text,String theme) {
		try {
			send_mail(aimsEmail, text, theme);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @author JayGoal
	 * 2018年3月26日上午10:32:04
	 * @param to 目标邮箱
	 * @param text 内容
	 * @param theme	主题
	 * @param picPath	图片路径
	 */
	public static void sendMail(String to, String text,String theme,String... picPath) {
		try {
			sendMailWithPic(to, text, theme, picPath);
		} catch (UnsupportedEncodingException | MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
