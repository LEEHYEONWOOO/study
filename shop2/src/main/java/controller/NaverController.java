package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("naver")
public class NaverController {
   
   @GetMapping("*")
   public String naver() {	//localhost:8080/naver/search 요청 => /WEB-INF/view/naver/search.jsp 뷰로 선택
      return null;	//뷰이름. null:url과 같은 이름의 뷰를 선택
   }
   
   @RequestMapping("naversearch")
   @ResponseBody //뷰없이 바로 데이터를 클라이언트로 전송
   public JSONObject naversearch(String data, int display, int start, String type) {
	   JSONObject jsonData=null;
	   
	   String clientId = "T7GG6SjlTasJbctBc4wu";
	   String clientSecret = "iIlmgfDqeA";
	   StringBuffer json = new StringBuffer();
	   try {
	   	System.out.println(data);
	   	data = URLEncoder.encode(data,"utf-8");
	   	String apiURL = "https://openapi.naver.com/v1/search/"+type+".json?query="
	   			+data+"&display="+display+"&start="+start;
	   	URL url = new URL(apiURL);
	   	System.out.println(url);
	   	HttpURLConnection con = (HttpURLConnection)url.openConnection();
	   	con.setRequestMethod("GET");
	   	con.setRequestProperty("X-Naver-Client-Id", clientId);
	   	con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	   	int responseCode = con.getResponseCode();
	   	BufferedReader br;
	   	if(responseCode == 200) {
	   		br = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
	   	} else {
	   		br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
	   	}
	   	String inputLine;
	   	while ((inputLine = br.readLine()) != null) {
	   		json.append(inputLine);
	   	}
	   	br.close();
	   } catch (Exception e) {
	   	System.out.println(e);
	   }
	   JSONParser parser = new JSONParser();
	   try {
		jsonData = (JSONObject)parser.parse(json.toString());
	} catch (ParseException e) {
		e.printStackTrace();
	}
	   System.out.println(json);
	   
	   return jsonData;
   }
   
   @RequestMapping("getimg1")
   @ResponseBody //뷰없이 바로 데이터를 클라이언트로 전송
   public String test1() {
	   return null;
   }
   @RequestMapping("getimg2")
   @ResponseBody //뷰없이 바로 데이터를 클라이언트로 전송
   public String test2() {
	   return null;
   }
   @RequestMapping("getimg3")
   @ResponseBody //뷰없이 바로 데이터를 클라이언트로 전송
   public String test3() {
	   return null;
   }

}