package com.yiyo.imagesqlite;

import io.realm.RealmObject;

/**
 * Created by yiyo on 27/08/15.
 */
public class Imagenes extends RealmObject {

    String Id_mail;





    public String getId_mail() {
        return Id_mail;
    }

    public void setId_mail(String id_mail) {
        Id_mail = id_mail;
    }
}
