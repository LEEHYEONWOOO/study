package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("chat")
public class ChatController {

	@RequestMapping("*")
	public String chat() {
		
		return null;
	}
}
