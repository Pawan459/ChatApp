package com.Model;

public class Decryption {


    private String message;

    public Decryption() {
    }

    /*public Encryption(String message) {
        this.message = message;
    }*/

    public String decrypt(String message)
    {
        int k=0;
        String final_message="";
        for(int i=0;i<message.length()/2;i++)
        {
            int var=message.charAt(i);
            var=var-5-k%message.length();

            final_message= (String) final_message.concat(String.valueOf((char)var));
            k++;

        }
        return  final_message;
    }
}
