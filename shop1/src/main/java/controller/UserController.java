package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import dao.UserDao;
import exception.LoginException;
import logic.Item;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired
	private ShopService service;
	
//	@GetMapping("join")
	@GetMapping("*")  // 설정되지 않은 모든 요청시 호출되는 메서드
	public ModelAndView join() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new User());
		return mav;
	}
	
	@PostMapping("join")
	public ModelAndView userAdd(@Valid User user, BindingResult bresult) {
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			//BindingResult.reject() : global error  
			//		=>jsp의 <spring:hasBindingErrors...부분에 오류 출력
			
			//BindingResult.rejectvalue() : global error 
			//		=> jsp의 <form:errors path...부분에 오류 출력
			bresult.reject("error.input.user"); // reject 메서드 : global error에 추가
			bresult.reject("error.input.check"); // reject 메서드 : global error에 추가
			return mav;
		}
		// 정상 입력 시 : 회원 가입 하기 => db의 useraccount 테이블에 저장
		try {
			service.userinsert(user);
			mav.addObject("user", user);
		} catch (DataIntegrityViolationException e) {
			// DataIntegrityViolationException : db에 중복 key 오류시 발생되는 예외 객체
			e.printStackTrace();
			bresult.reject("error.duplicate.user"); // global오류로 등록
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.setViewName("redirect:login");
		return mav;
	}
	
	@PostMapping("login")
	public ModelAndView login(@Valid User user, BindingResult bresult, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		if (bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			bresult.reject("error.login.input");
			return mav;
		}
		
		// 1. userid에 맞는 User를 db에서 조회
		User dbUser = null;
		try {
			dbUser = service.selectUserOne(user.getUserid());
			//System.out.println("dbUser : " +dbUser);
			
		} catch (EmptyResultDataAccessException e) { // 조회된 데이터가 없는 경우 발생
			e.printStackTrace();
			bresult.reject("error.login.id"); // 아이디 확인
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		
		// 2. 비밀번호 검증
		//	- 일치 : session.setAttribute("loginUser",dbUser) => 로그인 정보
		//  - 불일치 : 비밀번호를 확인 하세요. (error.login.password)
				
		// 3. mypage로 페이지 이동 => 성공시 404error 발생
		if (dbUser.getPassword().equals(user.getPassword())) {
			session.setAttribute("loginUser", dbUser);
			mav.setViewName("redirect:mypage?userid="+user.getUserid());
		} else {
			bresult.reject("error.login.password"); // 아이디 확인
			mav.getModel().putAll(bresult.getModel());
		}
		return mav;
	}
	
	@RequestMapping("mypage")
	public ModelAndView idCheckMypage(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUserOne(userid);
		List<Sale> salelist = service.salelist(userid);
		mav.addObject("user",user);
		mav.addObject("salelist",salelist);
		return mav;
	}
	
	/*
	 * AOP 설정하기 : UserLoginAspect 클래스의 userIdCheck 메서드로 구현하기.
	 * 1. pointcut : UserController 클래스의 idCheck로 시작하는 메서드이고,
	 * 				 마지막 매개변수의 userid, session인 경우
	 * 2. 로그인 여부 검증
	 * 		- 로그인이 안된 경우, '로그인이 필요합니다.' login.jsp로 호출
	 * 3. admin이 아니고, 로그인 아이디(세션)와 파라미터 userid값이 다른 경우
	 * 		- '본인만 이용 가능 합니다.' item/list 페이지 호출
	 */
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}
	//로그인상태,본인정보만조회 검증 =>AOP클래스()
	@GetMapping({"update","delete"})
	public ModelAndView idCheckUser(String userid, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = service.selectUserOne(userid);
		mav.addObject("user",user);
		return mav;
	}
	@PostMapping("update")
	   public ModelAndView update(@Valid User user, BindingResult bresult, String userid, HttpSession session) {
	      ModelAndView mav = new ModelAndView();
	      /*
	      if (bresult.hasErrors()) {
	         mav.getModel().putAll(bresult.getModel());
	         bresult.reject("error.login.input");
	         return mav;
	      }*/
	      
	      User loginUser = (User)session.getAttribute("loginUser");
	      if(!loginUser.getPassword().equals(user.getPassword())) {
	         mav.getModel().putAll(bresult.getModel());
	         bresult.reject("error.login.password"); // 아이디 확인
	         return mav;
	      }
	      
	      try {
	         service.userUpdate(user);
	         if(loginUser.getUserid().equals(user.getUserid()))
	            session.setAttribute("loginUser", user);
	         mav.setViewName("redirect:mypage?userid="+user.getUserid());
	      } catch (Exception e){
	         e.printStackTrace();
	         throw new LoginException("고객 정보 수정 실패.", "update?userid="+user.getUserid());
	      }
	      return mav;
	   }
	
	/*
	 * 탈퇴검중
	 * UserLoginAspect.userIdCheck() 메서드 실행 하도록 설정
	 * 
	 * 회원탈퇴
	 * 1.파라미터 정보 저장. (userid,password)
	 * 	-관리자인경우 탈퇴 불가
	 * 2.비밀번호 검증 => 로그인된 비밀번호 검증
	 * 	본인탈퇴 : 본인비밀번호
	 * 	관리자가 타인탈퇴 : 관리자 빔리번호
	 * 3.비밀번호 불일치
	 * 	메세지 출력 후 delete 페이지 이동
	 * 4.비밀번호 일치
	 * 	db에서 해당 사용자 정보 삭제하기
	 * 본인탈퇴 : 로그아웃,로그인 페이지 이동
	 * 관리자탈퇴 : admin/list페이지 이동 =>404
	 */
	@PostMapping("delete")
	   public ModelAndView idCheckdelete(String password, String userid, HttpSession session) {
	      ModelAndView mav = new ModelAndView();
	      User loginUser = (User)session.getAttribute("loginUser");
	      
	      if(userid.equals("admin"))
	    	  throw new LoginException("관리자는 탈퇴 불가","mypage?userid="+userid);
	      
	      if(!password.equals(loginUser.getPassword())) {
	    	  throw new LoginException("비밀번호가 다릅니다","delete?userid="+userid);
	      }
	      try {
	    	  service.userDelete(userid);
	      }catch (DataIntegrityViolationException e) {
	    	  throw new LoginException("주문정보가 있어요","mypage?userid="+userid);
	      }catch (Exception e) {
	    	  e.printStackTrace();
	    	  throw new LoginException("탈퇴시 오류 발생","delete?userid="+userid);
	      }
	      if(loginUser.getUserid().equals("admin")) {
	    	  mav.setViewName("redirect:admin/list");
	      }else {
	    	  session.invalidate();
	    	  mav.setViewName("redirect:login");
	      }
	         return mav;
	      }
	
	/*
	 * UserLoginAspect. loginCheck() : UserController.loginCheck*(..)인 메서드
	 * 									마지막 배개변수 HttpSession인 메서드
	 * 1.로그인 검증 => AOP 클래스.
	 * 2.파라미터값 저장 (매개변수)
	 * 3.현재비밀번호와 입력된 비밀번호 검증
	 *   불일치:오류메세지 출력. password 페이지 이동
	 * 4.일치 : db수정
	 * 5.성공 : 로그인정보 변경, mypage 페이지 이동
	 * 		실패: 오류메세지 출력. password 페이지 이동
	 * 
	 */
	@PostMapping("password")
	   public ModelAndView idCheckpassword(String password, String chgpass, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User loginUser = (User)session.getAttribute("loginUser");
		
		if(!password.equals(loginUser.getPassword())) {
			throw new LoginException("비밀번호가 다릅니다","password");
		}
		try {
			service.userChgpass(loginUser.getUserid(),chgpass);
			mav.setViewName("redirect:mypage?userid="+loginUser.getUserid());
			loginUser.setPassword(chgpass);
		}catch (Exception e) {
			throw new LoginException("비번변경하려했는데 이상해요","password");
		}
		return mav;
	}
	
	//"{url}serach" : {url} 값이 지정되지 않음. *search 요청시 호출되는 메서드
	@PostMapping("{url}search")
	public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) {
		//@PathVariable : "{url}"의 이름을 매개변수로 전달
		//	요청 : idsearch : url <="id"
		//	요청 : pwsearch : url <="pw"
		ModelAndView mav = new ModelAndView();
		String code = "error.userid.search";
		String title= "아이디";
		if(url.equals("pw")) {
			title = "비밀번호";
			if(user.getUserid() == null || user.getUserid().trim().equals("")) {
				bresult.rejectValue("userid","error.required");
			}
		}
		if(user.getEmail() == null || user.getEmail().trim().equals("")) {
			bresult.rejectValue("email","error.required");
		}
		if(user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
			bresult.rejectValue("phoneno","error.required");
		}
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		/*
		 * user.getUserid() == null : 아이디찾기
		 * user.getUserid() != null : 비밀번호 찾기
		 */
		
		if(user.getUserid() != null && user.getUserid().trim().equals("")) user.setUserid(null);
		String result = service.getSearch(user);
		if(result ==null) {
			bresult.reject(code);
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		mav.addObject("result",result);
		mav.addObject("title",title);
		mav.setViewName("search");
		return mav;
	}
	
	
	
}