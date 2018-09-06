package com.autodo;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("com.example.test1.fragmentadd", appContext.getPackageName());


        Subject zhangsan = new RealSubject();
        Subject proxy = new DynamicProxy(zhangsan).getProxy();
        int money = proxy.giveMeMyMoney(1000);
        System.out.println(money);


    }


    /**
     * Created by cheng.xi on 2017-04-12 19:58.
     * 代理模式的主题类，这里代表的是欠钱的人
     */
    public interface Subject {
        /**
         * 还给我的钱
         *
         * @param moneyCount 欠钱数
         * @return 还给我的钱数
         */
        int giveMeMyMoney(int moneyCount);
    }

    /**
     * Created by cheng.xi on 2017-04-12 19:58.
     * 真实主题，这里代表的是欠我钱的人
     */
    public class RealSubject implements Subject {
        @Override
        public int giveMeMyMoney(int moneyCount) {
            return moneyCount;
        }
    }

    /**
     * Created by cheng.xi on 2017-04-12 19:59.
     * 代理
     */
    public class DynamicProxy implements InvocationHandler {

        private Object target;

        public DynamicProxy(Object target) {
            this.target = target;
        }

        public <T> T getProxy() {
            return (T) Proxy.newProxyInstance(
                    target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("办事之前先收取点费用");
            System.out.println("开始办事");
            Object result = method.invoke(target, args);
            System.out.println("办完了");
            return result;
        }
    }


}
