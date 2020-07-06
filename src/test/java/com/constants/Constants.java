package com.constants;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 向阳
 * @date 2020/6/17-16:43
 */
public class Constants {
    //存放测试用例的EXCEL表路径
    public static final String EXCEL_PATH=System.getProperty("user.dir")+"/src/test/resources/cases_v3.xlsx";
//    public static final String EXCEL_PATH=Constants.class.getClassLoader().getResource("cases_v3.xlsx").getPath();
//    public final File file=new File(this.getClass().getResource("cases_v3.xlsx").getPath());
    public static final InputStream FILE=Constants.class.getClassLoader().getResourceAsStream("cases_v3.xlsx");

    //默认请求头
    public static final Map<String,String> HEADERS=new HashMap<String,String>();
    //将测试实际运行结果回写到excel表中的第8列
    public static final int REAL_RESULT_CELL_NUM=8;
    //将测试结果回写到excel表中的第9列
    public static final int TEST_RESULT=9;


}
