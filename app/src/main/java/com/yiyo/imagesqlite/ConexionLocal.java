package com.yiyo.imagesqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ConexionLocal extends SQLiteOpenHelper {

	private static String UserDivce = "create table usuario_divice(\n" +
									  "id_mail text primary key, \n" +
			 						  "alias text, \n" +
									  "key text, \n" +
									  "passwd text\n" +
									  ");";

	private static String Contactos = 	"create table contactos(\n" +
										"id_mail text, \n" +
										"alias text, \n" +
										"key text\n" +
										");";

	private static String r_usr_conver = 	"create table r_usr_conver(\n" +
											"id_conversacion serial primary key,\n" +
											"tu text default 'Tu',\n" +
											"amigo text references contactos not  null\n" +
											");";


	
	public ConexionLocal (Context ctx){
				  //Base datos
		super(ctx, "imagensafe2.db", null,2);
		}
		
	@Override
	public void onCreate(SQLiteDatabase db){
			//Crear Tablas		
		db.execSQL(UserDivce);
		db.execSQL(Contactos);
		db.execSQL(r_usr_conver);
			//Precargar Tablas
		}
		
		@Override
	public void onUpgrade(SQLiteDatabase db, int version_ant, int version_nue)
		{
			db.execSQL("DROP TABLE IF EXISTS usuario_divice");
			db.execSQL("DROP TABLE IF EXISTS contactos");
			db.execSQL("DROP TABLE IF EXISTS r_usr_conver");

			db.execSQL(UserDivce);
			db.execSQL(Contactos);
			db.execSQL(r_usr_conver);

		}
		
		public void Ejecutar(ItemContacto contacto){
			/*ContentValues cv = new ContentValues(	);
			cv.put("id_mail",contacto.getID_MAIL());
			cv.put("alias",contacto.getALIAS());
			cv.put("img", contacto.getIMG());
			this.getWritableDatabase().insert("contactos",null,cv);*/

			SQLiteStatement stmt = this.getWritableDatabase().compileStatement("INSERT INTO contactos values(?,?,?)");
			stmt.bindString(1,contacto.getID_MAIL());
			stmt.bindString(2,contacto.getALIAS());
			stmt.bindString(3, contacto.getIMG());
			stmt.execute();

			stmt.close();
			}
		
	public Cursor Consultar(String sql){
			Log.i("Select",sql);
			Cursor c = this.getReadableDatabase().rawQuery(sql,null);
		return c;		
		}
		
	public void abrir(){
			this.getWritableDatabase();		
		}
		
	public SQLiteDatabase getSQLiteDatabase( ){
		return this.getWritableDatabase();
	}
	public void cerrar(){
			this.close();
			}
		}