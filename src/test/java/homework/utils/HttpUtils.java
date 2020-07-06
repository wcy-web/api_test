package homework.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 发送HTTP请求的工具类
 * @author 向阳
 * @date 2020/6/10-20:43
 */
public class HttpUtils {

    /**
     * 封装call方法支持post、get、patch请求，支持json、form参数
     */
    public static HttpResponse call(String type,String contentType,String url,String params){
        if("json".equals(contentType)) {
            if ("post".equals(type)) {
                return post(url, params);
            } else if ("get".equals(type)) {
                return get(url);
            } else if ("patch".equals(type)) {
                return patch(url, params);
            } else {
                System.out.println("没有执行http请求的方法！");
            }
        }else if("form".equals(contentType)){
            params = josn2KeyValue(params);
            return formPost(url, params);
        }
        return null;
    }
    /**
     * 发送get请求
     */
    public static HttpResponse get(String url){
        try {
            //1、创建get请求，带请求方式、url
            HttpGet get = new HttpGet(url);
            //2、设置请求头、请求体
            get.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
            get.setHeader("Content-Type", "application/json");
            //3、创建请求客户端
            HttpClient client = HttpClients.createDefault();
            //4、发送请求，并接收响应
            HttpResponse response = client.execute(get);
            //5、获得响应头、响应体、响应code
            printReponse(response);
            return response;
        }catch (Exception e){
            e.getStackTrace();
        }
        return  null;
    }

    /**
     * 发送post请求
     */
    public static HttpResponse post(String url,String params){
        try {
            //1、创建get请求，带请求方式、url
            HttpPost post = new HttpPost(url);
            //2、设置请求头、请求体
            //2、1设置请求头
            post.setHeader("X-Lemonban-Media-Type", "lemonban.v1");
            post.setHeader("Content-Type", "application/json");
            //post.setHeader("Authorization","Bearer ${token}");
            //2、2设置请求体
            StringEntity requestentity = new StringEntity(params);
            post.setEntity(requestentity);
            //3、创建请求客户端
            HttpClient client = HttpClients.createDefault();
            //4、发送请求，并接收响应
            HttpResponse response = client.execute(post);
            //5、获得响应头、响应体、响应code
            printReponse(response);
            return response;
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    /**
     * 发送patch请求
     */
    public static HttpResponse patch(String url,String params){
        try {
        HttpPatch patch=new HttpPatch(url);
        patch.setHeader("X-Lemonban-Media-Type","lemonban.v1");
        patch.setHeader("Content-Type","application/json");
        StringEntity entity=new StringEntity(params,"utf-8");
        patch.setEntity(entity);
        //3、创建请求客户端
        HttpClient client = HttpClients.createDefault();
        //4、发送请求，并接收响应
        HttpResponse response = client.execute(patch);
        //5、获得响应头、响应体、响应code
        printReponse(response);
        return response;
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    /**
     * JSON参数转成form参数
     * @param jsonstr   json参数
     * @return
     */
    public static String josn2KeyValue(String jsonstr){
        Map<String,String> map = JSONObject.parseObject(jsonstr, Map.class);
        Set<String> keySet = map.keySet();
        String result="";
        for (String key : keySet) {
            String value = map.get(key);
            result+=key+"="+value+"&";
        }
        result=result.substring(0,result.length()-1);
        return result;
    }
    /**
     * content-type是form表单格式
     * @param url
     * @param params
     * @return
     */
    public static HttpResponse formPost(String url,String params){
        try {
            HttpPost form=new HttpPost(url);
            form.setHeader("X-Lemonban-Media-Type","lemonban.v1");
            form.setHeader("Content-Type","application/x-www-form-urlencoded");

            StringEntity entity=new StringEntity(params,"utf-8");
            form.setEntity(entity);
            //3、创建请求客户端
            HttpClient client = HttpClients.createDefault();
            //4、发送请求，并接收响应
            HttpResponse response = client.execute(form);
            //5、获得响应头、响应体、响应code
            printReponse(response);
            return response;
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    /**
     * 获得响应头、响应体、响应code
     * @param response
     * @throws IOException
     */
    private static void printReponse(HttpResponse response) throws Exception {
        int statusCode = response.getStatusLine().getStatusCode();
        Header[] allHeaders = response.getAllHeaders();
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        System.out.println(Arrays.toString(allHeaders));
        System.out.println(statusCode);
        System.out.println(s);
        System.out.println("================================================");
    }
}
