package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/*
 * @Controller : @Component + Controller 기능
 * 		호출되는 메서드 리턴 타입 : ModelAndView : view 이름 + 데이터
 * 							   String : view 이름
 * 
 * @RestController : @Component + Controller 기능 + 클라이언트에 데이터를 직접 전달
 * 		호출되는 메서드 리턴 타입 : String : 클라이언트에 전달되는 문자열 값.
 * 							   Object : 클라이언트에 전달되는 값. (JSON 형태)
 * 
 * 		Spring 4.0 이후에 추가됨
 * 		이전 버전에서는 @ResponseBody 기능으로 설정
 * 		@ResponseBody 어노테이션은 메서드에서 설정함.
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
	
	@RequestMapping("select")
	public List<String> select(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Set : 중복불가
		// LinkedHashSet : 순서유지, 중복불가, List아님(첨자 사용 불가)
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) { // fr.readLine() : 1줄씩
					String[] arr = data.split("\\s+");
					// \\s+ : \\s(공백) +(1개 이상) 
					if(arr.length >= 2) {
						set.add(arr[0].trim()); // 중복제거됨.
					} // if
				} // while
			} catch(IOException e) {
				e.printStackTrace();
			}
		}  else if (gu == null) { //si파라미터값은 null이 아님
			si = si.trim();
			try {
				while((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >=3 && arr[0].equals(si) &&
							!arr[1].contains(arr[0])) {
						set.add(arr[1].trim()); //구군 정보 설정
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			si = si.trim();
			gu = gu.trim();
			try {
				while((data = fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >=3 && arr[0].equals(si) &&
						arr[1].equals(gu) && !arr[0].equals(arr[1]) && !arr[2].contains(arr[1])) {
						if(arr.length > 3) {
							if(arr[3].contains(arr[1])) continue;
							arr[2] += " " + arr[3];
						}								
						set.add(arr[2].trim()); //동리 정보 설정
					}
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		List<String> list = new ArrayList<>(set);
		// Set 객체 => List 객체로 변경
		return list;
		// 리스트 객체가 브라우저에 전달, view 아님
		// pom.xml의 fasterxml.jackson.. 의 설정에 의해서 브라우저는 배열로 인식
	}
	
	//@RequestMapping("select2") // 클라이언트로 문자열 전송, 한글 깨짐 방지를 위해encoding 필요함
	/*
	 * produces : 클라이언트에 전달되는 데이터 특징을 설정
	 * 	  text/plain : 데이터 특징, 순수문자 
	 * 	  text/html  : HTML 형식의 문자 
	 * 	  text/xml   : XML 형식의 문자 
	 */
	
	
	@RequestMapping(value="select2",produces = "text/plain; charset=utf-8")
	public String select2(String si, String gu, HttpServletRequest request) {
		BufferedReader fr = null;
		String path = request.getServletContext().getRealPath("/")+"file/sido.txt";
		
		try {
			fr = new BufferedReader(new FileReader(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Set<String> set = new LinkedHashSet<>();
		String data = null;
		
		if(si==null && gu==null) {
			try {
				while((data=fr.readLine()) != null) {
					String[] arr = data.split("\\s+");
					if(arr.length >= 3) {
						set.add(arr[0].trim()); // 중복제거됨.
					} // if
				} // while
			} catch(IOException e) {
				e.printStackTrace();
			}
		} // if
		List<String> list = new ArrayList<>(set);
		return list.toString();
	}
	
	@RequestMapping(value="exchange", produces = "text/html; charset=utf-8") // SiteMesh 설정 필요
	public String exchange() {
		Document doc = null; // org.jsoup.nodes
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr"); // tr태그들, org.jsoup.nodes
			exdate = doc.select("p.table-unit").html(); // 조회기준일 : 2023.06.02
			// p.table-unit : class속성의 값이 table-unit인 p태그
			
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>(); // tr 태그들의 td 태그 목록
				Elements tds = tr.select("td"); // tr 태그의 하위 td 태그들
				for(Element td : tds) {
					tdlist.add(td.html());
				}
				if (tdlist.size() > 0) {
					if (tdlist.get(0).equals("USD")
					|| tdlist.get(0).equals("CNH")
					|| tdlist.get(0).equals("JPY(100)")
					|| tdlist.get(0).equals("EUR")) {
						trlist.add(tdlist);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<h4 class='w3-center'>수출입은행<br>" + exdate + "</h4>");
		sb.append("<table class='w3-table-all'>");
		sb.append("<tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
		for(List<String> tds : trlist) {
			sb.append("<tr><td>"+tds.get(0)+"<br>"+tds.get(1)+"</td><td>"+tds.get(4)+"</td>");
			sb.append("<td>"+tds.get(2)+"</td><td>"+tds.get(3)+"</td></tr>");
		}
		sb.append("</table>");
		//System.out.println(sb);
		return sb.toString();
	}
}
