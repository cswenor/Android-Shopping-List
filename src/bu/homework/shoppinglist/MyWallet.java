package bu.homework.shoppinglist;

import android.app.Application;

public class MyWallet extends Application {
	  private String myMoney = "0.00";

	  public String getState(){
	    return myMoney;
	  }
	  public void setState(String s){
	    myMoney = s;
	  }
}
