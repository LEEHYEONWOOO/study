package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Item;
import logic.Mail;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	private ShopService service;
/*
 * AdminController의 모든 메서드는 관리자 로그인이 필요함
 * 	=>AOP로 설정
 * 		1. 로그아웃상태=>로그인하세요. login페이지 이동
 * 		2. 관리자 로그인이 아닌 경우 => 관리자만 거래가능합니다. mypage이동
 */
	@RequestMapping("list")
	public ModelAndView userAdd(String sort, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		List<User> list = service.userList(); // itemList : item 테이블에 있는 모든 정보를 Item 객체의 List로 저장
		//if(sort.equals("")||sort==null) sort="";
		//if(sort.equals("10")) sort="order by userid desc";
		//if(sort.equals("11")) sort="order by userid";
		if(sort != null) {
			switch(sort){
			case "10" : 
				Collections.sort(list,(u1,u2)->u1.getUserid().compareTo(u2.getUserid()));
				break;
			case "11" :
				Collections.sort(list,(u1,u2)->u2.getUserid().compareTo(u1.getUserid()));
				break;
			case "20" : 
				Collections.sort(list,(u1,u2)->u1.getUsername().compareTo(u2.getUsername()));
				break;
			case "21" :
				Collections.sort(list,(u1,u2)->u2.getUsername().compareTo(u1.getUsername()));
				break;
			case "30" : 
				Collections.sort(list,(u1,u2)->u1.getPhoneno().compareTo(u2.getPhoneno()));
				break;
			case "31" :
				Collections.sort(list,(u1,u2)->u2.getPhoneno().compareTo(u1.getPhoneno()));
				break;
			case "40" : 
				Collections.sort(list,(u1,u2)->u1.getBirthday().compareTo(u2.getBirthday()));
				break;
			case "41" :
				Collections.sort(list,(u1,u2)->u2.getBirthday().compareTo(u1.getBirthday()));
				break;
			case "50" : 
				Collections.sort(list,(u1,u2)->u1.getEmail().compareTo(u2.getEmail()));
				break;
			case "51" :
				Collections.sort(list,(u1,u2)->u2.getEmail().compareTo(u1.getEmail()));
				break;
			
			}
		}
		
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping("mailForm")
	public ModelAndView mailForm(String[] idchks, HttpSession session) {
		//String[] idchks : idchks 파라미터의 값 여러개 가능. request.getParameterValues("파라미터")
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length==0) {
			throw new LoginException("메일을 보낼 대상자를 선택하세요","list");
		}
		List<User> list = service.getUserList(idchks); 
		System.out.println("왔냐?"+service.getUserList(idchks));
		mav.addObject("list",list);
		return mav;
	}
	@RequestMapping("mail")
	public ModelAndView mail (Mail mail, HttpSession session) {
		ModelAndView mav = new ModelAndView("alert");
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(session.getServletContext().getRealPath("/")+"/WEB-INF/classes/mail.properties");
			prop.load(fis);
			prop.put("mail.smtp.user", mail.getNaverid());
		}catch (IOException e) {
			e.printStackTrace();
		}
		mailSend(mail,prop);
		mav.addObject("message","메일 전송이 완료되었습니다.");
		mav.addObject("url","list");
		return mav;
	}
	
	private void mailSend(Mail mail, Properties prop) {
	      MyAuthenticator auth = new MyAuthenticator(mail.getNaverid(),mail.getNaverpw());
	      Session session = Session.getInstance(prop,auth);
	      MimeMessage msg = new MimeMessage(session);
	      try {
	         msg.setFrom(new InternetAddress(mail.getNaverid() + "@naver.com"));
	         List<InternetAddress> addrs = new ArrayList<InternetAddress>();
	         String[] emails = mail.getRecipient().split(",");
	         for(String email : emails) {
	            try {
	            	System.out.println("@@@@@@@@@@@@@@@@@@"+new String(email.getBytes("utf-8"),"8859_1"));
	               addrs.add(new InternetAddress(new String(email.getBytes("utf-8"),"8859_1")));
	            } catch(UnsupportedEncodingException ue) {
	               ue.printStackTrace();
	            }
	         }
	         InternetAddress[] arr = new InternetAddress[emails.length];
	         for(int i=0; i<addrs.size(); i++) {
	            arr[i] = addrs.get(i);
	         }
	         msg.setRecipients(Message.RecipientType.TO, arr); // 수신메일 설정
	         msg.setSentDate(new Date()); // 전송일자
	         msg.setSubject(mail.getTitle()); // 제목
	         MimeMultipart multipart = new MimeMultipart();
	         MimeBodyPart message = new MimeBodyPart();
	         message.setContent(mail.getContents(),mail.getMtype());
	         multipart.addBodyPart(message);
	         // 첨부파일 추가
	         for (MultipartFile mf : mail.getFile1()) {
	            if((mf != null) && (!mf.isEmpty())) {
	               multipart.addBodyPart(BodyPart(mf));
	            }
	         } // for
	         msg.setContent(multipart);
	         Transport.send(msg);
	      } catch (MessagingException me) {
	         me.printStackTrace();
	      }
	   }
	
	private BodyPart BodyPart(MultipartFile mf) {
	      MimeBodyPart body = new MimeBodyPart();
	      String orgFile = mf.getOriginalFilename();
	      String path = "c:/mailupload/";
	      File f1 = new File(path);
	      if(!f1.exists()) f1.mkdirs();
	      File f2 = new File(path + orgFile);
	      try {
	         mf.transferTo(f2);
	         body.attachFile(f2);
	         body.setFileName(new String(orgFile.getBytes("UTF-8"),"8859_1"));
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return body;
	   }
	
	
	//AdminController 클래스의 내부 클래스로 구현
	public final class MyAuthenticator extends Authenticator {
	     private String id;
	     private String pw;
	     public MyAuthenticator(String id, String pw) {
	    	 this.id = id;
	    	 this.pw = pw;
	     }
	     @Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO Auto-generated method stub
			return new PasswordAuthentication(id,pw);
		}
	
	}
}
