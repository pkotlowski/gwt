package shopping.shared;

public class Item {
	public Long id;
	public Long listId;
	public String name;
	public Double quantity;
	public Boolean isBought;
	
	public Boolean getIsBought() {
		return isBought;
	}
	public void setIsBought(Boolean isBought) {
		this.isBought = isBought;
	}
	public Long getListId() {
		return listId;
	}
	public void setListId(Long listId) {
		this.listId = listId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getQuantity() {
		return quantity;
	}
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
}
