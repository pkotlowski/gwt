package shopping.shared;

import java.util.ArrayList;
import java.util.List;
import shopping.shared.Item;

public class Lists {
	public Long id;
	public String name;
	public String creationDate;
	public List<Item> itemList = new ArrayList<Item>();

	public List<Item> getAllItems() {
		return itemList;
	}

	public void addItemToList(Item item) {
		itemList.add(item);
	}

	public List<Item> getAllItemsList(Long id) {
		List<Item> itemsForList = new ArrayList<Item>();
		for (Item x : itemList) {
			if (x.listId == id) {
				itemsForList.add(x);
			}
		}
		return itemsForList;
	}

	public List<Item> productBought(Long id, Boolean value) {
		for (Item x : itemList) {
			if (x.id == id) {
				if (value)
					x.isBought = true;

				else
					x.isBought = false;
			}
		}
		return itemList;
	}

	/* Getters and setters */

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
