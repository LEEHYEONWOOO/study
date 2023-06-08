package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

@Controller
@RequestMapping("weather")
public class WeatherController {
   
   
    @GetMapping("*")  //설정되지 않은 모든 요청시 호출되는 메서드
    public ModelAndView join() {
       ModelAndView mav = new ModelAndView();
       mav.addObject(new User());
       return mav;
    }
    @ResponseBody
    @PostMapping("weather2")
    public List<Map<String, Object>> test(@RequestBody List<Map<String, Object>> param) {
        System.out.println("param : "+ param);
        System.out.println(param.get(0));
        System.out.println(param.get(0).get("loc"));
        System.out.println(param.get(0).get("date"));
    	
		return param;
	}
    
    
}