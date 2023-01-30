package org.example;

public class Main {
    public static void main(String[] args) {

        Code code=new Code();
        //code.setCode("00000001");
        try{
            code.verifyCode("00000001",code.getCode());//getCode隨便給
        }
        catch (Exception e){
           e.printStackTrace();

        }
        finally {
            code.setCode("00000001");
        }

    }
}