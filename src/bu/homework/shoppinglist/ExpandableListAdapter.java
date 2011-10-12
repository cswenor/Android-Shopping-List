package bu.homework.shoppinglist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
	@Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    private Context context;

    private ArrayList<String> groups;

    private ArrayList<ArrayList<ShoppingItem>> children;

    public ExpandableListAdapter(Context context, ArrayList<String> groups,
            ArrayList<ArrayList<ShoppingItem>> children) {
        this.context = context;
        this.groups = groups;
        this.children = children;
    }

    /**
     * A general add method, that allows you to add a ShoppingItem to this list
     * 
     * Depending on if the category pf the shoppingItem is present or not,
     * the corresponding item will either be added to an existing group if it 
     * exists, else the group will be created and then the item will be added
     * @param shoppingItem
     */
    public void addItem(ShoppingItem shoppingItem, String group) {
        
    	if (!groups.contains(group)) {
            groups.add(group);
        }
        int index = groups.indexOf(group);
        if (children.size() < index + 1) {
            children.add(new ArrayList<ShoppingItem>());
        }
        children.get(index).add(shoppingItem);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    // Return a child view. You can load your custom layout here.
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        
    	ShoppingItem shoppingItem = (ShoppingItem) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_layout, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvChild);
        tv.setText("   " + shoppingItem.getName());

//        // Depending upon the child type, set the imageTextView01
//        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//        if (shoppingItem instanceof Car) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.car, 0, 0, 0);
//        } else if (shoppingItem instanceof Bus) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bus, 0, 0, 0);
//        } else if (shoppingItem instanceof Bike) {
//            tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bike, 0, 0, 0);
//        }
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Return a group view. You can load your custom layout here.
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_layout, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tvGroup);
        tv.setText(group);
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
    public void clear(){
    	this.groups.clear();
    	this.children.clear();
    }
}
