package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.User;

@Controller
@RequestMapping("place")
public class PlaceController {
   
   
    @GetMapping("location")  //설정되지 않은 모든 요청시 호출되는 메서드
    public ModelAndView join() {
       ModelAndView mav = new ModelAndView();
       mav.addObject(new User());
       return mav;
    }
    
    
}