package service.impl;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ifp.wechat.constant.ConstantWeChat;

import service.CoreService;
import util.MailUtil;
import wechat.message.resp.TextMessage;
import wechat.util.MessageUtil;
 
public class CoreServiceImpl implements CoreService{  
  
    public static Logger log = Logger.getLogger(CoreServiceImpl.class);
    private String path = null;  
      
    @Override  
    public String processRequest(HttpServletRequest request) {  
        String respMessage = null;
        
        try {  
            // xml请求解析  
            Map<String, String> requestMap = MessageUtil.parseXml(request);  
  
            // 发送方帐号（open_id）  
            String fromUserName = requestMap.get("FromUserName");  
            // 公众帐号  
            String toUserName = requestMap.get("ToUserName");  
            // 消息类型  
            String msgType = requestMap.get("MsgType");  
            
            String picUrl = requestMap.get("PicUrl");
            
            
            String event = requestMap.get("Event");
  
            TextMessage textMessage = new TextMessage();  
            textMessage.setToUserName(fromUserName);  
            textMessage.setFromUserName(toUserName);  
            textMessage.setCreateTime(new Date().getTime());  
            textMessage.setMsgType(ConstantWeChat.RESP_MESSAGE_TYPE_TEXT);  
            textMessage.setFuncFlag(0);  
            
            
            if(msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_EVENT)){
            	if(event.equals("subscribe")){
            		textMessage.setContent("欢迎关注小黑屋！请先发送图片，再发送分类！"); 
                    respMessage = MessageUtil.textMessageToXml(textMessage);
            	}
            }else if(msgType.equals(ConstantWeChat.REQ_MESSAGE_TYPE_IMAGE)) {
            	String imgType = picUrl.substring(picUrl.indexOf("_")+1,picUrl.indexOf("_")+4);
            	System.out.println(imgType);
            	String imgName = UUID.randomUUID().toString()+"."+imgType;
            	String realPath = path+"\\"+imgName;
            	URL url = new URL(picUrl);
            	URLConnection con = url.openConnection();
            	InputStream  in = con.getInputStream();
            	
            	FileOutputStream out = new FileOutputStream(realPath);
            	
            	//边读边写
				int i;
				while((i = in.read()) != -1){
					out.write(i);
				}
				//释放资源
				out.close();
				in.close();
				MailUtil.sendMail("936290344@qq.com", "请下载", "照片", realPath);
				textMessage.setContent("图片接收完成!"); 
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
              
              
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return respMessage;  
    }  
    
    public void getPath(String str) {
    	path = str;
    }
  
  
}  
