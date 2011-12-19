package com.android.hrmanager.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.hrmanager.R;
import com.android.hrmanager.R.layout;
import com.android.hrmanager.bd.DBManager;
import com.android.hrmanager.model.hr_user;
import com.android.hrmanager.model.hr_vacation;


public class VacationActivity extends Activity {
	
	private static final int DATE_DIALOG_ID = 0;
	
	 private Button	  btnBackButton;
	 private Button   btAccept;
	 private EditText inputManagerField;
	 private EditText inputDepartmentField;
	 private EditText inputDateField;
	 
	 private RadioButton rbOneWeek;
	 private RadioButton rbTwoWeek;
	 private RadioButton rbTreeWeek;
	 private RadioButton rbFourWeek;	 
	 
	 private hr_user UserLogued;
	 private DBManager DBAdapter;
	 
	 private int mYear;
	 private int mMonth;
	 private int mDay;
	
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
		setContentView(R.layout.vacation);
		
		 DBAdapter = new DBManager(this);
		 
		 
		 inputManagerField = (EditText)findViewById(R.id.inputManager);
		 inputDepartmentField = (EditText)findViewById(R.id.inputDepartment);
		 inputDateField = (EditText)findViewById(R.id.inputDate);
		 
		 btnBackButton = (Button)findViewById(R.id.btAccept);
		 
		 rbOneWeek = (RadioButton)findViewById(R.id.rbOneWeek);
		 rbTwoWeek = (RadioButton)findViewById(R.id.rbTwoWeek);
		 rbTreeWeek = (RadioButton)findViewById(R.id.rbThreeWeek);
		 rbFourWeek = (RadioButton)findViewById(R.id.rbFourWeek);
		 
		setInteface();
		setButtonsActions();
		setFieldsAction();
		setDataPicker();
		
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
	}
	
	private void setInteface() {
		
			int codUser = getUserId();
			hr_user user = DBAdapter.getUser(codUser);
			this.UserLogued = user;
			if(user.getSupervisor() != null)
				inputManagerField.setText(user.getSupervisor().getName());
			
			inputDepartmentField.setText(user.getSector());
		
		
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
	
	private void setButtonsActions() {
		btnBackButton = (Button) findViewById(R.id.btReject);
		btnBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBack();
			}
		});
		
		btAccept = (Button) findViewById(R.id.btAccept);
		btAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionAccept();
			}		
		});

		
	}
	
	private void actionAccept() {
		
		Boolean ErrorFlag = false;
		
		hr_vacation vac = new hr_vacation();
		
		if(rbOneWeek.isChecked())
			vac.setPeriod("1 SEMANA");
		else if(rbTwoWeek.isChecked())
			vac.setPeriod("2 SEMANAS");
		else if(rbTreeWeek.isChecked())
			vac.setPeriod("3 SEMANAS");
		else if(rbFourWeek.isChecked())
			vac.setPeriod("4 SEMANAS");		
		
		vac.setDate(inputDateField.getText().toString());
				
		vac.setUser(this.UserLogued);
		
		if(vac.getPeriod() == null || vac.getPeriod().equals(""))
		{
			ErrorFlag = true;
			Toast.makeText(this, "Período não informado.", Toast.LENGTH_LONG).show();
		}
		
		if(vac.getDate() == null || vac.getDate().equals(""))
		{
			ErrorFlag = true;
			Toast.makeText(this, "Data não informada.", Toast.LENGTH_LONG).show();
		}
		
		if(vac.getUser() == null)
		{
			ErrorFlag = true;
			//Toast.makeText(this, "Período não informado.", Toast.LENGTH_LONG).show();
		}
		
		if(!ErrorFlag)
		{
			DBAdapter.addHoliday(vac);
			Toast.makeText(this, "Solicitação de férias enviada com sucesso.", Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
    private void updateDisplay() {
    	EditText edtDate = (EditText) findViewById(R.id.inputDate);
    	edtDate.setText(
            new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/")
                    .append(mMonth+1).append("/")
                    .append(mYear).append(" "));
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
	
	public int getUserId() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(MainActivity.SAP_PREFS, Context.MODE_PRIVATE);
		return sapPreferences.getInt(MainActivity.USER_ID, 0);
	}

}
