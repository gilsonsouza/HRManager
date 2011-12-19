package com.android.hrmanager.activity;

import java.util.Calendar;

import com.android.hrmanager.R;
import com.android.hrmanager.bd.DBManager;
import com.android.hrmanager.model.hr_justification;
import com.android.hrmanager.model.hr_user;
import com.android.hrmanager.model.hr_vacation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class AbsenceActivity extends Activity{
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int DATE_DIALOG_ID = 0;
	private Uri fileUri;
	private Button	  btnBackButton;
	private Button	  btAttachButton;
    private int mYear;
    private int mMonth;
    private int mDay;
    
    private RadioButton rbTransport;
    private RadioButton rbSick;
    private RadioButton rbOther;
    
    private EditText inputJustificationField;
    private EditText inputDateField;
    
    private Button btConfirm;
    private hr_user UserLogued;
	private DBManager DBAdapter;
	
	 private DatePickerDialog.OnDateSetListener mDateSetListener =
         new DatePickerDialog.OnDateSetListener() {

		 @Override
             public void onDateSet(DatePicker view, int year, 
                                   int monthOfYear, int dayOfMonth) {
                 mYear = year;
                 mMonth = monthOfYear;
                 mDay = dayOfMonth;
                 updateDisplay();
             }
         };
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.absence);
		
		rbTransport = (RadioButton) findViewById(R.id.rbTrasnport);
		rbSick = (RadioButton) findViewById(R.id.rbSick);
		rbOther = (RadioButton) findViewById(R.id.rbOther);
		    
		inputJustificationField = (EditText) findViewById(R.id.inputJustification);
		inputDateField = (EditText) findViewById(R.id.inputDate);
		    
		DBAdapter = new DBManager(this);
				
		setButtonsActions();
		setFieldsAction();
		setDataPicker();
				
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	
	private void setDataPicker() { 
	    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();
                }
            };
	}
	
    private void updateDisplay() {
    	EditText edtDate = (EditText) findViewById(R.id.inputDate);
    	edtDate.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/")
                    .append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
    }
	
	private void setButtonsActions() {
		btnBackButton = (Button) findViewById(R.id.btBack);
		btnBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBack();
			}
		});
		btAttachButton = (Button) findViewById(R.id.btAttach);
		btAttachButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionAttach();
			}
		});
		btConfirm = (Button) findViewById(R.id.btConfirm);
		btConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				actionConfirm();
			}
		});
	}
	
	private void setFieldsAction() {
		EditText edtDate = (EditText) findViewById(R.id.inputDate);
		edtDate.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				 showDialog(DATE_DIALOG_ID);
			}
		});
	}
	
	
	private void actionBack() {
		Intent it = new Intent(this, NewsActivity.class);
		//it.putExtra("UserLogued", user.getCodUser());
	    startActivity(it);
	    finish();
	}
	
	private void actionAttach() {
	    //create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    
		 //fileUri = getOutputMediaFileUri("teste"); // create a file to save the image    
		 intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name    
		 // start the image capture Intent    
		 startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	   
	}
	private void actionConfirm()
	{
		Boolean ErrorFlag = false;
		
		hr_justification NewJust = new hr_justification();
		
		if(rbTransport.isChecked())
			NewJust.setReason("Tranporte");
		else if(rbSick.isChecked())
			NewJust.setReason("Doença");
		else if(rbOther.isChecked())
			NewJust.setReason("Outro");
		
		NewJust.setDate(inputDateField.getText().toString());
				
		NewJust.setDescription(inputJustificationField.getText().toString());
		
		try
		{		
			int codUser = getUserId();
			
			hr_user user = DBAdapter.getUser(codUser);
			
			this.UserLogued = user;
			
			NewJust.setUser(user);
		}
		catch (Exception e) {
			Log.e(MainActivity.TAG,e.getMessage());
		}
		
		if(NewJust.getReason() == null || NewJust.getReason().equals(""))
		{
			ErrorFlag = true;
			Toast.makeText(this, "Motivo não informado.", Toast.LENGTH_LONG).show();
		}
		
		if(NewJust.getDate() == null || NewJust.getDate().equals(""))
		{
			ErrorFlag = true;
			Toast.makeText(this, "Data não informada.", Toast.LENGTH_LONG).show();
		}
		
		if(NewJust.getDescription() == null || NewJust.getDescription().equals(""))
		{
			ErrorFlag = true;
			Toast.makeText(this, "Justificativa não informada.", Toast.LENGTH_LONG).show();
		}
		
		if(NewJust.getUser() == null)
		{
			ErrorFlag = true;			
		}
		
		if(!ErrorFlag)
		{
			DBAdapter.addJust(NewJust);
			Toast.makeText(this, "Justificativa de falta enviada com sucesso.", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {    
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {        
			if (resultCode == RESULT_OK) {           
				   // Image captured and saved to fileUri specified in the Intent            
				   Toast.makeText(this, "Imagem anexada com sucesso", Toast.LENGTH_LONG).show();        
				} 
			else if (resultCode == RESULT_CANCELED) {            
				// User cancelled the image capture        				
				} 
			else {            
					// Image capture failed, advise user        
				 }   		
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DATE_DIALOG_ID:
	        return new DatePickerDialog(this,
	                    mDateSetListener,
	                    mYear, mMonth, mDay);
	    }
	    return null;
	}
	
	public int getUserId() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(MainActivity.SAP_PREFS, Context.MODE_PRIVATE);
		return sapPreferences.getInt(MainActivity.USER_ID, 0);
	}

}
