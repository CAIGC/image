package com.qywenji.image.commons.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/9.
 */
public class PoiUtil {


    public static Workbook write(String filePath, List<Map> data) throws IOException {
        Workbook workbook = getWorkbookAsStream(filePath);
        forWrite(data, 1, getSheetAt(workbook, 0));
        return workbook;
    }

    public static Workbook write(String filePath, String sheetName, List<Map> data) throws IOException {
        Workbook workbook = getWorkbookAsStream(filePath);
        forWrite(data, 1, getSheet(workbook, sheetName));
        return workbook;
    }

    public static Workbook write(String filePath, List<Map> data, Map map, int mapRownum) throws IOException {
        Workbook workbook = getWorkbookAsStream(filePath);
        int start = forWrite(data, 1, getSheetAt(workbook, 0));
        mapWrite(map, start, mapRownum, getSheetAt(workbook, 0));
        return workbook;
    }

    public static Workbook mergedWrite(String filePath, List<List<Map>> data, int start, Integer[] colIndex) throws IOException {
        Workbook workbook = getWorkbookAsStream(filePath);
        mergedWrite(data, start, getSheetAt(workbook, 0), colIndex);
        return workbook;
    }

    public static Workbook getWorkbookAsStream(String filePath) throws IOException {
        return ReadExcelUtil.getWorkbookAsStream(filePath);
    }

    public static Sheet getSheet(Workbook workbook, String name) {
        return workbook.getSheet(name);
    }

    public static Sheet getSheetAt(Workbook workbook, int index) {
        return workbook.getSheetAt(index);
    }

    private static int forWrite(List<Map> data, int start, Sheet sheet) {
        if (sheet == null) {
            return start;
        }
        HashMap<String, Object> rows = insertRow(sheet, start, data.size());
        List<Row> insert = (List<Row>) rows.get("insert");
        Row current = (Row) rows.get("current");

        forRender(data, insert, current);
        int rownum = current.getRowNum();
        removeRow(sheet, current, true);
        return rownum;
    }

    private static void forRender(List<Map> data, List<Row> insert, Row current) {
        for (int i = 0; i < data.size(); i++) {
            Row row = insert.get(i);
            for (int columnIndex = current.getFirstCellNum(); columnIndex < current.getLastCellNum(); columnIndex++) {
                Cell currentCell = current.getCell(columnIndex);
                Cell cell = row.createCell(columnIndex);
                cell.setCellStyle(currentCell.getCellStyle());
                String value = ReadExcelUtil.getCellValue(currentCell, false);
                String[] or = value.split("\\|\\|");
                if (or.length > 1 || or[0].startsWith("VLOOKUP")) {
                    or(data, i, row, cell, value, or);
                } else {
                    cell.setCellValue(getValue(value, data.get(i)));
                }
            }
        }
    }

    private static void mapWrite(Map data, int start, int size, Sheet sheet) {
        if (sheet == null) {
            return;
        }
        for (int i = start; i < start + size; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int col = row.getFirstCellNum(); col < row.getLastCellNum(); col++) {
                    Cell cell = row.getCell(col);
                    cell.setCellValue(getValue(ReadExcelUtil.getCellValue(cell, false), data));
                }
            }
        }
    }


    private static void mergedWrite(List<List<Map>> data, int start, Sheet sheet, Integer[] colIndex) {
        Row current = sheet.getRow(start);
        for (List<Map> block : data) {
            HashMap<String, Object> rows = insertRow(sheet, start, block.size());
            List<Row> insert = (List<Row>) rows.get("insert");
            current = (Row) rows.get("current");
            Row minRow = (Row) rows.get("minRow");
            Row maxRow = (Row) rows.get("maxRow");
            start = current.getRowNum();
            forRender(block, insert, current);
            for (int i = 0; i < colIndex.length; i++) {
                sheet.addMergedRegion(new CellRangeAddress(minRow.getRowNum(), maxRow.getRowNum(), colIndex[i], colIndex[i]));
            }
        }
        removeRow(sheet, current, true);
    }

    private static void removeRow(Sheet sheet, Row row, boolean shift) {
        if (row != null) {
            int start = row.getRowNum() + 1;
            int end = sheet.getLastRowNum();
            sheet.removeRow(row);
            if (shift && start <= end) {
                sheet.shiftRows(start, end, -1);
            }
        }

    }

    private static void or(List<Map> data, int i, Row row, Cell cell, String value, String[] or) {
        for (int j = 0; j < or.length; j++) {
            String cellvalue;
            if (or[j].startsWith("VLOOKUP")) {
                cellvalue = or[j];
            } else {
                cellvalue = getValue(value, data.get(i));
            }
            if (!StringUtils.isEmpty(cellvalue)) {
                if (cellvalue.startsWith("VLOOKUP")) {
                    cellvalue = cellvalue.replace("$rowNumber", String.valueOf(row.getRowNum() + 1));
                    cell.setCellType(Cell.CELL_TYPE_FORMULA);
                    cell.setCellFormula(cellvalue);
                } else
                    cell.setCellValue(cellvalue);
                break;
            }
            if (j == or.length - 1) {
                cell.setCellValue(cellvalue);
            }
        }
    }

    public static HashMap<String, Object> insertRow(Sheet sheet, int rownum, int size) {
        HashMap<String, Object> data = new HashMap<>();
        Row row = sheet.getRow(rownum);
        if (row != null) {
            if (size > 0) {
                sheet.shiftRows(rownum, sheet.getLastRowNum(), size);
                List<Row> rows = new ArrayList<>();
                int start = rownum;
                int end = start + size - 1;
                for (int i = start; i <= end; i++) {
                    rows.add(sheet.createRow(i));
                }
                data.put("insert", rows);
                data.put("minRow", rows.get(0));
                data.put("maxRow", rows.get(rows.size() - 1));
            }
            data.put("current", sheet.getRow(rownum + size));
        }
        return data;
    }

    private static String getValue(String key, Map map) {
        Object value;
        if (key.contains("$")) {
            value = map.get(key.substring(1));
        } else {
            value = key;
        }
        if (value == null) value = "";
        return String.valueOf(value);
    }


}
