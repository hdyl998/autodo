package com.example.test1.fragmentadd;

import com.example.test1.fragmentadd.logic.SelectItem;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void useAppContext() throws Exception {


        System.out.println(new SelectItem().toString());
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