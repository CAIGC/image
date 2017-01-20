package com.qywenji.image.commons.utils;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ReadExcelUtil {

    private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";

    /***
     * <pre>
     * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类)
     *   xls:HSSFWorkbook
     *   xlsx：XSSFWorkbook
     * @param filePath
     * @return
     * @throws java.io.IOException
     * </pre>
     */
    public static Workbook getWorkbook(String filePath) throws IOException {
        Workbook workbook = null;
        InputStream is = new FileInputStream(filePath);
        if (filePath.endsWith(EXTENSION_XLS)) {
            workbook = new HSSFWorkbook(is);
        } else if (filePath.endsWith(EXTENSION_XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }
    public static Workbook getWorkbookAsStream(String filePath) throws IOException {
        Workbook workbook = null;
        InputStream is = PoiUtil.class.getResourceAsStream(filePath);
        if (filePath.endsWith(EXTENSION_XLS)) {
            workbook = new HSSFWorkbook(is);
        } else if (filePath.endsWith(EXTENSION_XLSX)) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }
    /**
     * 文件检查
     * @param filePath
     * @throws java.io.FileNotFoundException
     * @throws org.apache.xmlbeans.impl.piccolo.io.FileFormatException
     */
    private static void preReadCheck(String filePath) throws FileNotFoundException, FileFormatException {
        // 常规检查
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("传入的文件不存在：" + filePath);
        }

        if (!(filePath.endsWith(EXTENSION_XLS) || filePath.endsWith(EXTENSION_XLSX))) {
            throw new FileFormatException("传入的文件不是excel");
        }
    }

    /**
     * 读取excel文件内容
     * @param filePath
     * @throws java.io.FileNotFoundException
     * @throws org.apache.xmlbeans.impl.piccolo.io.FileFormatException
     * flag = true表示1读成1，=false表示把1读成1.0
     */
    public static List<Object> readExcel(String fileType,InputStream inputFile,Class bean,boolean flag) throws FileNotFoundException, FileFormatException {
        Workbook workbook = null;
        List<Object> list = new ArrayList<Object>();
        try {
            if (fileType.equals(EXTENSION_XLS)) {
                workbook = new HSSFWorkbook(inputFile);
            } else if (fileType.equals(EXTENSION_XLSX)) {
                workbook = new XSSFWorkbook(inputFile);
            }
           
            // 读文件 一个sheet一个sheet地读取
            for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }

                int firstRowIndex = sheet.getFirstRowNum();
                int lastRowIndex = sheet.getLastRowNum();

                Object tmpObject = null;
                Row currentRow = null;
                // 读取数据行
                for (int rowIndex = firstRowIndex + 2; rowIndex <= lastRowIndex; rowIndex++) {
                	currentRow = sheet.getRow(rowIndex);// 当前行
                    tmpObject = bean.newInstance();
                    Field[] fields = bean.getDeclaredFields();
                    for (int columnIndex = currentRow.getFirstCellNum(); columnIndex < currentRow.getLastCellNum(); columnIndex++) {
                    	if(columnIndex < fields.length){
                    		CommonUtil.setProperty(tmpObject, fields[columnIndex].getName(), getCellValue(currentRow.getCell(columnIndex), flag));
                    	}
                    }
                    list.add(tmpObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	if(inputFile != null){
        		try {
					inputFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        return list;
    }

    /**
     * 取单元格的值
     * @param cell 单元格对象
     * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
     * @return
     */
    public static   String getCellValue(Cell cell, boolean treatAsStr) {
        if (cell == null) {
            return "";
        }

        if (treatAsStr) {
            // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
            // 加上下面这句，临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
        	if(HSSFDateUtil.isCellDateFormatted(cell)){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        		Date date = cell.getDateCellValue();  
        		return sdf.format(date);
        	}
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

}