package com.dream.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    private static final String EXTENSION_XLS = "xls";
    private static final String EXTENSION_XLSX = "xlsx";

    /***
     * 取得Workbook对象(xls和xlsx对象不同,不过都是Workbook的实现类)
     *   xls:HSSFWorkbook
     *   xlsx：XSSFWorkbook
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

    /**
     * 根据文件对象获取exel文件对象
     */
    public static Workbook readExcel(MultipartFile file) {
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            if (originalFilename.endsWith(EXTENSION_XLS)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(EXTENSION_XLSX)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
            return workbook;
        } catch (Exception e) {
            logger.error("excel文件格式格式错误!FileName:["+originalFilename+"]",e);
            return null;
        }
    }

    /**
     * 获取单元格数据
     */
    public static  String getStrValue(Cell xssfRow) {
        if (xssfRow == null) {
            return "";
        }
        String result;
        if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
            result = String.valueOf(xssfRow.getBooleanCellValue());
        } else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
            result = String.valueOf(xssfRow.getNumericCellValue());
        } else {
            result = String.valueOf(xssfRow.getStringCellValue());
        }
        return result;
    }

}
