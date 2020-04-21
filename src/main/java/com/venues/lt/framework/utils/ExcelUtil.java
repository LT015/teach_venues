package com.venues.lt.framework.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);
    public static List<List<String>> excelToObjectList(InputStream inputStream) {
        List<List<String>> resultList = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        String cellData = null;
//        String filePath = "D:\\课表信息.xls";
        Workbook workbook = null;
//        workbook = readExcel(filePath);
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数

            int rownum = sheet.getPhysicalNumberOfRows();
            //工作表的列
            Row row = sheet.getRow(0);

            //总列数
            int colnum = row.getPhysicalNumberOfCells();
            log.debug("*********lieshu1",colnum);
            //得到指定的单元格
            Cell cell = row.getCell(0);
            for (int i = 0; i < rownum + 1; i++) {
                List<String> stringlist = new ArrayList<>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j = 0; j < colnum; j++){
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        stringlist.add(cellData);
                    }
                }else{
                    break;
                }
                resultList.add(stringlist);
            }
        } catch (Exception e) {
            LOGGER.error("parse excel file error :", e);
        }
        return resultList ;
    }

    /**
     * 导出excel
     *
     * @param fileName
     * @param headers
     * @param datas
     * @param response
     */
    public static void excelExport(String fileName, String[] headers, List<Object[]> datas,
                                   HttpServletResponse response) {
        Workbook workbook = getWorkbook(headers, datas);
        if (workbook != null) {
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                workbook.write(byteArrayOutputStream);

                String suffix = ".xls";
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + new String((fileName + suffix).getBytes(), "iso-8859-1"));

                OutputStream outputStream = response.getOutputStream();
                outputStream.write(byteArrayOutputStream.toByteArray());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     *
     * @param headers
     *            列头
     * @param datas
     *            数据
     * @return
     */
    public static Workbook getWorkbook(String[] headers, List<Object[]> datas) {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet();
        Row row = null;
        Cell cell = null;
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);

        Font font = workbook.createFont();

        int line = 0, maxColumn = 0;
        if (headers != null && headers.length > 0) {// 设置列头
            row = sheet.createRow(line++);
            row.setHeightInPoints(23);
            font.setBold(true);
            font.setFontHeightInPoints((short) 13);
            style.setFont(font);

            maxColumn = headers.length;
            for (int i = 0; i < maxColumn; i++) {
                cell = row.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(style);
            }
        }

        if (datas != null && datas.size() > 0) {// 渲染数据
            for (int index = 0, size = datas.size(); index < size; index++) {
                Object[] data = datas.get(index);
                if (data != null && data.length > 0) {
                    row = sheet.createRow(line++);
                    row.setHeightInPoints(20);

                    int length = data.length;
                    if (length > maxColumn) {
                        maxColumn = length;
                    }

                    for (int i = 0; i < length; i++) {
                        cell = row.createCell(i);
                        cell.setCellValue(data[i] == null ? null : data[i].toString());
                    }
                }
            }
        }

        for (int i = 0; i < maxColumn; i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }

    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue())
                            .replace(" ","")
                            .replaceAll("\r|\n", "");
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue())
                                .replace(" ","")
                                .replaceAll("\r|\n", "");
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString()
                            .replace(" ","")
                            .replaceAll("\r|\n", "");
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }

}
