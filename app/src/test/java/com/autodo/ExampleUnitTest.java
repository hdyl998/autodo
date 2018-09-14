package com.autodo;

import com.autodo.lottery.OrderItem;
import com.autodo.lottery.ConstDatas;
import com.autodo.mainjob.exception.TimeOutException;
import com.autodo.socket.MapCreator;

import org.junit.Test;

import java.text.SimpleDateFormat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void useAppContext() throws Exception {

        String url = "alipays://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2Fa6x05728z5wdnm1lsuzzj0d";
        if (!url.startsWith("http") && !url.contains("://")) {
            url = "http://" + url;
        }
        System.out.println(url);
        ///
//        SimpleDateFormat dateFormat = new SimpleDateFormat("中国人民yyyy_MM_dd_hh_mm_ss");
//        String strDate = dateFormat.format(new java.util.Date());
//        System.out.println(strDate);

//        String ttt = "8747|2001|1[3#1.52,1#3.40]/8747|2001|2[3#2.85]";
//        System.out.println(ttt.split("\\/")[0]);


//        int total = ConstDatas.strings.length;
//
//
//        String string = "8766|3001|1[3#2.22,1#3.20,0#2.73]/8766|3001|2[3#4.90,1#3.80,0#1.51]/8767|3002|1[3#2.28,1#3.00,0#2.80]/8767|3002|2[3#5.30,1#3.85,0#1.47]/8767|3002|4[1:0#6.50,2:0#10.00,2:1#8.00,3:0#21.00,3:1#19.00,3:2#28.00,4:0#50.00,4:1#50.00,4:2#80.00,5:0#150.00,5:1#150.00,5:2#250.00,胜其它#80.00,0:0#8.00,1:1#5.60,2:2#12.50,3:3#60.00,平其它#400.00,0:1#7.50,0:2#12.50,1:2#8.50,0:3#29.00,1:3#24.00,2:3#29.00,0:4#80.00,1:4#70.00,2:4#100.00,0:5#300.00,1:5#250.00,2:5#300.00,负其它#120.00]/8767|3002|3[0#8.00,1#3.80,2#3.05,3#3.70,4#6.00,5#12.00,6#22.00,7#34.00]/8767|3002|5[3-3#3.70,3-1#12.00,3-0#27.00,1-3#5.30,1-1#4.60,1-0#5.80,0-3#24.00,0-1#12.00,0-0#4.60]";
//
//        OrderItem item = new OrderItem(string);
//
//        System.out.println(item.toString());


//        String strMoney = "共2元";
//        strMoney = strMoney.substring(1, strMoney.length() - 1);
//
//        int money = Integer.parseInt(strMoney);
//
//        System.out.println("all money" + money);
//
//
////        AccessibilityNodeInfo nodeInfo = Tools.findFirstById(root, "com.other:id/detail");
//        String strInfo ="本次预计出票共4455张155注2倍";
//        int indexGong = strInfo.indexOf("共");
//        int indexZhang = strInfo.indexOf("张");
//        int indexZhu = strInfo.indexOf("注");
//        int indexBei = strInfo.indexOf("倍");
//        int zhang = Integer.parseInt(strInfo.substring(indexGong + 1, indexZhang));
//        int zhu = Integer.parseInt(strInfo.substring(indexZhang + 1, indexZhu));
//        int bei = Integer.parseInt(strInfo.substring(indexZhu + 1, indexBei));
//
//        System.out.println(indexBei);
//
//        System.out.println(strInfo.length());
//
//
//        System.out.println(zhang);
//
//        System.out.println(zhu);
//
//        System.out.println(bei);
//        File file=new File("f://test.txt");
//
//        FileReader fileReader= new FileReader(file);
//        BufferedReader reader=new BufferedReader(fileReader);
//
////        StringBuilder stringBuilder=new StringBuilder();
//
//        String ttt=reader.readLine();
//        while (ttt!=null){
//            System.out.println(ttt.substring(4));
//            ttt=reader.readLine();
//        }


//        float aa=3.06f;
//        float bb=12f;
//
//        float cc=3.1f;
//        float dd=50f;
//
//
//        float reslut=aa*(cc+dd)+bb*(cc+dd);
//        System.out.println(reslut*6);
//        try {
//            throw new RuntimeException("eeeeaaa");
//        } catch (Throwable t) {
//            RuntimeException exception = new RuntimeException("错了");
//            exception.initCause(t);
//            exception.printStackTrace();
//            StackTraceElement[] messages = exception.getStackTrace();
//            int length = messages.length;
//            for (int i = 0; i < length; i++) {
//                System.out.println("" + messages[i].toString());
//            }
//        }
    }


}