package bu.homework.shoppinglist;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ItemEditActivity extends Activity {

	 private EditText mNameText;
	    private EditText mPriceText;
	    private TextView mPriorityText;
	    private Long mRowId;
	    private ItemsModel mDbHelper;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mDbHelper = new ItemsModel(this);
	        mDbHelper.open();

	        setContentView(R.layout.item_edit);
	        setTitle(R.string.edit_item);

	        mNameText = (EditText) findViewById(R.id.name);
	        mPriceText = (EditText) findViewById(R.id.price);
	        mPriorityText = (TextView) findViewById(R.id.priority);

	        Button confirmButton = (Button) findViewById(R.id.confirm);

	        mRowId = (savedInstanceState == null) ? null :
	            (Long) savedInstanceState.getSerializable(ItemsModel.KEY_ROWID);
			if (mRowId == null) {
				Bundle extras = getIntent().getExtras();
				mRowId = extras != null ? extras.getLong(ItemsModel.KEY_ROWID)
										: null;
			}

			populateFields();

	        confirmButton.setOnClickListener(new View.OnClickListener() {

	            public void onClick(View view) {
	                setResult(RESULT_OK);
	                finish();
	            }

	        });
	    }

	    private void populateFields() {
	        if (mRowId != null) {
	            Cursor note = mDbHelper.fetchItem(mRowId);
	            startManagingCursor(note);
	            mNameText.setText(note.getString(
	                    note.getColumnIndexOrThrow(ItemsModel.KEY_NAME)));
	            mPriceText.setText(note.getString(
	                    note.getColumnIndexOrThrow(ItemsModel.KEY_PRICE)));
	            mPriorityText.setText(note.getString(
	                    note.getColumnIndexOrThrow(ItemsModel.KEY_PRIORITY)));
	        }
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        saveState();
	        outState.putSerializable(ItemsModel.KEY_ROWID, mRowId);
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        saveState();
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	        populateFields();
	    }

	    private void saveState() {
	        String name = mNameText.getText().toString();
	        String price = mPriceText.getText().toString();
	        int priority = mDbHelper.getHighestPriority();

	        if (mRowId == null) {
	            long id = mDbHelper.createItem(name, price, priority);
	            if (id > 0) {
	                mRowId = id;
	            }
	        } else {
	        	priority = Integer.parseInt(mPriorityText.getText().toString());
	            mDbHelper.updateItem(mRowId, name, price, priority);
	        }
	    }

}
