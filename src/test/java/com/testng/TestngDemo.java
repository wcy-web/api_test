package com.testng;

import org.testng.annotations.*;

/**
 * @author 向阳
 * @date 2020/6/2-23:40
 */
public class TestngDemo {
    //用代码说明 @BeforeSuite  @BeforeTest @BeforeClass @BeforeMethod 执行顺序
    @Test
    public void method(){
        System.out.println("执行方法。。。");
    }
    @BeforeSuite
    public void beforeSuite(){
        System.out.println("初始化套件。。。");
    }
    @BeforeTest
    public void beforeTest(){
        System.out.println("初始化模块。。。");
    }
    @BeforeClass
    public void beforeClass(){
        System.out.println("初始化类。。。");
    }
    @BeforeMethod
    public void beforeMethod(){
        System.out.println("初始化方法。。。");
    }

    /**
     * @author 向阳
     * @date 2020/6/3-13:31
     */
    public static class TestngDemo2 {
        @BeforeMethod
        public void beforeMethod(){
            System.out.println("初始方法二。。。");
        }

        @Test(dataProvider = "datas")
        public void method(String name,String age){
            System.out.println(name+"==="+age);
        }
        @DataProvider
        public Object[][] datas(){
            Object[][] datas={{"张三","20"},{"李四","22"}};
            return datas;
        }
    }
}
