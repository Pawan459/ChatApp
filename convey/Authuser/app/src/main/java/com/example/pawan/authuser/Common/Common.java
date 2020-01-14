package com.example.pawan.authuser.Common;

import com.example.pawan.authuser.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class Common {

    //ADDING A GLOBAL VARIABLE TO COMMON CLASS
    public static final String DIALOG_EXTRA="Dialogs";

    public static  final String UPDATE_DIALOG_EXTRA="ChatDialogs";
    public static  final String UPDATE_MODE="Mode";

    public static  final String UPDATE_ADD_MODE="add";
    public static  final String UPDATE_REMOVE_MODE="remove";

    //Dialog Avatar
    public static final  int SELECT_PICTURE=7171;


    public  static String createChatDialogName(List<Integer> qbUsers)
    {
        List<QBUser> qbUsers1 = QBUsersHolder.getInstance().getUsersByIds(qbUsers);
        StringBuilder name = new StringBuilder();

        //DIALOG NAME WILL BE NAME OF ALL USERS IN LIST
        for(QBUser user:qbUsers1)
            name.append(user.getFullName()).append(" ");
        if(name.length()>30)
            name = name.replace(30,name.length()-1,"...");
        return  name.toString();
    }

    //to check whether the entered string is empty or not
    public  static boolean isNullOrEmptyString(String context)
    {
        return (context != null && !context.trim().isEmpty()?false:true);
    }
}
