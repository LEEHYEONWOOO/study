package controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import exception.CartEmptyException;
import exception.LoginException;
import logic.Cart;
import logic.Item;
import logic.ItemSet;
import logic.Sale;
import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("cartAdd")
	public ModelAndView add(Integer id, Integer quantity, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Item item = service.getItem(id);
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null) {
			cart = new Cart();
			session.setAttribute("CART", cart);
		}
		
		cart.push(new ItemSet(item, quantity));
		mav.addObject("message", item.getName()+" : "+quantity+"개 장바구니 추가");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartDelete")
	public ModelAndView delete(int index, HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		ItemSet robj = cart.getItemSetList().remove(index);
		mav.addObject("message",robj.getItem().getName()+" 삭제 완료.");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("cartView")
	public ModelAndView view(HttpSession session) {
		ModelAndView mav = new ModelAndView("cart/cart");
		Cart cart = (Cart)session.getAttribute("CART");
		mav.addObject("cart",cart);
		return mav;
	}
	
	@RequestMapping("checkout")
	public String checkout(HttpSession session) {
		/*
		Cart cart = (Cart)session.getAttribute("CART");
		if(cart == null || cart.getItemSetList().size() == 0)  { // 장바구니에 상품이 없을 때
			throw new CartEmptyException("장바구니에 상품이 없습니다. ","../item/list");
		}
		
		User loginUser = (User)session.getAttribute("loginUser");
		if(loginUser == null) {
			throw new LoginException("로그인 하세요. ", "../user/login");
		}
		*/ // CartAspect.java 참고
		return null; //view의 이름 리턴, null인 경우 url과 같은 이름을 호출 => /WEB-INF/view/cart/checkout.jsp
	}
	
	/*
	 * 주문 확정
	 * 1. 로그인 상태 + 장바구니에 상품이 존재 => aop로 설정
	 * 2. 장바구니 상품을 saleitem 테이블에 저장, 주문정보(sale) 테이블에 저장
	 * 3. 장바구니 상품 제거 (saleitem)
	 * 4. end.jsp 에서 sale, saleitem 데이터를 조회
	 */
	
	@RequestMapping("end")
	public ModelAndView checkend(HttpSession session) {
		ModelAndView mav = new ModelAndView();
		Cart cart = (Cart)session.getAttribute("CART");
		User loginUser = (User)session.getAttribute("loginUser");
		Sale sale = service.checkend(loginUser, cart);
		session.removeAttribute("CART"); // 장바구니 제거
		mav.addObject("sale", sale);
		return mav;
	}
		
		
		
		
		
} // class
