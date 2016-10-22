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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
