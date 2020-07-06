package com.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.constants.Constants;
import com.pojo.CaseInfo;
import com.pojo.WriteBackInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;



/**
 * @author 向阳
 * @date 2020/6/22-18:18
 */
public class BaseCase {
    Logger logger= Logger.getLogger(BaseCase.class);
    //定义成员变量，方便方法调用
    public int startSheet;

    //默认请求头只需要加载一次，所以使用@BeforeSuite
    @BeforeSuite
    //设置默认请求头
    public void init(){
        Constants.HEADERS.put("X-Lemonban-Media-Type", "lemonban.v2");
        logger.info("============init================");
    }

    //从testng.xml中获取参数：startSheet
    @BeforeClass
    @Parameters({"startSheet"})
    public void getSheetIndex(int startSheet){
        this.startSheet=startSheet;
    }

    @AfterSuite
    //调用批量回写
    public void finish(){
        ExcelUtils.writeExcel();
        logger.info("============finish================");
    }

    /**
     * 添加回写对象到集合中
     * @param startSheet    sheet编号
     * @param rowNum        行号
     * @param cellNum       列号
     * @param content       回写内容
     */
    public void addWriteBackData(int startSheet,int rowNum,int cellNum,String content) {
        //创建回写对象
        WriteBackInfo writeBackInfo=new WriteBackInfo(startSheet,rowNum,cellNum,content);
        //将回写对象添加到回写集合中
        ExcelUtils.list.add(writeBackInfo);
    }

    /**
     * 断言：环Map中内容，通过Key得到预期结果,与Body中的根据表达式得到的实际结果进行对比，返回测试结果
     * @param expectResultContent  excel表预期结果列对应的内容
     * @param body      响应体
     * @return          测试结果
     */
    public boolean assertTest(String expectResultContent, String body) {

        //预期结果列内容转出Map对象
        Map<String,Object> map = JSONObject.parseObject(expectResultContent, Map.class);
        Set<String> keySet = map.keySet();
        boolean falg=false;
        //循环Map中内容，通过Key得到预期结果,与Body中的根据表达式得到的实际结果进行对比
        for (String key : keySet) {
            //预期结果
            Object expectResult=map.get(key);
            logger.info(expectResult);
            //实际结果
            Object realResult = JSONPath.read(body, key);
            logger.info(realResult);
            if(expectResult==null ||realResult==null){
//                runResult="预期结果或实际结果为空";
                return falg;
            }
            //对比预期结果、实际结果
            if(expectResult.equals(realResult)){
                falg=true;
            }else{
                falg=false;
            }
        }
        return falg;
    }

    /**
     * 参数化替换
     * @param caseInfo
     */
    public  void newParams(CaseInfo caseInfo) {
        //前提：把excel表中需要参数化的内容用占位符替换；在properties文件中设置占位符=值
        //1、读取properties文件中的内容，放到环境变量ENV中
        Properties properties=new Properties();
        FileInputStream fis=null;
        try {
            fis=new FileInputStream("E:\\javatest\\java_api_test\\java_test_v3\\src\\test\\resources\\data.properties");
            //加载properties文件中的内容
            properties.load(fis);
            //把properties文件中的内容放到作为环境变量的ENV中（需要把properties强转成Map）
            Authentication.ENV.putAll((Map) properties);
            logger.info("Authentication.ENV="+Authentication.ENV);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            ExcelUtils.close(fis);
        }
        //2、从ENV中循环
        Set<String> keySet = Authentication.ENV.keySet();
        for (String key : keySet) {
            //根据key，得到value值
            String value = Authentication.ENV.get(key).toString();
            //params参数化替换
            if(StringUtils.isNoneBlank(caseInfo.getParams())){
                //参数中的占位符替换成值
                String params=caseInfo.getParams().replace(key,value);
                //把params新值赋给caseinfo
                caseInfo.setParams(params);
            }
            //sql参数化替换
            if(StringUtils.isNoneBlank(caseInfo.getSql())){
                String sql=caseInfo.getSql().replace(key,value);
                caseInfo.setSql(sql);
            }
            //预期结果参数化替换
            if (StringUtils.isNoneBlank(caseInfo.getExpectResult())) {
                String expect=caseInfo.getExpectResult().replace(key,value);
                caseInfo.setExpectResult(expect);
            }

        }
    }


}
