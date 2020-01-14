package com.Model;

import android.widget.Toast;

public class Encryption {


    public Encryption() {
    }

    /*public Encryption(String message) {
        this.message = message;
    }*/

   public String encypt(String message, char sender)
   {
       int k=0;
      String final_message="";
       for(int i=0;i<message.length();i++)
       {
           int var=message.charAt(i);
           var=var+5+k%message.length();

           final_message= (String) final_message.concat(String.valueOf((char)var));
           k++;

       }

       //stringBuffer=final_message;
       StringBuilder stringBuilder=new StringBuilder(final_message);
       String sss=stringBuilder.reverse().toString();
       final_message=final_message.concat(sss);
       return  final_message;
   }
}
