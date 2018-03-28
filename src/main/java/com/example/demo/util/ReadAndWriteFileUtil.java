package com.example.demo.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.example.demo.pojo.TagChanges;

@Service
public class ReadAndWriteFileUtil {

    public void writeFile(List<Object> tagChanges) {
        Path fpath = Paths.get("D:/readme.txt");
        // 创建文件
        if (Files.exists(fpath)) {
            try {
                Files.delete(fpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.createFile(fpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 创建BufferedWriter
        try {
            BufferedWriter bfw = Files.newBufferedWriter(fpath);
            for (Object object : tagChanges) {
                TagChanges tag = (TagChanges) object;
                bfw.write("TagName: " + tag.getTagName() + "\r\n" + "\r\n");
                if (tag.getDbChanges() != null && !tag.getDbChanges().isEmpty()) {
                    bfw.write("DBchanges: \r\n" + tag.getDbChanges() + "\r\n" + "\r\n");
                }
                if (tag.getWcsChanges() != null && !tag.getWcsChanges().isEmpty()) {
                    bfw.write("WCSchanges: \r\n" + tag.getWcsChanges() + "\r\n" + "\r\n");
                }
                if (tag.getEndecaChanges() != null && !tag.getEndecaChanges().isEmpty()) {
                    bfw.write("EndecaChanges: \r\n" + tag.getEndecaChanges() + "\r\n" + "\r\n");
                }
                if (tag.getEnvClientChanges() != null && !tag.getEnvClientChanges().isEmpty()) {
                    bfw.write("EnvClientChanges: \r\n" + tag.getEnvClientChanges() + "\r\n" + "\r\n");
                }
                if (tag.getContentKeyChanges() != null && !tag.getContentKeyChanges().isEmpty()) {
                    bfw.write("ContentKeyChanges: \r\n" + tag.getContentKeyChanges() + "\r\n" + "\r\n");
                }
                if (tag.getComments() != null && !tag.getComments().isEmpty()) {
                    bfw.write("Comments: \r\n" + tag.getComments() + "\r\n" + "\r\n");
                }
            }
            bfw.flush();
            bfw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void downloadFileFromString(HttpServletResponse response, String content) {
        String filename = "readme.txt";
        response.reset();
        response.setContentType("application/x-download");
        // "给用户提供的下载文件名"
        String filedisplay = filename;
        try {
            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bis = new BufferedInputStream(in);
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void downloadFile(HttpServletResponse response, String filePath) {
        // 关于文件下载时采用文件流输出的方式处理：
        String filename = "readme.txt";
        response.reset();// 可以加也可以不加
        response.setContentType("application/x-download");
        String filedownload = filePath + filename;
        // "给用户提供的下载文件名"
        String filedisplay = filename;
        try {
            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        File file = new File(filedownload);
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + filedisplay);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
            bis = new BufferedInputStream(in);
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
