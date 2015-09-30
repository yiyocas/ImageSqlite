package com.yiyo.imagesqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    int SELECT_PICTURE = 1;
    String selectedImagePath;

    ImageView img;
    Button bn1,bn2;
    EditText text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imageView);
        bn1 = (Button) findViewById(R.id.bn1);
        bn2 = (Button) findViewById(R.id.bn2);
        text= (EditText) findViewById(R.id.text);


        bn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"CampoVacio",Toast.LENGTH_LONG).show();
                }else{

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);

                }
            }
        });



        bn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(text.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,"CampoVacio",Toast.LENGTH_LONG).show();
                }else {

                    ConexionLocal conn = new ConexionLocal(getApplicationContext());

                    conn.abrir();
                    Cursor c = conn.Consultar("select * from contactos where alias='"+text.getText().toString()+"'");


                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        Log.i("ID_MAIL", c.getString(c.getColumnIndex("id_mail")));
                        Log.i("ALIAS", c.getString(c.getColumnIndex("alias")));
                        Log.i("KEY", c.getString(c.getColumnIndex("key")));

                       /* Realm realm = Realm.getInstance(getApplicationContext());
                        Imagenes imgs = realm.where(Imagenes.class).equalTo("Id_mail",c.getString(c.getColumnIndex("id_mail"))).findFirst();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imgs.getImagen(), 0, imgs.getImagen().length);
                        */
                    }
                    conn.cerrar();

                }
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                Log.i("Path", selectedImagePath);

                try {
                    FileInputStream fis = new FileInputStream(new File(selectedImagePath));

                    byte[] imgbyte2 = new byte[fis.available()];

                    fis.read(imgbyte2);

                    ConexionLocal conn = new ConexionLocal(getApplicationContext());

                    conn.abrir();


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imgbyte2,0,imgbyte2.length);


                    ItemContacto contacto = new ItemContacto();

                    contacto.setALIAS(text.getText().toString());
                    contacto.setID_MAIL("YIYO@YIYO.COM.MX");
                    contacto.setIMG("UNKNOWN");

                    conn.Ejecutar(contacto);
                    conn.cerrar();

                    Realm realm = Realm.getInstance(getApplicationContext());
                    realm.beginTransaction();
                    Imagenes imgs = realm.createObject(Imagenes.class);
                    //imgs.setImagen(imgbyte2);
                    imgs.setId_mail(contacto.getID_MAIL());
                    realm.commitTransaction();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }
}
