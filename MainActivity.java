package com.example.readdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import android.widget.TextView;

public class MainActivity extends Activity {
    private Button btnAdd,btnShow;
    private EditText editName,editPhone;
    private TextView tvName,tvPhone;
    private ListView ListResult;
	private ContentResolver resolver;
	private LinearLayout title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitAll();
	    addOnClickListener();
	}
	private void addOnClickListener() {
		// TODO Auto-generated method stub
		  MyOnClickListener myOnClickListener=new MyOnClickListener();
		  btnAdd.setOnClickListener(myOnClickListener);
		  btnShow.setOnClickListener(myOnClickListener);
	}
	private void InitAll() {
		// TODO Auto-generated method stub
		btnAdd=(Button) this.findViewById(R.id.btnAdd);
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
	
	/**
	 * 首先查询所有人的联系人姓名和ID号，然后根据ID号查询号码表中的号码，再将
	 * 每个人的信息放入同一个map对象中，最后将这个map对象添加到列表中，作为结果返回
	 */
	public ArrayList<Map<String,String>>queryPerson(){
		//创建一个保存所有联系人信息的列表，每项是一个map对象
		ArrayList<Map<String,String>>detail=new ArrayList<Map<String,String>>();
		Cursor  cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		//查询通讯录中所有联系人
		while(cursor.moveToNext()){
			Map<String,String>person=new HashMap<String,String>();
			//遍历查询每一个联系人，每个联系人的信息用一个map对象储存
			String personId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//获取联系人ID 号和姓名
			person.put("id", personId);  //将获取的信息存入map对象中
			person.put("name",name);
			Cursor nums=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+personId,null,null);
			//根据ID号，查询手机号码
			if(nums.moveToNext()){
				String num=nums.getString(nums.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				person.put("num", num);
			}   //将手机号存入map对象
			nums.close();               //关闭资源
			detail.add(person);
			}
		cursor.close();              //关闭资源
		return detail;               //返回查询列表
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    private class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btnAdd:
				addPerson();
				break;
			
			case R.id.btnShow:
			title.setVisibility(View.VISIBLE);
			ArrayList<Map<String,String>>persons=queryPerson();
			SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,persons,R.layout.result,new String[]{
					"id","name","num"},new int[]{
					R.id.personname,R.id.personnum,R.id.personid});
			
			ListResult.setAdapter(adapter);
			  default:break;

		}
		}
    }
}
