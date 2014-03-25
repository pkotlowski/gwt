package shopping.client;

import java.util.Date;

import shopping.shared.Item;
import shopping.shared.Lists;
import shopping.shared.StorageService;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.ListDataProvider;

public class ShoppingList implements EntryPoint {

	public void onModuleLoad() {
		final StorageService ss = new StorageService();
		final Lists lst = new Lists();

		final Button addItemToListButton = new Button("Send");
		final TextBox nameField = new TextBox();
		final TextBox quantity = new TextBox();
		final Button newList = new Button("Nowa lista zakupów");
		final Button viewAllLists = new Button("Przeglądaj listy zakupów");
		final TextBox newListName = new TextBox();
		final Button persistNewList = new Button("Dodaj");
		final CellTable<Lists> table = new CellTable<Lists>();
		final CellTable<Item> items = new CellTable<Item>();

		final TextBox listId = new TextBox();
		final Label productTableHeader = new Label();
		listId.setVisible(false);
		table.setTitle("Historia list zakupów");
		
		nameField.setText("Nazwa produktu");
		quantity.setText("Ilość");
		addItemToListButton.addStyleName("sendButton");
		newList.addStyleName("sendButton");

		final FlowPanel addList = new FlowPanel();
		final FlowPanel addToList = new FlowPanel();
		final FlowPanel menuBar = new FlowPanel();

		addList.add(newListName);
		addList.add(persistNewList);

		addToList.add(productTableHeader);
		addToList.add(quantity);
		addToList.add(nameField);
		addToList.add(addItemToListButton);
		addToList.add(listId);

		menuBar.add(viewAllLists);
		menuBar.add(newList);

		final SplitLayoutPanel p = new SplitLayoutPanel();
		final FlowPanel center = new FlowPanel();

		p.addNorth(menuBar, 100);
		p.add(center);

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(p);
		
		final PopupPanel popup = new PopupPanel(true);
	    popup.setStyleName("demo-PopUpPanel");
	    //PopUpPanelContents = new VerticalPanel();
	    popup.add(new Label("dodano"));
	    popup.center();

		TextColumn<Lists> nameColumn = new TextColumn<Lists>() {
			@Override
			public String getValue(Lists lists) {
				return lists.name;
			}
		};
		TextColumn<Lists> creationDateColumn = new TextColumn<Lists>() {
			@Override
			public String getValue(Lists lists) {
				return lists.creationDate;
			}
		};
		TextColumn<Lists> idColumn = new TextColumn<Lists>() {
			@Override
			public String getValue(Lists lists) {
				return lists.id.toString();
			}
		};
		ButtonCell addToListButton = new ButtonCell();
		Column<Lists, String> preview = new Column<Lists, String>(
				addToListButton) {
			public String getValue(Lists object) {
				return "Dodaj do listy";
			}
		};
		preview.setFieldUpdater(new FieldUpdater<Lists, String>() {
			@Override
			public void update(int index, Lists object, String value) {
				center.remove(addToList);
				listId.setText(object.id.toString());
				productTableHeader.setText("Dodawanie do listy numer "
						+ listId.getText());
				center.add(addToList);
			}
		});

		ButtonCell showDetailedList = new ButtonCell();
		Column<Lists, String> dl = new Column<Lists, String>(showDetailedList) {
			public String getValue(Lists object) {
				return "Pokaż listę";
			}
		};

		dl.setFieldUpdater(new FieldUpdater<Lists, String>() {
			@Override
			public void update(int index, Lists object, String value) {
				ListDataProvider<Item> dataProvider = new ListDataProvider<Item>();
				dataProvider.addDataDisplay(items);

				listId.setText(object.id.toString());
				items.setRowData(0,
						lst.getAllItemsList(Long.valueOf((listId.getText()))));

				center.add(items);
			}
		});
		
		PushButton removeFromListButton = new PushButton("indeks.jpeg");
		Column<Lists, String> remove = new Column<Lists, String>(
				addToListButton) {
			public String getValue(Lists object) {
				return "Usuń";
			}
		};
		remove.setFieldUpdater(new FieldUpdater<Lists, String>() {
			@Override
			public void update(int index, Lists object, String value) {
				//center.clear();
				ss.removeList(object);
				//center.remove(table);
				//center.add(table);
				table.redraw();
				
			}
		});

		table.addColumn(idColumn, "ID");
		table.addColumn(nameColumn, "Name");
		table.addColumn(creationDateColumn, "Data utworzenia");
		table.addColumn(preview);
		table.addColumn(dl);
		table.addColumn(remove);

		TextColumn<Item> itemName = new TextColumn<Item>() {
			@Override
			public String getValue(Item item) {
				return item.name;
			}
		};
		TextColumn<Item> quantityColumn = new TextColumn<Item>() {
			@Override
			public String getValue(Item item) {
				return item.quantity.toString();
			}
		};

		final CheckboxCell cbCell = new CheckboxCell();
		Column<Item, Boolean> cbColumn = new Column<Item, Boolean>(cbCell) {
			@Override
			public Boolean getValue(Item object) {
				return object.isBought;
			}
		};
		cbColumn.setFieldUpdater(new FieldUpdater<Item, Boolean>() {
			@Override
			public void update(int index, Item object, Boolean value) {
				lst.productBought(object.id, value);
				center.add(items);
			}

		});

		items.addColumn(itemName, "nazwa");
		items.addColumn(quantityColumn, "ilość");
		items.addColumn(cbColumn, "Kupione");
		newList.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				center.clear();
				center.add(addList);
			}
		});
		persistNewList.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Lists sl = new Lists();
				Date date = new Date();
				sl.id = ss.getAllLists().size() + 1L;
				sl.creationDate = date.toString();
				sl.name = newListName.getText();
				ss.addList(sl);
			}
		});
		
		viewAllLists.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				center.clear();
				ListDataProvider<Lists> dataProvider = new ListDataProvider<Lists>();
				dataProvider.addDataDisplay(table);
				table.setRowData(0, ss.getAllLists());
				SimplePager simplePager = new SimplePager(TextLocation.CENTER);
				simplePager.setDisplay(table);
				simplePager.setPageSize(10);
				center.add(table);
				center.add(simplePager);
			}
		});
		/* Dodawanie produktu */
		addItemToListButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Item item = new Item();
				String name = nameField.getText();
				Double quan = Double.parseDouble(quantity.getText());
				item.id = lst.getAllItems().size() + 1L;
				item.name = name;
				item.quantity = quan;
				item.listId = Long.parseLong(listId.getText());
				item.isBought = false;
				lst.addItemToList(item);
				center.remove(addToList);
				popup.show();
			}
		});
		
		
		
		/* Akcje po kliknieciu na textboxy do wprowadzania produktu */
		nameField.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				nameField.setText("");
			}
		});
		quantity.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				quantity.setText("");
			}
		});
	}
}
