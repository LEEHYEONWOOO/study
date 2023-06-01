package exception;

// 예외처리 생략 가능 예외클래스
public class CartEmptyException extends RuntimeException {
	private String url;
	
	public CartEmptyException(String msg, String url) {
		super(msg);
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

}
