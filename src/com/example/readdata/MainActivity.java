package com.example.readdata;

import android.os.Bundle;


import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

public class MainActivity extends Activity {
    private Button btnName,btnShow;
    private EditText editName,editPhone;
    private TextView tvName,tvPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnName=(Button) this.findViewById(R.id.btnAdd);
		btnShow=(Button) this.findViewById(R.id.btnShow);
		editName=(EditText) this.findViewById(R.id.editName);
		editPhone=(EditText) this.findViewById(R.id.editPhone);
		tvName=(TextView) this.findViewById(R.id.tvName);
		tvPhone=(TextView) this.findViewById(R.id.tvPhone);
		
		
	    
		
		
	}
	public void addPerson(){      //添加联系人
      String nameStr=editName.getText().toString();
      String phoneStr=editPhone.getText().toString();    //获得联系人姓名和号码
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
