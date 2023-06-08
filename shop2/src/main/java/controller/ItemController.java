package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

//http://localhost:8080/shop1/item/list

@Controller // @Componet + Controller 기능을 함, Controller 기능 : 요청을 받음
@RequestMapping("item") // url에 item 요청
public class ItemController {
	@Autowired // ShopService 객체 주입.
	private ShopService service; // ShopService 객체를 service로 사용
	
	@RequestMapping("list") // list라는 메서드가 요청되면 아래 코드 실행
	public ModelAndView list() {
		// ModelAndView : Model + view
		//				  view에 전송 할 데이터 + view 설정 // 데이터는 list로, view @RequestMapping로 설정
		ModelAndView mav = new ModelAndView();
		List<Item> itemList = service.itemList(); // itemList : item 테이블에 있는 모든 정보를 Item 객체의 List로 저장
		mav.addObject("itemList",itemList);
		return mav;
	}
	
	// http://localhost:8080/shop1/item/detail?id=1
	@GetMapping({"detail","update","delete"}) // Get, Post방식 상관없이 호출.
	public ModelAndView detail(Integer id) {
		// id = id 파라미터의 값
		// 매개변수 이름과 같은 이름의 파라미터 값을 자동으로 저장함.
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	
	// http://localhost:8080/shop1/item/create
	@GetMapping("create") // Get방식 요청시 동작 // 기존 : @RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(new Item());
		return mav;
	}
	
	@PostMapping("create") // post 방식 요청시 동작 // 기존 : @RequestMapping("register")
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		// request : 요청 객체 주입.
		// item의 프로퍼티와 파라미터값을 비교하여 같은 이름의 값을 item 객체에 저장
		// @Valid : item 객체에 입력된 내용을 유효성 검사함.
		// @Valid, BindingResult는 같이 사용.
		ModelAndView mav = new ModelAndView("item/create");
		if(bresult.hasErrors()) { // @Valid 프로세스에 의해서 입력 데이터 오류가 있는 경우
			mav.getModel().putAll(bresult.getModel());
			return mav; // item 객체 정보 + 에러 메세지 
		}
		service.itemCreate(item, request); // db추가, 이미지 업로드
		mav.setViewName("redirect:list");
		return mav;
	}
	
	/*
	// http://localhost:8080/shop1/item/update
	@GetMapping("update") // Get방식 요청시 동작 // 기존 : @RequestMapping("create")
	public ModelAndView update(Integer id) {
		ModelAndView mav = new ModelAndView();
		Item item = service.getItem(id);
		mav.addObject("item",item);
		return mav;
	}
	*/
	
	/*
	 * 1. 입력값에 대한 유효성 검증
	 * 2. db의 내용을 수정. 파일 업로드
	 * 3. update 완료 시, list로 페이지 전환 
	 */
	
	@PostMapping("update")
	public ModelAndView update(@Valid Item item, BindingResult bresult, HttpServletRequest request) {
		// request : 요청 객체 주입.
		// item의 프로퍼티와 파라미터값을 비교하여 같은 이름의 값을 item 객체에 저장
		// @Valid : item 객체에 입력된 내용을 유효성 검사함.
		// @Valid, BindingResult는 같이 사용.
		ModelAndView mav = new ModelAndView();
		if(bresult.hasErrors()) { // @Valid 프로세스에 의해서 입력 데이터 오류가 있는 경우
			mav.getModel().putAll(bresult.getModel());
			return mav; // item 객체 정보 + 에러 메세지 
		}
		service.itemUpdate(item, request); // db추가, 이미지 업로드
		mav.setViewName("redirect:list");
		return mav;
	}
	
	@PostMapping("delete")
	public String delete(Integer id) {
		service.itemDelete(id); // db추가, 이미지 업로드
		return "redirect:list";
	}
	
}


