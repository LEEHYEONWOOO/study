package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import exception.LoginException;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("user")
public class UserController {
	@Autowired 
	private ShopService service;
	
	@GetMapping("test1")
	public ModelAndView test1() throws IOException, ParseException {
		ModelAndView mav = new ModelAndView();
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=cIU8HoBdDJx9IAv4NEQ88GvIfz3eoVBo1LHbEfxRtMKcNlK7xaWgZQexbnedoiqNWqPVRcLQ4JeBb8YhhBW6Cw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20230605", "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0900", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("60", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        JSONParser parser = new JSONParser();
        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장. 	
        JSONObject obj = (JSONObject)parser.parse(sb.toString());
        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
        System.out.println("obj : "+obj);
        JSONArray dataArr = (JSONArray) obj.get("category");
        System.out.println(dataArr);
        // 4. model에 담아준다.
        System.out.println("jsonsb++++ "+dataArr);
        mav.addObject("jsonsb",dataArr);
        System.out.println("sb++++ "+sb.toString());
        
        JSONObject response = (JSONObject)obj.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
        
        for (int i = 0; i<item.size(); i++) {
           System.out.println(item.get(i));
        }
        
        
        
		return mav;
	}
	
//  @GetMapping("join")
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
     User dbUser = service.selectUserOne(user.getUserid());
     if(dbUser == null) { // 조회된 데이터가 없는 경우 발생
        bresult.reject("error.login.id"); // 아이디 확인
        mav.getModel().putAll(bresult.getModel());
        return mav;
     }
     // 2. 비밀번호 검증
     //   - 일치 : session.setAttribute("loginUser",dbUser) => 로그인 정보
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
   * mypage AOP 설정하기 : UserLoginAspect 클래스의 userIdCheck 메서드로 구현하기.
   * 1. pointcut : UserController 클래스의 idCheck로 시작하는 메서드이고,
   *              마지막 매개변수의 userid, session인 경우
   * 2. 로그인 여부 검증
   *       - 로그인이 안된 경우, '로그인이 필요합니다.' login.jsp로 호출
   * 3. admin이 아니고, 로그인 아이디(세션)와 파라미터 userid값이 다른 경우
   *       - '본인만 이용 가능 합니다.' item/list 페이지 호출
   */
  
  @RequestMapping("logout")
  public String logout(HttpSession session) {
     session.invalidate();
     return "redirect:login";
  }
  
  @GetMapping({"update","delete"}) // 본인 정보만 조회해야 하기 때문에 설정함
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
  
  @PostMapping("delete")
  public String idCheckDelete(String password, String userid, HttpSession session) {
     if(userid.equals("admin")) {
        throw new LoginException("관리자는 탈퇴가 불가능합니다.", "mypage?userid="+userid);
     }
     
     User loginUser = (User)session.getAttribute("loginUser");
     // password = 현재 입력한 비밀번호
     // loginUser.getPassword() : 로그인한 사용자의 비밀번호
     if(!password.equals(loginUser.getPassword())) {
        throw new LoginException("비밀번호를 확인하세요.", "delete?userid="+userid);
     }
     try {
        service.userDelete(userid); // 비밀번호 일치 : db에서 제거
     } catch(DataIntegrityViolationException e) {
        throw new LoginException("주문 정보가 존재 합니다. 관리가 연락 요망.", "mypage?userid="+userid);
     } catch(Exception e) {
        e.printStackTrace();
        throw new LoginException("탈퇴시 오류 발생.", "delete?userid="+userid);
     }
     
     // 탈퇴 성공시 동작되는 코드
     if(loginUser.getUserid().equals("admin")) { // 관리자가 강제 탈퇴
        return "redirect:../admin/list";
     } else {
        session.invalidate();
        return "redirect:login";
     }
  }
  
  /*
   * UserLoginAspect loginCheck() : UserController.loginCheck*(..)인 메서드
   *                           마지막 매개변수가 httpSession인 메서드
   * 
   * 1. 로그인 검증 => AOP 클래스
   * 2. 파라미터값 저장
   * 3. 현재비밀번호와 입력된 비밀번호 검증
   *       불일치 : 오류 메세지 출력, password 페이지 이동
   * 4. 일치 : db수정
   * 5. 성공 : 로그인 정보 변경, mypage로 이동
   *      실패 : 오류 메세지 출력, password 페이지 이동
   * 
   */
  
  @PostMapping("password")
  public String loginCheckPasswordRtn(String password, String chgpass, HttpSession session) {
     User loginUser = (User)session.getAttribute("loginUser");
     
     if(!password.equals(loginUser.getPassword())) {
        throw new LoginException("비밀번호가 일치하지 않습니다.", "password");
     }
     
     try {
        service.userChgpass(loginUser.getUserid(),chgpass);
        loginUser.setPassword(chgpass); // 비밀번호 변경 후 값 저장
     } catch(Exception e) {
        throw new LoginException("변경시 오류 발생.", "password");
     }
     
     return "redirect:mypage?userid="+loginUser.getUserid();
  }
  
  @PostMapping("{url}search") // {url}search : *search 요청시 호출되는 메서드
  public ModelAndView search(User user, BindingResult bresult, @PathVariable String url) {
     // @Pathvariable : {url}의 이름을 매개변수로 전달.
     //      요청 : idsearch : url <= "id"
     //      요청 : pwsearch : url <= "pw"
     
     ModelAndView mav = new ModelAndView();
     String code = "error.userid.search";
     String title = "아이디";
     if(url.equals("pw")) { // 비밀번호 검증인 경우
        title = "비밀번호";
        code = "error.password.search";
        if(user.getUserid() == null || user.getUserid().trim().equals("")) {
           //BindingResult.reject() : global error => jsp의 <spring:hasBindErrors ... 부분에 오류 출력
           //BindingResult.rejectValue() : global error => jsp의 <form:error path=... 부분에 오류 출력
           bresult.rejectValue("userid", "error.required");
        }
     }
     if(user.getEmail() == null || user.getEmail().trim().equals("")) {
        bresult.rejectValue("email", "error.required");
     }
     if(user.getPhoneno() == null || user.getPhoneno().trim().equals("")) {
        bresult.rejectValue("phoneno", "error.required");
     }
     if(bresult.hasErrors()) {
        mav.getModel().putAll(bresult.getModel());
        return mav;
     }
     // 입력값 == db데이터 입력 검증
     if(user.getUserid() != null && user.getUserid().trim().equals(""))
        user.setUserid(null);
     
     String result = service.getSearch(user); // mybatis 구현시 해당 레코드가 없는 경우 결과값이 null임.
     
     if(result == null) {
        bresult.reject(code);
        mav.getModel().putAll(bresult.getModel());
        return mav;
     }
     
     mav.addObject("result",result);
     mav.addObject("title",title);
     mav.setViewName("search");
     return mav;
  }
  
  


} // class