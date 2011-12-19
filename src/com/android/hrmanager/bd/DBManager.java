package com.android.hrmanager.bd;

import java.util.ArrayList;

import com.android.hrmanager.model.hr_vacation;
import com.android.hrmanager.model.hr_justification;
import com.android.hrmanager.model.hr_news;
import com.android.hrmanager.model.hr_user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


//Class Management Database
public class DBManager {

	static final String dbName="HrManagerDB";
	
	static SQLiteDatabase db;
	
	Context context;
	
	// Table News Definition 
	
	static final String NewsTable="News";
	static final String colNewsID="codNews";
	static final String colNewsImage="image";
	static final String colNewsDescription="description";
	
	// Table Users Definition
	
	static final String UsersTable="Users";
	static final String colUsersID="codUser";
	static final String colUsersDocument="document";
	static final String colUsersName="name";
	static final String colUsersFunction ="function";
	static final String colUsersSector ="sector";
	static final String colUsersSupervisor ="codSupervisor";
	static final String colUsersLogin ="login";
	static final String colUsersPassword ="password";
	
	// Table Users justifications
	
	static final String justTable="justifications";
	static final String coljustID="codJust";
	static final String coljustDate="date";
	static final String coljustreason="reason";
	static final String coljustdesc ="description";
	static final String coljustUser ="codUser";
	
	
	// Table Users holiday
	
	static final String holidayTable ="holiday";
	static final String colholidayID ="codhol";
	static final String colholidayDate ="date";
	static final String colholidayPeriod ="period";
	static final String colholidayUser ="codUser";
	
	public DBManager(Context context) {
		this.context = context;
		db = context.openOrCreateDatabase(dbName,
				Context.MODE_PRIVATE, null);
		db.close();
	}
			
