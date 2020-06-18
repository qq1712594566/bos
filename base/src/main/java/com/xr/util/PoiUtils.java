package com.xr.util;

import com.xr.entity.SysUser;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PoiUtils {

    //这是把数据导出到本地保存为Excel文件的方法
    public static ResponseEntity<byte[]> exportJobLevelExcel(List<SysUser> allSysUser) throws IOException {
        System.out.println("进入exportJobLevelExcel");

        HSSFWorkbook workbook = new HSSFWorkbook();   //创建一个Excel文件

        // 创建Excel文档属性，必不可少。少了的话，getDocumentSummaryInformation()方法就会返回null
        workbook.createInformationProperties();
        DocumentSummaryInformation info = workbook.getDocumentSummaryInformation();
        info.setCompany("新睿集团");   // 设置公司信息
        info.setManager("老袁");      // 设置管理者
        info.setCategory("User表");   //  设置文件名


        // 设置文件中的日期格式
        HSSFCellStyle datecellStyle = workbook.createCellStyle();
        datecellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));  // 这个文件的日期格式和平时的不一样

        // 创建表单
        HSSFSheet sheet = workbook.createSheet("用户表");
        // 定义列的宽度
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);

        HSSFRow r0 = sheet.createRow(0);  // 创建第一行
        HSSFCell c0 = r0.createCell(0);   //  创建列
        HSSFCell c1 = r0.createCell(1);   //  创建列
        HSSFCell c2 = r0.createCell(2);   //  创建列
        HSSFCell c3 = r0.createCell(3);   //  创建列
     /* HSSFCell c4 = r0.createCell(4);     // 创建列 */

        c0.setCellValue("用户编号");
        c1.setCellValue("用户名");
        c2.setCellValue("email");
        c3.setCellValue("创建时间");
        /*c4.setCellValue("是否可用");*/

        for (int i = 0; i < allSysUser.size(); i++) {

            SysUser jl=allSysUser.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            HSSFCell cell0 = row.createCell(0);
            cell0.setCellValue(jl.getId());
            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(jl.getUsername());
            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(jl.getEmail());
            HSSFCell cell3 = row.createCell(3);
            cell3.setCellStyle(datecellStyle);     //让日期格式数据正确显示
            cell3.setCellValue(jl.getCreateTime());
           /* HSSFCell cell4 = row.createCell(4);*/
            /* cell4.setCellValue(jl.()?"是":"否");*/


        }

    /* // 配置指定下载路径                                                  // 指定路径与名字和格式
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\idealproject\\指定下载路径\\user.xls");
        workbook.write(fileOutputStream);// 将数据写出去*/

        // 配置自定义下载路径
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String("用户表.xls".getBytes("UTF-8"),"iso-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        workbook.write(baos);
        ResponseEntity<byte[]> entity = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.CREATED);
        System.out.println(entity);
        return entity;

    }





    //这是解析上传的Excel文件为对象集合，从而批量添加数据的方法
    public static List<SysUser> parseFile2List(MultipartFile file) throws IOException {
        System.out.println("进入parseFile2List");
        System.out.println(file);

        List<SysUser> sysUsers=new ArrayList<>();
        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
        HSSFSheet sheet = workbook.getSheetAt(0);

        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();// 获取表单所有的行
        for (int i = 1; i < physicalNumberOfRows; i++) {
            HSSFRow row = sheet.getRow(i);
            SysUser j1=new SysUser();

            HSSFCell c0 = row.getCell(0);
            j1.setId((long) c0.getNumericCellValue());

            HSSFCell c1 = row.getCell(1);
            j1.setUsername(c1.getStringCellValue());

            HSSFCell c2 = row.getCell(2);
            j1.setEmail(c2.getStringCellValue());

            HSSFCell c3 = row.getCell(3);
            j1.setCreateTime(c3.getDateCellValue());

         /*  HSSFCell c4 = row.getCell(4);
          j1.setEnabled(c4.getStringCellValue().equals("是"));
         */
            sysUsers.add(j1);
        }

        return sysUsers;
    }


}