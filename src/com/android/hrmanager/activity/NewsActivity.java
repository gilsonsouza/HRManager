package com.android.hrmanager.activity;

import java.util.ArrayList;

import  com.android.hrmanager.R;
import com.android.hrmanager.R.id;
import com.android.hrmanager.R.layout;
import com.android.hrmanager.bd.DBManager;
import com.android.hrmanager.model.hr_justification;
import com.android.hrmanager.model.hr_news;
import com.android.hrmanager.model.hr_vacation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewsActivity extends Activity {
	
	private Button absenceButton;
	private Button vacationButton;
	private DBManager DBAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setButtonsActions();
		setLayoutActions();
		
		DBAdapter = new DBManager(this);
		
		alertsManager();
	}
	
	private void setButtonsActions() {
		absenceButton = (Button) findViewById(R.id.btVacation);
		absenceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionAbsenceScreen();
			}
		});
		vacationButton = (Button) findViewById(R.id.btAbsent);
		vacationButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionVacationScreen();
			}
		});
	}
	
	private void actionAbsenceScreen() {
		Intent it = new Intent(this, VacationActivity.class);
		startActivity(it);
	}
	
	private void actionVacationScreen() {
		Intent it = new Intent(this, AbsenceActivity.class);
		startActivity(it);
	}
	
	private void setLayoutActions() {
		LinearLayout messagesLayout = (LinearLayout) findViewById(R.id.messages);
		messagesLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionPopupMessages();
			}
		});
	}
	
	private void actionPopupMessages() {
		
		if (getVacationMessagesCont()>0) {
			
		ArrayList<hr_vacation> vacList = DBAdapter.getAllholiday();

		
		hr_vacation vacation = vacList.get(0);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Sua solicitação de férias do dia "+vacList.get(0).getDate()+ " foi aprovada.")
    	       .setCancelable(false)
    	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    	DBAdapter.clearHolidays(vacation.getCodhol());
    	alertsManager();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setMessage("Você realmente deseja sair?")
	    	       .setCancelable(false)
	    	       .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                finish();
	    	           }
	    	       })
	    	       .setNegativeButton("Não", new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	AlertDialog alert = builder.create();
	    	alert.show();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	private int getVacationMessagesCont() {
		
		ArrayList<hr_vacation> vacList = DBAdapter.getAllholiday();
		return vacList.size();
	}
	
	private int getAbsencesMessagesCont() {
		
		ArrayList<hr_justification> absList = DBAdapter.getAllJust();
		return absList.size();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		alertsManager();
	}
	
	private void alertsManager() {
		
		TextView TextAlert = (TextView)findViewById(R.id.newsCount);
		TextView TextNewsTop = (TextView)findViewById(R.id.newsTitleTop);
		TextView TextNewsBottom = (TextView)findViewById(R.id.newsTitleBottom);
		
		ArrayList<hr_news> NewsList = DBAdapter.getAllNews();
			
		if (getVacationMessagesCont()==0) {
			TextAlert.setText("Você não possui novas mensagens.");
		} else if (getVacationMessagesCont()==1) {
			TextAlert.setText("Você tem " + (getVacationMessagesCont())+" mensagem.");
		} else {
			TextAlert.setText("Você tem " + (getVacationMessagesCont())+" mensagens.");
		}
			
		TextNewsTop.setText(NewsList.get(0).getDescription());
		TextNewsBottom.setText(NewsList.get(1).getDescription());					
		
	}
}
