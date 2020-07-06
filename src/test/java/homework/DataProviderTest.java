package homework;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author 向阳
 * @date 2020/6/12-13:53
 */
public class DataProviderTest {

    @Test(dataProvider="datas")
    public void dataTest(String url,String params){
        System.out.println(url+","+params);
    }

    @DataProvider
    public Object[][] datas() throws Exception {
        Object[][] datas=ReadUtils.readExcel();
        return datas;
    }
}
