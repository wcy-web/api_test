package homework.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import homework.constants.Constants;

import java.io.FileInputStream;
import java.util.List;

/**
 * 将List集合转出一维数组，方便使用testng的数据驱动
 * @author 向阳
 * @date 2020/6/15-14:50
 */
public class ExcelUtil {
    /**
     * 将list集合转为一维数组，提供给testng的数据驱动使用
     * @param indexSheet
     * @param clazz
     * @return
     */
    public static Object[] getDatas(int indexSheet,Class clazz){
        try {
            List list = readExcel(indexSheet, clazz);
            Object[] objects = list.toArray();
            return objects;
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;
    }

    /**
     * 使用easypoi读取excel数据并返回List集合
     * @param indexSheet  sheet开始位置
     * @param clazz       映射关系字节码
     * @return
     * @throws Exception
     */
    public static List readExcel(int indexSheet,Class clazz) throws Exception {
        String path= Constants.EXCEL_PATH;
        FileInputStream fis = new FileInputStream(path);
        ImportParams params=new ImportParams();
        params.setStartSheetIndex(indexSheet);
        List list = ExcelImportUtil.importExcel(fis, clazz, params);
        return list;
    }
}
