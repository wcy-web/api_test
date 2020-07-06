package homework.cases;


import homework.pojo.CaseInfo2;
import homework.utils.ExcelUtil;
import homework.utils.HttpUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * @author 向阳
 * @date 2020/6/15-15:40
 */
public class Register {
    //sheetIndex参数,定义为成员变量，datas方法可以直接使用
    public int indexSheet;

    //读取testng.xml sheetIndex参数
    @BeforeClass
    @Parameters({"indexSheet"})
    public void parameterData(int indexSheet){
        this.indexSheet=indexSheet;
    }
    @Test(dataProvider = "datas")
    public void register(CaseInfo2 caseInfo2){
        HttpUtils.call(caseInfo2.getType(), caseInfo2.getContentType(), caseInfo2.getUrl(), caseInfo2.getParams());
    }

    @DataProvider
    public Object[] datas(){
        Object[] datas = ExcelUtil.getDatas(indexSheet, CaseInfo2.class);
        return datas;
    }
}
