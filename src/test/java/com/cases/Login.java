package com.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.constants.Constants;
import com.pojo.CaseInfo;
import com.pojo.WriteBackInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import com.utils.HttpUtils;
import com.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author 向阳
 * @date 2020/6/17-16:57
 */
public class Login extends BaseCase {

    @Test(dataProvider = "datas")
    public void login(CaseInfo caseInfo){
        //参数化替换
        newParams(caseInfo);

        //根据请求方式json/form，添加请求头中的请求方式
        Authentication.addHeader(caseInfo.getContentType());
        //数据库前置查询结果
        QueryRunner query=new QueryRunner();
//        query.query(JDBCUtils.getConnection(),caseInfo.getSql(),);

        HttpResponse response = HttpUtils.call(caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType(), caseInfo.getContentType(),Constants.HEADERS);


        String body = HttpUtils.printResponse(response);
        //从响应体中获取到token，把token存到用作鉴权的map中
        Authentication.json2ENV(body,"$.data.token_info.token","${token}");
        //从响应体中获取到member_id，把token存到用作鉴权的map中

            Authentication.json2ENV(body, "$.data.id", "${member_id}");

        //添加响应体到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(),Constants.REAL_RESULT_CELL_NUM,body);
        //响应断言
        boolean b = assertTest(caseInfo.getExpectResult(), body);
        String s = b ? "通过" : "失败";
        //添加测试结果到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(),Constants.TEST_RESULT,s);
        //报表断言
        Assert.assertEquals(s,"通过");
        System.out.println("git");

    }

    @DataProvider
    public Object[] datas(){
        Object[] datas = ExcelUtils.getDatas(startSheet, CaseInfo.class);
        return datas;
    }

}
