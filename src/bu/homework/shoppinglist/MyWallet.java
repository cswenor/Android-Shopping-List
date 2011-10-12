package bu.homework.shoppinglist;

import android.app.Application;

public class MyWallet extends Application {
	  private String myMoney;

	  public String getState(){
	    return myMoney;
	  }
	  public void setState(String s){
	    myMoney = s;
	  }
}
