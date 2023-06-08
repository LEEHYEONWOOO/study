package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import logic.ShopService;
import logic.User;

@RestController
@RequestMapping("api")
public class ApiController {
   
   
	@RequestMapping("weatherApi")
    public @ResponseBody JSONArray test2() throws IOException, ParseException {

	   
       System.out.println("weatherApi호출됨");
       Date date1 = new Date();
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd"); 
       String date = simpleDateFormat.format(date1);       
       /*
       if(!param.get(0).get("date").toString().equals("")||param.get(0).get("date").toString()!=null) {
    	   date = param.get(0).get("date").toString();
       }
       
       System.out.println(date+" 이건 지금 시각?");
       */
      StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=cIU8HoBdDJx9IAv4NEQ88GvIfz3eoVBo1LHbEfxRtMKcNlK7xaWgZQexbnedoiqNWqPVRcLQ4JeBb8YhhBW6Cw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20230607", "UTF-8")); /*‘21년 6월 28일발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0500", "UTF-8")); /*05시 발표*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        //System.out.println("Response code: " + conn.getResponseCode());
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
        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
        // 4. model에 담아준다.
        //mav.addObject("sb",sb.toString()); //이건 String Type
        /*
        String strJson = sb.toString();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(strJson);
         */
        JSONObject jsonObj = (JSONObject) obj;
        
        JSONObject response = (JSONObject) jsonObj.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
       
        //System.out.println("obj : "+obj);
        System.out.println("item out : \n"+item);
      return item;
   }
    
	
	
		@RequestMapping("placeApi")
		public @ResponseBody JSONArray test3(String si2, String gu2, HttpServletRequest request) throws IOException, ParseException {
		 System.out.println("placeApi call");
		 System.out.println("si2 = "+si2+" gu2 = "+gu2);
	   StringBuilder urlBuilder = new StringBuilder("https://bigdata.kepco.co.kr/openapi/v1/EVcharge.do");
	       urlBuilder.append("?" + URLEncoder.encode("metroCd","UTF-8") + "=" + URLEncoder.encode(si2, "UTF-8"));
	       urlBuilder.append("&" + URLEncoder.encode("cityCd","UTF-8") + "=" + URLEncoder.encode(gu2, "UTF-8"));
	     urlBuilder.append("&" + URLEncoder.encode("apiKey","UTF-8") + "=XmusbW46HG076pa219D4Y2X59Wpy76w1tcA70U79");
	     urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
	     
	     URL url = new URL(urlBuilder.toString());
	     System.out.println(url);
	     HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	     conn.setRequestMethod("GET");
	     conn.setRequestProperty("Content-type", "application/json");
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
	        JSONParser parser = new JSONParser();
	        JSONObject obj = (JSONObject)parser.parse(sb.toString());
	        
	        JSONObject jsonObj = (JSONObject) obj;
	        JSONArray item = (JSONArray) jsonObj.get("data");
	        
	        System.out.println("item : "+item);
	      return item;
	}
	/*
	@RequestMapping("cityCodeApi")
    public @ResponseBody JSONArray test4() throws IOException, ParseException {
		 System.out.println("cityCodeApi호출됨");
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
	        
	        System.out.println("data.get(0) : "+data.get(0));
	        System.out.println("data의 길이" + data.size());
	      return null;
	}
	*/
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
	
	@RequestMapping("selectText")
	public List<String> select(String si2, String gu2, HttpServletRequest request) throws Exception{
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
		}else if(si2!=null && gu2==null) {
			si2 = si2.trim();
			System.out.println("si2 value : "+si2);
			for(int i=0; i<data.size(); i++ ) {
				if(si2.equals(data.get(i).toString().substring(25,data.get(i).toString().indexOf("codeNm")-3).trim())) {
					set.add(data.get(i).toString().substring(data.get(i).toString().indexOf("codeNm")+9,data.get(i).toString().indexOf("codeTy")-3).trim());
//					System.out.println("gu num : "+data.get(i).toString().substring(9,11));
	//				System.out.println("si num : "+data.get(i).toString().substring(data.get(i).toString().indexOf("\"uppoCd\"")+10,data.get(i).toString().indexOf("\"uppoCd\"")+12));
					//String siNum = data.get(i).toString().substring(25,data.get(i).toString().indexOf("codeNm")-3).trim();
					//String guNum = data.get(i).toString().substring(9,11)
				}
			}
		}else if(si2!=null && gu2!=null && !si2.equals("") && !gu2.equals("")) {
			System.out.println("si2 and gu2 is not null");
			for(int i=0; i<data.size(); i++ ) {
				if(si2.equals(data.get(i).toString().substring(25,data.get(i).toString().indexOf("codeNm")-3).trim()) && 
						gu2.equals(data.get(i).toString().substring(data.get(i).toString().indexOf("codeNm")+9,data.get(i).toString().indexOf("codeTy")-3).trim())) {
					
					String siNum = data.get(i).toString().substring(data.get(i).toString().indexOf("\"uppoCd\"")+10,
							data.get(i).toString().indexOf("\"uppoCd\"")+12);
					
					String guNum = data.get(i).toString().substring(9,11);
					System.out.println(data.get(i));
					set.add(siNum);
					set.add(guNum);
				}
			}
		}
		
		//System.out.println("1 : "+data.get(0).toString().substring(25,data.get(0).toString().indexOf("codeNm")-3).trim());
		//System.out.println("2 : "+data.get(0));
		List<String> list = new ArrayList<>(set); //set 객체 => List 객체
		System.out.println("selectText_list out : "+list);
		return list;	//리스트 객체가 브라우저에 전달. 뷰가 아님
		//pom.xml의 fasterxml.jackson...의 설정에 의해서 브라우저는 배열로 인식함
		}
		
	}
	
	
    
