package com.cases;

import com.constants.Constants;
import com.pojo.CaseInfo;
import com.utils.Authentication;
import com.utils.ExcelUtils;
import com.utils.HttpUtils;
import com.utils.SQLUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 向阳
 * @date 2020/7/1-17:12
 */
public class AuditLoan extends BaseCase{
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
        addWriteBackData(startSheet,caseInfo.getId(), Constants.REAL_RESULT_CELL_NUM,body);
        //测试结果
        boolean b1 = assertTest(caseInfo.getExpectResult(), body);

        //数据库后置查询
        Object afterResult = SQLUtils.getData(caseInfo.getSql());
        //数据库断言
//        sqlAssert(caseInfo, beforeResult, afterResult);
        String s = b1 ? "通过" : "失败";
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
}
