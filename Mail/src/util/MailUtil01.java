package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
public class MailUtil01 {
	/**
	 * 
	 *2017年11月9日下午4:42:55
	 * @param to 
	 * @param text
	 * @param theme
	 * @throws MessagingException
	 */
	public static void send_mail(String to, String text,String theme) throws MessagingException {
		String from = "yj936290344@163.com";
		String nickName = "三号杂货铺";
		String password = "JayGoal520";
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
				return new PasswordAuthentication(from,password);
			}
		});

		// 创建邮件对象
		Message message = new MimeMessage(session);
		// 设置发件人
		message.setFrom(new InternetAddress(from));
		// 设置收件人
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		//设置自定义发件人昵称    
        String nick="";    
        try {    
            nick=javax.mail.internet.MimeUtility.encodeText(nickName);    
        } catch (UnsupportedEncodingException e) {    
            e.printStackTrace();    
        }     
        message.setFrom(new InternetAddress(nick+" <"+from+">"));
		// 设置主题
		message.setSubject(theme);
		// 设置邮件正文 第二个参数是邮件发送的类型
		message.setContent(text, "text/html;charset=UTF-8");
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
}
