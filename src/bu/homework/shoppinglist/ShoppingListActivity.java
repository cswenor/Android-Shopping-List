package bu.homework.shoppinglist;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class ShoppingListActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Resources resources = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec; // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ItemListActivity.class);
        
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("items").setIndicator("Items",
        												resources.getDrawable(R.drawable.ic_tab_item_list))
										  .setContent(intent);
        tabHost.addTab(spec);
        // Repeat for all Tabs
        intent = new Intent().setClass(this, MoneyInHandActivity.class);
        spec = tabHost.newTabSpec("wallet").setIndicator("Wallet",
        												resources.getDrawable(R.drawable.ic_tab_item_list))
										  .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, PurchasedItemsActivity.class);
        spec = tabHost.newTabSpec("purchased").setIndicator("Purchased",
        												resources.getDrawable(R.drawable.ic_tab_item_list))
										  .setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
    }
}
