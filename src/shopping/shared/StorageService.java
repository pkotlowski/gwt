package shopping.shared;

import java.util.ArrayList;
import java.util.List;

public class StorageService {

	public List<Lists> db = new ArrayList<Lists>();

	public void addList(Lists sl) {
		db.add(sl);
		// System.out.println("Dodano "+ sl.id+" "+sl.name+" "+sl.creationDate);
	}

	public List getAllLists() {
		//System.out.println("XXX " + db.size());
		return db;
	}

	public void modifyList(Lists lists) {
		Lists tmpList = lists;
		db.remove(lists);
		tmpList.itemList.add(null);
	}
	
	

}
