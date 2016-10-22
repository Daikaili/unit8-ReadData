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
	 *   ����ͨѶ¼�е��û����ͺ������ڲ�ͬ�ı��У�������ϵ��ID��ϵ�������������
	 *   ��ϵ���д���һ���յļ�¼�������µ�ID�ţ�Ȼ�����ID�ŷֱ��������ű��в�����Ӧ������
	 */ 
	public void addPerson(){      //�����ϵ��
      String nameStr=editName.getText().toString();
      String phoneStr=editPhone.getText().toString();    //�����ϵ�������ͺ���
      
      ContentValues values=new ContentValues();   //����һ���յ�ContentValues
  
	//��RawContacts.CONTENT_URI�����ֵ��Ŀ���ǻ�ȡ���ص�ID��
      Uri rawContactsUri=resolver.insert(RawContacts.CONTENT_URI,values);
      long contactId=ContentUris.parseId(rawContactsUri);
      //�õ�����ϵ�˵�ID��
      values.clear();   //���values������
      values.put(Data.RAW_CONTACT_ID, contactId);   //����ID��
      values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);  //��������
      values.put(StructuredName.GIVEN_NAME, nameStr);         //��������
      resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
      //��Uri�����ϵ������
      values.clear();
      values.put(Data.RAW_CONTACT_ID, contactId);
      values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
      values.put(Phone.NUMBER,phoneStr);
      values.put(Phone.TYPE, Phone.TYPE_MOBILE);
      resolver.insert(android.provider.ContactsContract.Data.CONTENT_URI, values);
      //���õ绰��ID�ţ����ͣ����룬�绰
      
      Toast.makeText(MainActivity.this,"��ϵ��������ӳɹ�", 1000).show();

	}
	
	/**
	 * ���Ȳ�ѯ�����˵���ϵ��������ID�ţ�Ȼ�����ID�Ų�ѯ������еĺ��룬�ٽ�
	 * ÿ���˵���Ϣ����ͬһ��map�����У�������map������ӵ��б��У���Ϊ�������
	 */
	public ArrayList<Map<String,String>>queryPerson(){
		//����һ������������ϵ����Ϣ���б�ÿ����һ��map����
		ArrayList<Map<String,String>>detail=new ArrayList<Map<String,String>>();
		Cursor  cursor=resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		//��ѯͨѶ¼��������ϵ��
		while(cursor.moveToNext()){
			Map<String,String>person=new HashMap<String,String>();
			//������ѯÿһ����ϵ�ˣ�ÿ����ϵ�˵���Ϣ��һ��map���󴢴�
			String personId=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//��ȡ��ϵ��ID �ź�����
			person.put("id", personId);  //����ȡ����Ϣ����map������
			person.put("name",name);
			Cursor nums=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+personId,null,null);
			//����ID�ţ���ѯ�ֻ�����
			if(nums.moveToNext()){
				String num=nums.getString(nums.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				person.put("num", num);
			}   //���ֻ��Ŵ���map����
			nums.close();               //�ر���Դ
			detail.add(person);
			}
		cursor.close();              //�ر���Դ
		return detail;               //���ز�ѯ�б�
		
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
