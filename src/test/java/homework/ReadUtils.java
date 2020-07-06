package homework;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author 向阳
 * @date 2020/6/12-13:28
 */
public class ReadUtils {
    public static Object[][] readExcel() throws Exception {
        String path=ReadUtils.class.getClassLoader().getResource("cases_v1.xls").getPath();
        FileInputStream fis=new FileInputStream(path);
        Workbook sheets = WorkbookFactory.create(fis);
        Sheet sheet = sheets.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        //创建二维数组用于储存url 和 params
        Object[][] objects=new Object[lastRowNum][2];
        for (int i = 0; i <lastRowNum ; i++) {
            Row row = sheet.getRow(i+1);
            short lastCellNum = row.getLastCellNum();
//            for (int j = 0; j < lastCellNum; j++) {
//                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                cell.setCellType(CellType.STRING);
//                String stringCellValue = cell.getStringCellValue();
//                System.out.print(stringCellValue+",");
//            }
            //获取url的值
            Cell urlCell = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String url = urlCell.getStringCellValue();
            objects[i][0]=url;

            //获取params的值
            Cell paramsCell = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String params = paramsCell.getStringCellValue();
            objects[i][1]=params;
        }
        return objects;
    }
}
