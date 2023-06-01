package logic;

public class ItemSet {
	private Item item;
	private Integer quantity;
	
	//생성자
	public ItemSet(Item item, Integer quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}
	
	//Getter, Setter
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	//toString
	@Override
	public String toString() {
		return "ItemSet [item=" + item + ", quantity=" + quantity + "]";
	}
	
	
	
	
	
} // class
