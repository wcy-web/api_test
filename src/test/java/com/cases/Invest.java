package com.cases;

import com.alibaba.fastjson.JSONPath;
import com.constants.Constants;
import com.pojo.CaseInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import com.utils.HttpUtils;
import com.utils.SQLUtils;
import org.apache.http.HttpResponse;

import org.apache.poi.hpsf.Decimal;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 向阳
 * @date 2020/7/1-17:12
 */
public class Invest extends BaseCase{
    @Test(dataProvider = "datas")
    public void recharge(CaseInfo caseInfo) throws IOException {
        //参数化替换
        newParams(caseInfo);
        //请求头
        Map<String, String> headers = getHeader(caseInfo);
        //数据库前置查询
       Map<String,Object> map1= SQLUtils.getDataAsMap(caseInfo.getSql());
        //执行请求
        HttpResponse response = HttpUtils.call(caseInfo.getUrl(), caseInfo.getParams(), caseInfo.getType(), caseInfo.getContentType(), headers);
        //获得响应体
        String body = HttpUtils.printResponse(response);
        //添加响应体到回写集合中
        addWriteBackData(startSheet,caseInfo.getId(), Constants.REAL_RESULT_CELL_NUM,body);
        //测试结果
        boolean b1 = assertTest(caseInfo.getExpectResult(), body);
        //数据库后置查询
        Map<String,Object> map2=SQLUtils.getDataAsMap(caseInfo.getSql());
        //数据库断言
        boolean b2 = sqlAssert(caseInfo, map1, map2);
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

    public boolean sqlAssert(CaseInfo caseInfo,Map<String,Object> map1,Map<String,Object> map2){
        boolean flag=false;
        //投资前的用户账户余额
        BigDecimal leave_amount1 = (BigDecimal)map1.get("leave_amount");
        //投资前的用户投资金额
        BigDecimal amount1= (BigDecimal)map1.get("total_amount");
        //投资后的用户账户余额
        BigDecimal leave_amount2 = (BigDecimal)map2.get("leave_amount");
        //投资后的用户投资金额
        BigDecimal amount2= (BigDecimal)map2.get("total_amount");
        Object invest = JSONPath.read(caseInfo.getParams(), "$.amount");
        //投资金额
        BigDecimal investAmount=new BigDecimal(invest.toString());
        //投资的账户余额变化
        BigDecimal invest1 = leave_amount1.subtract(leave_amount2);
        //投资金额的变化
        BigDecimal invest2 = amount2.subtract(amount1);
        if(invest1.compareTo(investAmount)==0 && invest2.compareTo(investAmount)==0){
            System.out.println("数据库断言成功");
            flag=true;
        }else{
            System.out.println("数据库断言失败");
        }
        return flag;
    }
}
