package com.cases;

import com.alibaba.fastjson.JSONPath;
import com.constants.Constants;
import com.pojo.CaseInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import com.utils.HttpUtils;
import com.utils.SQLUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 向阳
 * @date 2020/6/17-16:57
 */
public class Recharge extends BaseCase{

    @Test(dataProvider = "datas")
    public void recharge(CaseInfo caseInfo) throws IOException {
        //参数化替换
        newParams(caseInfo);
        //请求头
        Map<String, String> headers = getHeader(caseInfo);
        //数据库前置查询
        Object beforeResult = SQLUtils.getData(caseInfo.getSql());
        //执行请求
        HttpResponse response = HttpUtils.call(caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType(), caseInfo.getContentType(), headers);
        //获得响应体
        String body = HttpUtils.printResponse(response);
        //添加响应体到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(),Constants.REAL_RESULT_CELL_NUM,body);
        //响应断言
        boolean b1 = assertTest(caseInfo.getExpectResult(), body);

        //数据库后置查询
        Object afterResult = SQLUtils.getData(caseInfo.getSql());
        //数据库断言
        boolean b2 = sqlAssert(caseInfo, beforeResult, afterResult);
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
     * 获取请求头
     * @param caseInfo
     * @return
     */
    public Map<String, String> getHeader(CaseInfo caseInfo) {
        //取出token
        Object token = Authentication.ENV.get("${token}");
        //重新创建请求头，把鉴权添加到请求头中
        Map<String,String> headers=new HashMap<String, String>();
        headers.put("Authorization","Bearer "+token);
        Authentication.addHeader(caseInfo.getContentType());
        //把默认请求头也添加到定义的请求头中（因为默认的请求头只有两个值，如果直接往默认请求头中put值，
        // 那么默认请求头就变成了三个值，之后的请求头也是三个值，所以如果请求头多与默认请求头，则重新创建请求头，
        // 单独添加该请求特有的请求头，然后再把默认请求头放进来即可）
        headers.putAll(Constants.HEADERS);
        return headers;
    }

    /**
     * 数据库断言
     * @param caseInfo
     * @param beforeResult  数据库前置查询结果
     * @param afterResult   数据库后置查询结果
     */
    public boolean sqlAssert(CaseInfo caseInfo, Object beforeResult, Object afterResult) {
        boolean flag=false;
        if(beforeResult==null || afterResult ==null){
            System.out.println("sql为空，不需要断言");
            return flag;
        }else{
            BigDecimal result1=(BigDecimal)beforeResult;
            BigDecimal result2=(BigDecimal)afterResult;
            //充值后金额 - 充值前金额
            BigDecimal acutalResult=result2.subtract(result1);
            //通过JSONPath取出参数中的充值金额
            Object expect = JSONPath.read(caseInfo.getParams(), "$.amount");
            //转出BigDecimal格式（BigDecimal接收String类型参数；String类型不能强转成BigDecimal格式，只能封装）
            BigDecimal expectResult=new BigDecimal(expect.toString());
            System.out.println(expectResult);
            System.out.println(acutalResult);
            //compareTo方法比较两个BigDecimal类型的值，相等则返回0
            if(acutalResult.compareTo(expectResult)==0){
                System.out.println("断言成功");
                flag=true;
            }else{
                System.out.println("断言失败");
            }
        }
        return flag;
    }
}
