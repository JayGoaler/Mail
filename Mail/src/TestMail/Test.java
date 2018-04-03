package TestMail;

import javax.mail.MessagingException;

/**
 * 测试类
 */
public class Test {
    public static void main(String[] args) throws MessagingException {//930437313
		util.MailUtil.sendMail("930437313@qq.com", "通过酷炫的Java程序发送！", "三组进度","E:\\JavaLearn\\三组进度.rar");
		}
}
