package com.ibeacon.web.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibeacon.model.msginfo.MessageInfo;

/**
 * 文件下载controller
 * @author zz
 * @version 1.0 2017年7月14日
 */
@Controller
@RequestMapping("/download")
public class DownloadController {

    @RequestMapping("/file")
    @ResponseBody
    public MessageInfo downFile(HttpServletRequest request, HttpServletResponse response){
        String name=request.getParameter("filename");
        // TODO Auto-generated method stub
        try {
            String path = request.getSession().getServletContext().getRealPath(
                    "opt/"+name);
            File file = new File(path);
            String filename = file.getName();
            // 取得文件的扩展名ext
            String ext = filename.substring(filename.lastIndexOf(".") + 1)
                    .toUpperCase();
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length()); // 设置返回的文件类型
            OutputStream toClient = new BufferedOutputStream(response
                    .getOutputStream()); // 得到向客户端输出二进制数据的对象
            // 根据扩展名声称客户端浏览器mime类型
            if (ext.equals("xls"))
                response.setContentType("application/msexcel");
            else
                response.setContentType("application/octet-stream"); // 设置返回的文件类型
            toClient.write(buffer); // 输出数据
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return new MessageInfo(1, "failed");
        }
        return new MessageInfo(0, "success");
    }

}
