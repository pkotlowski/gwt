package shopping.shared;

import java.util.ArrayList;
import java.util.List;

public class StorageService {

	public List<Lists> db = new ArrayList<Lists>();

	public void addList(Lists sl) {
		db.add(sl);
		// System.out.println("Dodano "+ sl.id+" "+sl.name+" "+sl.creationDate);
	}

	public List<Lists> getAllLists() {
		//System.out.println("XXX " + db.size());
		return db;
	}
	public List<Item> getAllItems(){
		return db.get(0).itemList;
	}

	public void modifyList(Lists lists) {
		Lists tmpList = lists;
		db.remove(lists);
		tmpList.itemList.add(null);
	}
	
	public List<Lists> removeList(Lists ls){
		System.out.println(db.size());
		db.remove(ls);
		System.out.println(db.size());
		return db;
	}
	
	

}
