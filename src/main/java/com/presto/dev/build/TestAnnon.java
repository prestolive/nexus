package com.presto.dev.build;

import com.presto.web.test.TestCtrl;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2017/3/22.
 */
public class TestAnnon {

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException {
        TestAnnon t =new TestAnnon();
        t.test();

    }

    private void test(){

        Method[]  methods= TestCtrl.class.getMethods();
        for(Method method:methods){
            ActionBuildField annon =method.getAnnotation(ActionBuildField.class);
            if(annon!=null){
                System.out.println(annon.module());
                System.out.println(annon.app());
                System.out.println(annon.group());
                System.out.println(annon.action());
                System.out.println(annon.name());
                System.out.println(annon.desc());
            }

        }
    }
}
