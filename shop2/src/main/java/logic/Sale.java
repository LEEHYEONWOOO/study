package logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Setter
@Getter
@ToString
public class Sale {
	private int saleid;
	private String userid;
	private Date saledate;
	private User user;
	private List<SaleItem> itemList = new ArrayList<>();
	
	public int getTotal() { // 주문상품 전체체 금액 리턴
		int sum = 0;
		/*
		for(SaleItem s : itemList) {
		 sum += s.getItem().getPrice() * s.getQuantity();
		}
		return sum;
		*/
		
		return itemList.stream().mapToInt(s->s.getItem().getPrice() * s.getQuantity()).sum();
	}

	
	
	

}
