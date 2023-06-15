package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;

import logic.Exchange;
import logic.ShopService;

public class CountScheduler {
	ShopService service = new ShopService();
	private int cnt;
	/*
	 * cron
	 * 	1. 특성시간, 주기적으로 프로그램을 수행하는 프로세스. 유닉스기반의 프로세스.
	 * 	2. 리눅스에서 crontab 명령 설정 가능함.
	 * 	3. 스프링에서는 cron기능을 Scheduler라고 한다.
	 * 	
	 *  크론식 : "0/5 * * * * ?" cron을 설정할수 있는 형식
	 *  
	 *  형식 : 초 분 시 일 월 요일 [년도]
	 *  초 : 0~59
	 *  분 : 0~59
	 *  시 : 0~23
	 *  일 : 1~31
	 *  월 : 1~12 (JAN~DEC)
	 *  요일 : 1~7 (MON~SUN)
	 *  
	 *  표현 방식
	 *  	* : 매번
	 *  	A/B : 주기 A에서B마다 1번씩 실행
	 *  	? : 설정 없음.(일, 요일에서 사용됨)
	 *  
	 *  크론식 예시
	 *  	0/10 * * * * ? : 10초마다 한번씩
	 *  	0 0/1 * * * ? : 1분마다 한번씩
	 *  	0 20,50 * * * ? : 매시간 20,50분 마다 실행
	 *  	0 0 0/3 * * ? : 3시간 마다 실행
	 *  	0 0 12 ? * 1  :  월요일 12시마다 실행.
	 *  	0 0 12 ? * MON : 월요일 12시마다 실행.
	 *  	0 0 10 ? * SAT,SUN : 주말 10시마다 실행.
	 *  
	 *  크론식 작성 사이트 : www.cronmaker.com
	 *  	
	 */	
	@Scheduled(cron="0/5 * * * * ?")
	public void execute1() {
		System.out.println("cnt : " + cnt++);
	}
	
	@Scheduled(cron="0 10 15 14 6 ?")
	public void execute2() {
		System.out.println("3시 10분입니다.");
	}
	
	/*
	 * 1. 평일 아침 10시에 환율 정보를 조회해서 db에 등록
	 * 2. exchange 테이블 생성하기
	 * 		create table exchange(
	 * 			enum int primary key,
	 * 			code varchar(10),	#통화코드
	 * 			name varchar(50),	#통화명
	 * 			primeamt float,	#매매기준율
	 * 			sellamt float,	#매도율
	 * 			buyamt float,		#매입율
	 * 			edate  varchar(10)	#환율기준일
	 * 		)
	 */
	@Scheduled(cron="0 18 10 * * ?")
	public void execute3() {
		System.out.println("평일 아침 10시에 환율 정보 조회 후 db 등록.");
		Document doc = null;
		List<List<String>> trlist = new ArrayList<>();
		String url = "https://koreaexim.go.kr/wg/HPHKWG057M01";
		String exdate = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements trs = doc.select("tr");
			exdate = doc.select("p.table-unit").html();
			exdate = exdate.substring(exdate.indexOf(":")+2);
			for(Element tr : trs) {
				List<String> tdlist = new ArrayList<>();
				Elements tds = tr.select("td");
				for(Element td : tds) {
					tdlist.add(td.html());
				}
				if(tdlist.size()>0) {
					trlist.add(tdlist);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(List<String> tds:trlist) {
			Exchange ex = new Exchange(0,tds.get(0),tds.get(1),
					Float.parseFloat(tds.get(4).replace(",","")),
					Float.parseFloat(tds.get(2).replace(",","")),
					Float.parseFloat(tds.get(3).replace(",","")),exdate.trim());
			service.exchangeInsert(ex);
		}
		System.out.println(exdate + "황율등록완료");
		
	}
	
}
