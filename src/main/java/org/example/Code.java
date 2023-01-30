package org.example;
import redis.clients.jedis.Jedis;

import java.util.Random;

public class Code {

    private int loginCount=0;
    /**
     * 隨機產生6位隨機碼
     */
    public String getCode(){

        StringBuilder code= new StringBuilder();
        Random random=new Random();

        for(int i=0;i<6;i++){
           int rand=random.nextInt(10);
           code.append(rand);
        }
        return code.toString();
    }

    /**
     * 驗證碼暫時存到redis並設置緩存時間
     */
    public void setCode(String phone){

        String countKey=phone+"count";//存在redis紀錄次數
        String codeKey=phone+"code";//存在Redis的驗證碼
        try(Jedis jedis=new Jedis("localhost",6379)){

           String count= jedis.get(countKey);
           if(count==null){
               jedis.setex(countKey,60*60*24,String.valueOf(1));
           }
           else if(Integer.parseInt(count)<=2){
               jedis.incr(codeKey);
           }
           else if(Integer.parseInt(count)>2){
               System.out.println("驗證碼發送次數超過3次喽");

           }
           String code=getCode();
           jedis.setex(codeKey,60*5,code);//儲存驗證碼並設立過期時間 5分鐘
        }

        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 驗證碼校驗
     */
    public void verifyCode(String phone,String code){

        try(Jedis jedis=new Jedis("localhost",6379)){
         String redisCode= jedis.get(phone+"code");
         if(loginCount>3){
             System.out.println("登入錯誤超過3次");
         }
         if(redisCode.equals(code)){
             System.out.println("驗證碼正確");
             loginCount=0;
         }
         else{
             System.out.println("驗證碼錯誤");
             loginCount++;
         }
        }
        catch (Exception e){
            System.out.println("過期重新傳送驗證碼");
        }


    }
}

