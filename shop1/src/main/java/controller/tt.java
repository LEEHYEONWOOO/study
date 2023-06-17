package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class tt {
	
	
	@RequestMapping(value="select", produces="text/plain; charset=utf8")
	public String select2(String si2, String gu2, HttpServletRequest request) throws Exception{
		System.out.println("select 호출됨");
		 StringBuilder urlBuilder = new StringBuilder("https://bigdata.kepco.co.kr/openapi/v1/commonCode.do");
	     urlBuilder.append("?" + URLEncoder.encode("codeTy","UTF-8") + "=" + URLEncoder.encode("cityCd", "UTF-8"));
	     urlBuilder.append("&" + URLEncoder.encode("apiKey","UTF-8") + "=XmusbW46HG076pa219D4Y2X59Wpy76w1tcA70U79");
	     urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
	     
	     URL url = new URL(urlBuilder.toString());
	     System.out.println(url);
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
	        //System.out.println(sb.toString());
	        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
	        JSONParser parser = new JSONParser();
	        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장.    
	        JSONObject obj = (JSONObject)parser.parse(sb.toString());
	         
	        JSONObject jsonObj = (JSONObject) obj;
	        JSONArray data = (JSONArray) jsonObj.get("data");
	        
	        Set<String> set = new LinkedHashSet<>();
		if(si2==null && gu2==null) {
			for(int i=0; i<data.size(); i++ ) {
				set.add(data.get(i).toString().substring(25,data.get(i).toString().indexOf("codeNm")-3).trim());
				
			}
			System.out.println("data(0) : "+data.get(0));
		}
		List<String> list = new ArrayList<>(set); //set 객체 => List 객체
		System.out.println("list.tostring   :   " + list.toString());
		return list.toString();	//리스트 객체가 브라우저에 전달. 뷰가 아님
						//pom.xml의 fasterxml.jackson...의 설정에 의해서 브라우저는 배열로 인식함
	}
}
