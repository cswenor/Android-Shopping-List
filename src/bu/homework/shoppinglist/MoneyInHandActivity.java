package bu.homework.shoppinglist;

import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class MoneyInHandActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.wallet);
	    
	    final EditText etWalletAmount = (EditText) findViewById(R.id.wallet_amount);

	    // Add a Listener to the Wallet EditText
	    etWalletAmount.addTextChangedListener(new TextWatcher(){
	    	String current = "";
	    	
	    	public void onTextChanged(CharSequence s, int start, int before, int count){
	    		if(!s.toString().equals(current)){ // Check to make sure that the string has actually changed
	    			etWalletAmount.removeTextChangedListener(this); // Remove the listener so it doesn't get called before this function has finished
	    			
	    			// Grabs the correct currency symbol and cleans the string
	    			String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
	    			String cleanString = s.toString().replaceAll(replaceable,"");
	    			
	    			// Converts the string and converts it to currency format
	    			double parsed = Double.parseDouble(cleanString);
	    			String formated = NumberFormat.getCurrencyInstance().format((parsed/100));
	    			
	    			//Sets the current string to the formatted string
	    			current = formated;
	    			
	    			// Sets the reformatted text to the edittext field
	    			etWalletAmount.setText(formated);
	    			MyWallet appState = ((MyWallet)getApplicationContext());
	    			
	    		    appState.setState(formated);

	    			
	    			// Moves the cursor to the end of the field
	    			etWalletAmount.setSelection(formated.length());
	    			
	    			// Re-add the Listener
	    			etWalletAmount.addTextChangedListener(this);
	    			
	    		}
	    	}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
	    });
	
	}

}
