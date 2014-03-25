package shopping.shared;

import java.util.ArrayList;
import java.util.List;
import shopping.shared.Item;

public class Lists {
	public Long id;
	public String name;
	public String creationDate;
	public List<Item> itemList = new ArrayList<Item>();
	
	public List getAllItems(){
		return itemList;
	}
	public void addItemToList(Item item){
		itemList.add(item);
		System.out.println(item.id+" "+item.listId);
	}
	
	public List<Item> getAllItemsList() {
		return itemList;
	}
	public List getItemsFromList(Long listId){
		List<Item> result = new ArrayList<Item>();
		for (Item x : itemList) {
			if(x.listId == listId){
				result.add(x);
			}
		}
		return result;
	}
	
	/*Getters and setters*/
	
	public Long getId() {
		return id;
	}

	
	public List<Item> getItemList() {
		return itemList;
	}
	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
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
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}


}
