package shopping.client;

import java.text.DateFormat;
import java.util.Date;

import shopping.shared.FieldVerifier;
import shopping.shared.Lists;
import shopping.shared.StorageService;
import sun.io.Converters;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
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
import com.ibm.icu.text.SimpleDateFormat;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ShoppingList implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final StorageService ss = new StorageService();
		
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		final TextBox quantity = new TextBox();
		final Button newList = new Button("Nowa lista zakupów");
		final Button viewAllLists = new Button("Przeglądaj listy zakupów");
		final TextBox newListName = new TextBox();
		final Button persistNewList = new Button("Dodaj");
		final CellTable<Lists> table = new CellTable<Lists>();

		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		newList.addStyleName("sendButton");

		final FlowPanel addList = new FlowPanel();
		final FlowPanel addToList = new FlowPanel();
		final FlowPanel menuBar = new FlowPanel();

		addList.add(newListName);
		addList.add(persistNewList);

		addToList.add(errorLabel);
		addToList.add(quantity);
		addToList.add(nameField);
		addToList.add(sendButton);

		menuBar.add(viewAllLists);
		menuBar.add(newList);

		final SplitLayoutPanel p = new SplitLayoutPanel();
		final FlowPanel center =  new FlowPanel();
		// p.addWest(addToList, 0);
		p.addNorth(menuBar, 100);
		p.add(center);
		// p.addSouth(addToList, 0);
		

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
		table.addColumn(idColumn, "ID");
		table.addColumn(nameColumn, "Name");
		table.addColumn(creationDateColumn, "Data utworzenia");
		
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
				sl.id = ss.getAllLists().size()+1L;
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
			    table.setTitle("Historia list zakupów");
			    table.setRowCount(ss.getAllLists().size(), true);
			    table.setVisibleRange(0, 20);
			    
			    SimplePager simplePager = new SimplePager(TextLocation.CENTER, true, 10, true);
			    //simplePager.setPageSize(5);
			    simplePager.setDisplay(table);
			    center.add(table);				

			}
		});
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				/*
				 * greetingService.greetServer(textToServer, new
				 * AsyncCallback<String>() { public void onFailure(Throwable
				 * caught) { // Show the RPC error message to the user dialogBox
				 * .setText("Remote Procedure Call - Failure");
				 * serverResponseLabel
				 * .addStyleName("serverResponseLabelError");
				 * serverResponseLabel.setHTML(SERVER_ERROR);
				 * dialogBox.center(); closeButton.setFocus(true); }
				 * 
				 * public void onSuccess(String result) {
				 * dialogBox.setText("Remote Procedure Call");
				 * serverResponseLabel
				 * .removeStyleName("serverResponseLabelError");
				 * serverResponseLabel.setHTML(result); dialogBox.center();
				 * closeButton.setFocus(true); } });
				 */
				greetingService.addToShoppingList(textToServer,
						Double.parseDouble(quantity.getText()),
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
