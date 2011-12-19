package com.android.hrmanager.activity;

import com.android.hrmanager.R;
import com.android.hrmanager.R.id;
import com.android.hrmanager.R.layout;
import com.android.hrmanager.bd.DBManager;
import com.android.hrmanager.model.hr_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Button				loginButton;
	private Button				cancelButton;
	private	EditText			loginField;
	private	EditText			passwdField;
	private DBManager DBAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		try
		{
			DBAdapter = new DBManager(this);
			DBAdapter.CreateDatabase();		
		}
		catch (Exception e) {
			Log.e(MainActivity.TAG, e.getMessage());
		}
	    
		
		setButtonsActions();
	}

	private void setButtonsActions() {
		cancelButton = (Button) findViewById(R.id.btCancel);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionClose();
			}
		});
		loginButton = (Button) findViewById(R.id.btEnter);
		loginButton.setOnClickListener( new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionLogin();
			}
		});
	}

	private void actionClose() {
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
	}

	private void actionLogin() {
		passwdField = (EditText) findViewById(R.id.inputPassword);
		loginField = (EditText) findViewById(R.id.inputUser);
		String login = loginField.getText().toString();
		String password = passwdField.getText().toString();
		if (login.isEmpty() || password.isEmpty())
			Toast.makeText(this, "Preencha Usuário e Senha", Toast.LENGTH_LONG).show();
		else{
						
			
			hr_user user = new hr_user();
			
			user.setLogin(login);
			user.setPassword(password);
			
			user = DBAdapter.LoginUser(user);
			
			if(user != null) {
				Toast.makeText(this, "Login efetuado com sucesso", Toast.LENGTH_LONG).show();
				setAuth();
				setUserId(user.getCodUser());
				CheckBox loginRequired = (CheckBox)findViewById(R.id.checkLogin);
					
				if (!loginRequired.isChecked()) {
					setLoginRequired();
				}
				Intent it = new Intent(this, MainActivity.class);
			    startActivity(it);
			    finish();
			}
			else
			{
				Toast.makeText(this, "Login inválido. Tente novamente.", Toast.LENGTH_LONG).show();
			}
		}
			
	}
	
	public void setAuth() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(MainActivity.SAP_PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sapPreferences.edit();
		edit.putBoolean(MainActivity.AUTHENTICATED, true);
		
		edit.commit();
	}
	
	public void setLoginRequired() {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(MainActivity.SAP_PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sapPreferences.edit();
		edit.putBoolean(MainActivity.AUTHENTICATED, true);
		
		edit.commit();
	}
	
	public void setUserId(int codUser) {
		SharedPreferences sapPreferences = this.getApplicationContext().getSharedPreferences(MainActivity.SAP_PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sapPreferences.edit();
		edit.putInt(MainActivity.USER_ID, codUser);
		edit.commit();
	}
}