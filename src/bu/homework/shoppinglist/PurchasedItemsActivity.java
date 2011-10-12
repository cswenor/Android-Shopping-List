package bu.homework.shoppinglist;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

public class PurchasedItemsActivity extends Activity {
	private ExpandableListAdapter adapter;
	private ItemsModel mDbHelper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.purchased);
	    
	    mDbHelper = new ItemsModel(this);
        mDbHelper.open();
        
	    ExpandableListView listView = (ExpandableListView) findViewById(R.id.purchasedListView);
	    listView.setOnChildClickListener(new OnChildClickListener()
        {
            
            public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2, int arg3, long arg4)
            {
                Toast.makeText(getBaseContext(), "Child clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        
        listView.setOnGroupClickListener(new OnGroupClickListener()
        {
            
            public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2, long arg3)
            {
                Toast.makeText(getBaseContext(), "Group clicked", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Initialize the adapter with blank groups and children
        // We will be adding children on a thread, and then update the ListView
        adapter = new ExpandableListAdapter(this, new ArrayList<String>(), new ArrayList<ArrayList<ShoppingItem>>());

        // Set this blank adapter to the list view
        listView.setAdapter(adapter);
        // fillList();

	}
	@Override
	protected void onResume() {
		super.onResume();
		fillList();
	};
	private void fillList(){
		
		ShoppingItem shoppingItem; // Reusable Shopping Item
		adapter.clear();
		MyWallet appState = ((MyWallet)getApplicationContext());
	    String state = appState.getState();

		double moneyLeft = 67.00;
	 
	    // Grabs the correct currency symbol and cleans the string
		String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
		String cleanString = state.toString().replaceAll(replaceable,"");
		
		// Converts the string and converts it to currency format
		moneyLeft = Double.parseDouble(cleanString) / 100;
		
		Cursor itemCursor = mDbHelper.fetchAllItems();
	    itemCursor.moveToFirst();
	    
		while (itemCursor.isAfterLast() == false) {
			shoppingItem = new ShoppingItem(itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsModel.KEY_NAME)));
			shoppingItem.setPrice(itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsModel.KEY_PRICE)));
			if(moneyLeft >= Double.parseDouble(shoppingItem.getPrice())) {
				adapter.addItem(shoppingItem, "Bought");
				moneyLeft -= Double.parseDouble(shoppingItem.getPrice());
			} else {
				adapter.addItem(shoppingItem, "Still Need");
			}
			
       	    itemCursor.moveToNext();
        }
	}


}
