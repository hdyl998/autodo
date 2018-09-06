package com.autodo;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void useAppContext() throws Exception {
        String strMoney = "共2元";
        strMoney = strMoney.substring(1, strMoney.length() - 1);

        int money = Integer.parseInt(strMoney);

        System.out.println("all money" + money);


//        AccessibilityNodeInfo nodeInfo = Tools.findFirstById(root, "com.other:id/detail");
        String strInfo ="本次预计出票共4455张155注2倍";
        int indexGong = strInfo.indexOf("共");
        int indexZhang = strInfo.indexOf("张");
        int indexZhu = strInfo.indexOf("注");
        int indexBei = strInfo.indexOf("倍");
        int zhang = Integer.parseInt(strInfo.substring(indexGong + 1, indexZhang));
        int zhu = Integer.parseInt(strInfo.substring(indexZhang + 1, indexZhu));
        int bei = Integer.parseInt(strInfo.substring(indexZhu + 1, indexBei));

        System.out.println(indexBei);

        System.out.println(strInfo.length());


        System.out.println(zhang);

        System.out.println(zhu);

        System.out.println(bei);
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