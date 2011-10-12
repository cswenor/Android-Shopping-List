package bu.homework.shoppinglist;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.commonsware.cwac.tlv.TouchListView;

public class ItemListActivity extends ListActivity {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
	
	private ItemsModel mDbHelper;
	
	private SimpleCursorAdapter notes;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	     MyDropListener onDrop = new MyDropListener();
	     
	     setContentView(R.layout.item_list);
	     mDbHelper = new ItemsModel(this);
         mDbHelper.open();
         fillList();
         TouchListView itemListView = (TouchListView) getListView();
         itemListView.setDropListener(onDrop);
         registerForContextMenu(itemListView);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return result;
    }
	
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createItem();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case INSERT_ID:
        	createItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteItem(info.id);
                fillList();
                return true;
        }
        return super.onContextItemSelected(item);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ItemEditActivity.class);
        i.putExtra(ItemsModel.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillList();
    }
	
	private class MyDropListener implements TouchListView.DropListener {
		public void drop(int from, int to) {
			mDbHelper.adjustPriority(from, to);
			fillList();
			
		}
	}
	
    private void createItem() {
    	Intent i = new Intent(this, ItemEditActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    private void fillList() {
        // Get all of the notes from the database and create the item list
        Cursor itemCursor = mDbHelper.fetchAllItems();
        startManagingCursor(itemCursor);

        // Create an array to specify the fields we want to display in the list (only NAME)
        String[] from = new String[] { ItemsModel.KEY_NAME };
        
        // and an array of the fields we want to bind those fields to (in this case just item_row)
        int[] to = new int[] { R.id.item_row };
        
        // Now create an array adapter and set it to display using our row
        notes = new SimpleCursorAdapter(this, R.layout.item_row, itemCursor, from, to);
        setListAdapter(notes);
    }
	
}
