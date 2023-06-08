package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class UserLoginAspect {
	@Before("execution(* controller.User*.idCheck*(..)) && args(userid,session)")
	// execution : 접근제한자 상관없음 / controller.Cart* : Cart로 시작하는 컨트롤러의 check로 시작하는 모든 메서드
	// && args(..,session) : .. => 시작은 상관없고, 끝이 session으로 끝나는 메서드
	public void idCheck(String userid, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser"); // session에서 로그인 정보를 가져옴.
		if(loginUser == null) {
			throw new LoginException("로그인이 필요합니다.", "../user/login");
		}
		
		if (!loginUser.getUserid().equals("admin") && !loginUser.getUserid().equals(userid)) {
			throw new LoginException("본인만 이용 가능합니다.", "../item/list");
		}
	}
	
	@Around("execution(* controller.User*.loginCheck*(..)) && args(..,session)")
	public Object loginCheck(ProceedingJoinPoint joinPoint, HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("[loginCheck] 로그인이 필요합니다.", "login");
		}
		return joinPoint.proceed();
	}
} // class
