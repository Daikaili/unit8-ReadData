package com.example.readdata;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.text.Editable;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.TextView;

public class MainActivity extends Activity {
    private Button btnName,btnShow;
    private EditText editName,editPhone;
    private TextView tvName,tvPhone;
    private ListView ListResult;
	private ContentResolver resolver;
	private LinearLayout title;
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
	/**
	 *   由于通讯录中的用户名和号码存放在不同的表中，根据联系人ID联系起来，因此先向
	 *   联系人中创建一个空的记录，产生新的ID号，然后根据ID号分别在这两张表中插入相应的数据
	 */ 
	public void addPerson(){      //添加联系人
      String nameStr=editName.getText().toString();
      String phoneStr=editPhone.getText().toString();    //获得联系人姓名和号码
      
      ContentValues values=new ContentValues();   //创建一个空的ContentValues
  
	//向RawContacts.CONTENT_URI插入空值，目的是获取返回的ID号
      Uri rawContactsUri=resolver.insert(RawContacts.CONTENT_URI,values);
      long contactId=ContentUris.parseId(rawContactsUri);
      //得到新联系人的ID号
      values.clear();   //清空values的内容
      values.put(Data.RAW_CONTACT_ID, contactId);   //设置ID号
      values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);  //设置类型
      values.put(StructuredName.GIVEN_NAME, nameStr);         //设置姓名
      resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
      //向Uri添加联系人姓名
      values.clear();
      values.put(Data.RAW_CONTACT_ID, contactId);
      values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
      values.put(Phone.NUMBER,phoneStr);
      values.put(Phone.TYPE, Phone.TYPE_MOBILE);
      resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
      //设置电话的ID号，类型，号码，电话
      
      Toast.makeText(MainActivity.this,"联系人数据添加成功", 1000).show();
      
      
      
      
      
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
