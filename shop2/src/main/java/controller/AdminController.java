package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import exception.LoginException;
import logic.Mail;
import logic.ShopService;
import logic.User;
/*
 * AdminController의 모든 메서드들은 반드시 관리자로 로그인 해야함.
 * AOP 설정 : AdminLoginAspect클래스 adminCheck 메서드
 *   1. 로그아웃 상태 : 로그인 하세요. , login 페이지로 이동
 *   2. 관리자 로그인이 아닌경우 : 관리자만 가능한 거래입니다. mypage로 이동
 * 
 */
@Controller
@RequestMapping("admin")
public class AdminController {
	
	@Autowired
	private ShopService service;
	
	@RequestMapping("list")
	public ModelAndView list(String sort,HttpSession session) {
		System.out.println("동작");
		ModelAndView mav = new ModelAndView();
		List<User> list = service.userlist();
		if (sort != null) {
			switch(sort) {
				case "10" : Collections.sort(list,(u1,u2)->u1.getUserid().compareTo(u2.getUserid()));
					break;
				case "11" : Collections.sort(list,(u1,u2)->u2.getUserid().compareTo(u1.getUserid()));
					break;
				case "20" : Collections.sort(list,(u1,u2)->u1.getUsername().compareTo(u2.getUsername()));
					break;
				case "21" : Collections.sort(list,(u1,u2)->u2.getUsername().compareTo(u1.getUsername()));
					break;
				case "30" : Collections.sort(list,(u1,u2)->u1.getPhoneno().compareTo(u2.getPhoneno()));
					break;
				case "31" : Collections.sort(list,(u1,u2)->u2.getPhoneno().compareTo(u1.getPhoneno()));
					break;
				case "40" : Collections.sort(list,(u1,u2)->u1.getBirthday().compareTo(u2.getBirthday()));
					break;
				case "41" : Collections.sort(list,(u1,u2)->u2.getBirthday().compareTo(u1.getBirthday()));
					break;
				case "50" : Collections.sort(list,(u1,u2)->u1.getEmail().compareTo(u2.getEmail()));
					break;
				case "51" : Collections.sort(list,(u1,u2)->u2.getEmail().compareTo(u1.getEmail()));
					break;	
			}
		}
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping("mailForm")
	public ModelAndView mailform(String[] idchks, HttpSession session) {
		// String[] idchks : idchks 파라미터의 값이 여러개 이기 때문
		// -> request.getParameterValues("파라미터")와 동일한 기능
		ModelAndView mav = new ModelAndView("admin/mail");
		if(idchks == null || idchks.length == 0) {
			throw new LoginException("대상자를 선택 하세요.", "list");
		}
		List<User> list = service.getUserList(idchks);
		mav.addObject("list",list);
		return mav;
	}
	
	@RequestMapping("mail")
	public ModelAndView mail(Mail mail, HttpSession session) { // mail : 전체 파라미터값을 mail 객체로 받음. mail.jsp -> Mail.java
		ModelAndView mav = new ModelAndView("alert");
		Properties prop = new Properties(); // java.util
		try {
			// mail.properties : resources 폴더에 생성
			// src, resources폴더의 내용은 : WEB-INF/classes에 복사됨.
			FileInputStream fis = new FileInputStream
					(session.getServletContext().getRealPath("/")+"/WEB-INF/classes/mail.properties");
			prop.load(fis);
			prop.put("mail.smtp.user", mail.getNaverid());
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		mailSend(mail,prop);
		mav.addObject("message","메일 전송이 완료 되었습니다.");
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
					System.out.println("@@@@@@@@@@@@@@@@@@@@@"+new String(email.getBytes("utf-8"),"8859_1"));
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
					multipart.addBodyPart(bodyPart(mf));
				}
			} // for
			msg.setContent(multipart);
			Transport.send(msg);
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

	private BodyPart bodyPart(MultipartFile mf) {
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = "c:/mailupload/";
		File f1 = new File(path);
		if(!f1.exists()) f1.mkdirs();
		File f2 = new File(path + orgFile);
		try {
			mf.transferTo(f2);
			body.attachFile(f2);
			body.setFileName(new String(orgFile.getBytes("EUC-KR"),"8859_1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	// AdminController 클래스의 내부 클래스로 구현함.
	private final class MyAuthenticator extends Authenticator {
		private String id;
		private String pw;
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}
		
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(id, pw);
		}
	}
	

} // class