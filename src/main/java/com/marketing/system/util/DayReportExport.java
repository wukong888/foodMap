package com.marketing.system.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class DayReportExport {
    /**
     * 导出项目日报
     * @param outputStream 参数为输出流
     */
    public static void exportExcel(List<Map> result, OutputStream outputStream){
        try {
            //1  创建工作薄
            HSSFWorkbook workBook=new HSSFWorkbook();
            //1.1  创建合并单元格对象
            CellRangeAddress cellRangeAddress=new CellRangeAddress(0,0,0,4);
            //2 创建工作表
            HSSFSheet sheet=workBook.createSheet();
            //2.1 加载合并单元格对象
            sheet.addMergedRegion(cellRangeAddress);
            //自适应列宽度：
            sheet.autoSizeColumn(1, true);
            /*//设置列宽
            sheet.setColumnWidth(0, 15*256);
            sheet.setColumnWidth(1, 15*350);
            sheet.setColumnWidth(2, 15*256);
            sheet.setColumnWidth(3, 15*256);
            sheet.setColumnWidth(4, 15*256);*/




            //3 创建行
            //3.1头标题行
            HSSFRow rowHead=sheet.createRow(0);
            HSSFCell cellHead=rowHead.createCell(0);
            //设置单元格样式
            HSSFCellStyle style = workBook.createCellStyle();
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
            //3.1.3将设置好的样式添加到头标题行
          /*  Date date=new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
            String Date=sdf1.format(date);*/
            cellHead.setCellValue("项目管理系统日报");
            cellHead.setCellStyle(getStyle(workBook,(short)20 ));
            //3.2 标题行
            HSSFRow rowColumn=sheet.createRow(1);
            //3.2.1 标题数组
            String[] title={"名称","负责人","状态","进度","动态"};
            for (int i = 0; i < title.length; i++) {//遍历数组
                HSSFCell cellColumn=rowColumn.createCell(i);
                //3.2.2 调用方法 设置标题行样式
                cellColumn.setCellStyle(getStyle(workBook,(short) 13));
                //3.2.3 设置标题
                cellColumn.setCellValue(title[i]);

            }
            //设日期格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            HSSFCellStyle dateCellStyle=workBook.createCellStyle();
            short df=workBook.createDataFormat().getFormat("yyyy-mm-dd hh:mm");
            dateCellStyle.setDataFormat(df);
            //4 操作单元格
            if(result!=null){
                for (int j = 0; j < result.size(); j++) {
                    HSSFRow row=sheet.createRow(j+2);
                    System.out.println("result---"+result);
                    row.createCell(0).setCellValue((Integer)result.get(j).get("proName"));
                    row.createCell(1).setCellValue((String)result.get(j).get("creater"));
                    row.createCell(2).setCellValue((String)result.get(j).get("proState"));
                    row.createCell(3).setCellValue((String)result.get(j).get("proProgres"));
                    row.createCell(4).setCellValue((String)result.get(j).get("proLogs"));


                }
            }
//			sheet.autoSizeColumn((short)3);
            //5、输出
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HSSFCellStyle getStyle(HSSFWorkbook workBook,short fontSize){
        HSSFCellStyle style=workBook.createCellStyle();
        //3.1.1设置头标题行居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //3.1.2设置头标题行字体加粗居中
        style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font=workBook.createFont();
        font.setBoldweight((short) 12);
        font.setFontHeightInPoints(fontSize);
        style.setFont(font);
        return style;
    }


}