	public void OpenDatabase()
	{
		   try
	       {
	          db = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);       	      
	       }
	       catch (Exception e) {	
	    	   e.getMessage();
	       }
	}
	
	public void CloseDatabase()
	{
		   try
	       {
	          db.close();	       	      
	       }
	       catch (Exception e) {			 
	       }
	}
	
	public void CreateDatabase()
	{
		OpenDatabase();
			
		//CREATE TABLE NEWS
		db.execSQL("CREATE TABLE IF NOT EXISTS "+NewsTable +"("+
					 colNewsID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   	 colNewsImage +" TEXT," +
				     colNewsDescription +"  TEXT);");
		
		//CREATE TABLE USERS
		db.execSQL("CREATE TABLE IF NOT EXISTS "+UsersTable+"("+
		           colUsersID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   colUsersDocument +" TEXT,"+
		           colUsersName +"  TEXT,"+
				   colUsersFunction  +"  TEXT,"+
		           colUsersSector+"  TEXT,"+
				   colUsersSupervisor+"  INTEGER,"+
		           colUsersLogin+"  TEXT,"+				   
				   colUsersPassword+" TEXT,"+
			       "FOREIGN KEY("+colUsersSupervisor+") REFERENCES Users("+colUsersID+"));");
		
		//CREATE TABLE JUSTIFICATIONS
		db.execSQL("CREATE TABLE IF NOT EXISTS "+justTable+"("+
		           coljustID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   coljustDate +" TEXT,"+
		           coljustreason +"  TEXT,"+
				   coljustdesc  +"  TEXT,"+
		           coljustUser +" INTEGER,"+
		           "FOREIGN KEY("+coljustUser+") REFERENCES Users(codUser));");
		
		//CREATE TABLE HOLIDAY
		db.execSQL("CREATE TABLE IF NOT EXISTS "+holidayTable+"("+
		           colholidayID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				   colholidayDate +" TEXT,"+
		           colholidayPeriod +"  TEXT,"+
		           colholidayUser +" INTEGER,"+
		           "FOREIGN KEY("+colholidayUser+") REFERENCES Users(codUser));");
		
		/* INITIALIZING TABLES */
		
		db.execSQL("DELETE FROM "+NewsTable);
		db.execSQL("DELETE FROM "+UsersTable);
		db.execSQL("DELETE FROM "+justTable);
		db.execSQL("DELETE FROM "+holidayTable);
		
		hr_news auxNotice = new hr_news();
		
		auxNotice.setDescription("Equipe 5 sai vitoriosa do SAP Apps Experience 2011.");
		
        addNews(auxNotice);
		
		auxNotice.setDescription("Week of Service será realizado na próxima semana.");
		
		addNews(auxNotice);
		
		auxNotice.setDescription("Sap App Experience 3");
		
		addNews(auxNotice);
		
		auxNotice.setDescription("Sap App Experience 4");
		
		addNews(auxNotice);
		
		hr_user Supervisor = new hr_user();
		
		Supervisor.setDocument("00000000000");
		Supervisor.setFunction("Project Management");
		Supervisor.setName("Guilherme");
		Supervisor.setSector("Recursos Humanos");
		Supervisor.setSupervisor(null);
		Supervisor.setLogin("supervisor");
		Supervisor.setPassword("sap");
		
		addUser(Supervisor);
		
	    Supervisor = LoginUser(Supervisor);
		
		hr_user func = new hr_user();
		
		func.setDocument("018663777777");
		func.setFunction("Developer");
		func.setName("Douglas Souza");
		func.setSector("TI-Android");
		func.setSupervisor(Supervisor);
		func.setLogin("func");
		func.setPassword("sap");
		
		addUser(func);
		
		func = LoginUser(func);
		
		hr_vacation hol = new hr_vacation();
		
		hol.setUser(func);
		hol.setDate("30/10/2011");
		hol.setPeriod("1 SEMANA");
		
		addHoliday(hol);
		
		hr_justification just = new hr_justification();
		
		just.setDate("30/10/2011");
		just.setReason("Transporte");
		just.setDescription("Perdi onibus");
		just.setUser(func);
				
		addJust(just);
		
					
		CloseDatabase();
	}
	
	
	//Methods for operations with Table News
	public ArrayList<hr_news> getAllNews()
	{
	   ArrayList<hr_news> NewsList = new ArrayList<hr_news>();	
	   OpenDatabase();
	   
	   Cursor cur = db.rawQuery("SELECT "+colNewsID+", "+colNewsImage+","+ colNewsDescription +" from "+ NewsTable, new String [] {});
	   
	   try {
           
		      while(cur.moveToNext())
		      {
		    	  hr_news auxNewNotice = new hr_news();
		    	  auxNewNotice.setCodNew(cur.getInt(0));
		    	  auxNewNotice.setImage(cur.getBlob(1));
		    	  auxNewNotice.setDescription(cur.getString(2));
		    	  
		    	  NewsList.add(auxNewNotice);
		      }
		      } finally {
		    	  cur.close();
		      }
	   
	   
	   CloseDatabase();
	   
	   return NewsList;
	}
	
	public void addNews(hr_news newNotice)
	{		
		 OpenDatabase();
		 ContentValues cv = new ContentValues();		   
		   	     		   
	     cv.put(colNewsImage, newNotice.getImage());
	     cv.put(colNewsDescription,newNotice.getDescription());
		   
		 db.insert(NewsTable, colNewsID, cv);
		 
		 CloseDatabase();
	}
	
	//Methods for operations with Table Users
	
	public void addUser(hr_user newUser)
	{
		 OpenDatabase();
		 ContentValues cv = new ContentValues();		   
		   	     		   
	     cv.put(colUsersDocument, newUser.getDocument());
	     cv.put(colUsersName, newUser.getName());
	     cv.put(colUsersFunction, newUser.getFunction());
	     cv.put(colUsersSector, newUser.getSector());
	     
	     if(newUser.getSupervisor() != null)
	    	 cv.put(colUsersSupervisor, newUser.getSupervisor().getCodUser());
	     
	     cv.put(colUsersLogin, newUser.getLogin());
	     cv.put(colUsersPassword, newUser.getPassword());
	     		   
		 db.insert(UsersTable, colUsersID, cv);
		 
		 CloseDatabase();
	}
	
	public hr_user LoginUser(hr_user newUser)
	{
		 OpenDatabase();
				 
		 hr_user auxUser = null;
		 Cursor cur = db.rawQuery("SELECT "+colUsersID+", "+colUsersDocument+","+ colUsersName +","+ colUsersFunction +","+ colUsersSector +","+ colUsersSupervisor +","+ colUsersLogin +","+ colUsersPassword +
             " FROM "+ UsersTable+" WHERE login ='"+newUser.getLogin()+"' AND password='"+newUser.getPassword()+"'", new String [] {});
		 
		 try {
	           
		      while(cur.moveToNext())
		      {
		    	  auxUser = new hr_user();
		    	  auxUser.setCodUser(cur.getInt(0));
		    	  auxUser.setDocument(cur.getString(1));
		    	  auxUser.setName(cur.getString(2));
		    	  auxUser.setFunction(cur.getString(3));
		    	  auxUser.setSector(cur.getString(4));
		    	  auxUser.setSupervisor(getUser(cur.getInt(5)));
		    	  auxUser.setLogin(cur.getString(6));
		    	  auxUser.setPassword(cur.getString(7));		    	  		    	 		    	 
		      }
		      }
		 catch (Exception e) {}
		 finally 
		 {
		    cur.close();
		 }
		 
		 CloseDatabase();
		 
		 return auxUser;
	}
		
	public hr_user getUser(int codUser)
	{
		 OpenDatabase();
				 
		 hr_user auxUser = null;
		 Cursor cur = db.rawQuery("SELECT "+colUsersID+", "+colUsersDocument+","+ colUsersName +","+ colUsersFunction +","+ colUsersSector +","+ colUsersSupervisor +","+ colUsersLogin +","+ colUsersPassword +
				                  " FROM "+ UsersTable+" WHERE codUser="+codUser, new String [] {});
		 
		 try {
	           
		      while(cur.moveToNext())
		      {
		    	  auxUser = new hr_user();
		    	  auxUser.setCodUser(cur.getInt(0));
		    	  auxUser.setDocument(cur.getString(1));
		    	  auxUser.setName(cur.getString(2));
		    	  auxUser.setFunction(cur.getString(3));
		    	  auxUser.setSector(cur.getString(4));
		    	  auxUser.setSupervisor(getUser(cur.getInt(5)));
		    	  auxUser.setLogin(cur.getString(6));
		    	  auxUser.setPassword(cur.getString(7));		    	  		    	 		    	 
		      }
		      }
		 catch (Exception e) {}
		 finally 
		 {
		    cur.close();
		    CloseDatabase();
		 }
		 
		 
		 
		 return auxUser;
	}

	
	//Methods for operations with Table Justifications
	public ArrayList<hr_justification> getAllJust()
	{
	   ArrayList<hr_justification> JustList = new ArrayList<hr_justification>();	
	   OpenDatabase();
	   
       Cursor cur = db.rawQuery("SELECT "+coljustID+", "+coljustDate+","+ coljustreason +","+coljustdesc+" from "+ justTable, new String [] {});
	   
	   try {
           
		      while(cur.moveToNext())
		      {
		    	  hr_justification auxNewJust = new hr_justification();
		    	  auxNewJust.setCodJust(cur.getInt(0));
		    	  auxNewJust.setDate(cur.getString(1));
		    	  auxNewJust.setReason(cur.getString(2));
		    	  auxNewJust.setDescription(cur.getString(3));
		    	  
		    	  JustList.add(auxNewJust);
		      }
		      } finally {
		    	  cur.close();
		      }
	   
	   
	   CloseDatabase();
	   
	   return JustList;
	}
	
	public void addJust(hr_justification newJust)
	{		
		if(newJust.getUser() != null)
		{
			 OpenDatabase();
			 ContentValues cv = new ContentValues();		   
			   	     		   
		     cv.put(coljustDate, newJust.getDate());
		     cv.put(coljustreason, newJust.getReason());
		     cv.put(coljustdesc,newJust.getDescription());
			 cv.put(coljustUser, newJust.getUser().getCodUser());  
			 db.insert(justTable, coljustID, cv);
			 
			 CloseDatabase();
		}
	}

	//Methods for operations with Table holiday
	public ArrayList<hr_vacation> getAllholiday()
	{
	   ArrayList<hr_vacation> HolList = new ArrayList<hr_vacation>();	
	   OpenDatabase();
	   
       Cursor cur = db.rawQuery("SELECT "+colholidayID+", "+colholidayDate+","+ colholidayPeriod +" from "+ holidayTable, new String [] {});
	   
	   try {
           
		      while(cur.moveToNext())
		      {
		    	  hr_vacation auxNewHoliday = new hr_vacation();
		    	  auxNewHoliday.setCodhol(cur.getInt(0));
		    	  auxNewHoliday.setDate(cur.getString(1));
		    	  auxNewHoliday.setPeriod(cur.getString(2));
		    	  
		    	  
		    	  HolList.add(auxNewHoliday);
		      }
		      } finally {
		    	  cur.close();
		      }
	   
	   
	   CloseDatabase();
	   
	   return HolList;
	}
	
	public void addHoliday(hr_vacation newHoliday)
	{	
		if(newHoliday.getUser() != null)
		 {
			 OpenDatabase();
			
			 ContentValues cv = new ContentValues();		            		
		     cv.put(colholidayDate, newHoliday.getDate());
		     cv.put(colholidayPeriod, newHoliday.getPeriod());
		     cv.put(colholidayUser, newHoliday.getUser().getCodUser());		   
			 db.insert(holidayTable, colholidayID, cv);
			 
			 CloseDatabase();
		 }
	}
	
	public void clearJusts() {
		OpenDatabase();
		
		db.execSQL("DELETE FROM "+justTable);
		
		CloseDatabase();
	}
	
	public void clearHolidays() {
		OpenDatabase();
		
		db.execSQL("DELETE FROM "+holidayTable);
		
		CloseDatabase();
	}
	
	public void clearHolidays(int codHoliday) {
		OpenDatabase();
		
		db.execSQL("DELETE FROM "+holidayTable+" WHERE "+colholidayID+"="+codHoliday);
		
		CloseDatabase();
	}
	
	
	
}
