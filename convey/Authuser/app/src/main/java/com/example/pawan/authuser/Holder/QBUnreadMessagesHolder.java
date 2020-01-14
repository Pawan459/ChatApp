package com.example.pawan.authuser.Holder;

import android.os.Bundle;

public class QBUnreadMessagesHolder {
    private static  QBUnreadMessagesHolder instance;

    private Bundle bundle;

    public static  synchronized QBUnreadMessagesHolder getInstance(){
        QBUnreadMessagesHolder qbUnreadMessagesHolder;
        synchronized (QBUnreadMessagesHolder.class)
        {
            if(instance==null)
                instance = new QBUnreadMessagesHolder();
            qbUnreadMessagesHolder = instance;
        }
        return  qbUnreadMessagesHolder;
    }

    private QBUnreadMessagesHolder()
    {
         bundle = new Bundle();
    }

    public void  setBundle(Bundle bundle)
    {
        this.bundle=bundle;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public  int getUnreadMessageByDialogId(String id)
    {
        return this.bundle.getInt(id);
    }
}
