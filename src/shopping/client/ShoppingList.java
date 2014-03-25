package shopping.client;

import java.awt.List;
import java.text.DateFormat;
import java.util.Date;

import shopping.shared.FieldVerifier;
import shopping.shared.Item;
import shopping.shared.Lists;
import shopping.shared.StorageService;
import sun.io.Converters;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.ibm.icu.text.SimpleDateFormat;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShoppingList implements EntryPoint {
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	
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
		final Label addingProductLabel = new Label("Dodawanie produktu");
		final TextBox listId = new TextBox();
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

		addToList.add(addingProductLabel);
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
				System.out.println("SSSS "+lst.getAllItemsList().size());
				items.setRowData(0, lst.getAllItemsList());
				//lst.getItemsFromList(object.id);
				//System.out.println("bangla");
				center.add(items);
			}
		});

		table.addColumn(idColumn, "ID");
		table.addColumn(nameColumn, "Name");
		table.addColumn(creationDateColumn, "Data utworzenia");
		table.addColumn(preview);
		table.addColumn(dl);

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
		items.addColumn(itemName,"nazwa");
		items.addColumn(quantityColumn, "ilość");

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
				lst.addItemToList(item);
				//System.out.println(lst.getAllItems());
			}
		});
		/*Akcje po kliknieciu na textboxy do wprowadzania produktu*/
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
