package aop;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.User;

@Component
@Aspect
public class CartAspect {
	@Before("execution(* controller.Cart*.check*(..)) && args(..,session)")
	// execution : 접근제한자 상관없음 / controller.Cart* : Cart로 시작하는 컨트롤러의 check로 시작하는 모든 메서드
	// && args(..,session) : .. => 시작은 상관없고, 끝이 session으로 끝나는 메서드
	public void cartCheck(HttpSession session) throws Throwable {
		User loginUser = (User)session.getAttribute("loginUser"); // session에서 로그인 정보를 가져옴.
		if(loginUser == null) {
			throw new LoginException("회원만 주문 가능합니다.", "../user/login");
		}
		
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0) {
			throw new CartEmptyException("장바구니에 상품이 없습니다.", "../item/list");
		}
	}

}
