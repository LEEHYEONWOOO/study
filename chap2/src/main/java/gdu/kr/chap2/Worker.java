package gdu.kr.chap2;

import org.springframework.stereotype.Component;

@Component   //worker 이름으로 컨테이너에 저장
public class Worker {
	public void work(WorkUnit unit) {
		System.out.println(this + ":work:"+unit);
	}
}
