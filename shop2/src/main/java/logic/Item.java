package logic;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Item {
	private int id;
	
	@NotEmpty(message="상품명을 입력하세요.")
	private String name;
	
	@Min(value=10, message="10원 이상부터 가능 합니다.")
	@Max(value=100000, message="100,000원 이하 까지 가능 합니다.")
	private int price;
	
	@NotEmpty(message="상품 설명을 입력하세요.")
	private String description;
	
	private String pictureUrl;
	private MultipartFile picture; // picture file에서 업로드된 파일의 내용
	
	
	
	

}
