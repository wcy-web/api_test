package com.cases;

import com.constants.Constants;
import com.pojo.CaseInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import com.utils.HttpUtils;
import com.utils.SQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.poi.util.StringUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author 向阳
 * @date 2020/6/17-16:51
 */
public class Register extends BaseCase{

    @Test(dataProvider = "datas")
    public void register(CaseInfo caseInfo){
        //参数化替换
        newParams(caseInfo);
        //根据请求方式json/form，添加请求头中的请求方式
        Authentication.addHeader(caseInfo.getContentType());
        //数据库前置查询
        Object beforeResult=SQLUtils.getData(caseInfo.getSql());
        //执行请求
        HttpResponse response = HttpUtils.call(caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType(), caseInfo.getContentType(),Constants.HEADERS);
        //获取响应体
        String body = HttpUtils.printResponse(response);
        //添加响应体到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(),Constants.REAL_RESULT_CELL_NUM,body);
        //响应断言
        boolean b1 = assertTest(caseInfo.getExpectResult(), body);
        //数据库后置查询
        Object afterResult=SQLUtils.getData(caseInfo.getSql());
        //数据库断言
        boolean b2 = sqlAssert(beforeResult, afterResult);
        //响应断言和数据库断言都为真，则测试通过，反之测试失败
        String s = b1 && b2 ? "通过" : "失败";
        //添加测试结果到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(),Constants.TEST_RESULT,s);
        //报表断言
        Assert.assertEquals(s,"通过");
    }

    @DataProvider
    public Object[] datas(){
        Object[] datas = ExcelUtils.getDatas(startSheet, CaseInfo.class);
        return datas;
    }

    /**
     * 数据库断言
     * @param beforeResult  数据库前置查询结果
     * @param afterResult   数据库后置查询结果
     */
    public boolean sqlAssert(Object beforeResult, Object afterResult) {
        boolean flag=false;
        if(beforeResult==null||afterResult==null){
            System.out.println("sql为空，不需要断言");
        }else{
            //beforeResult的实际类型是Long类型，直接强转，方便进行比较
            Long result1=  (Long)beforeResult;
            Long result2=  (Long)afterResult;
            System.out.println(result1);
            System.out.println(result2);
//            注册前数据库记录为0，且注册后数据库记录为1，断言成功
            if(result1==0 && result2==1){
                flag=true;
                System.out.println("断言成功");
            }else {
                System.out.println("断言失败");
            }
        }
        return flag;
    }


}
