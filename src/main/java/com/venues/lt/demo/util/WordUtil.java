package com.venues.lt.demo.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.Text;

import com.venues.lt.framework.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 生成word文件(全局可用)
     *
     * @param dataMap      word中需要展示的动态数据，用map集合来保存
     * @param templateName word模板名称，例如：test.ftl
     */
    @SuppressWarnings("unchecked")
    public static void createWord(Map dataMap,String templateName,String filePath,String fileName) {
        LOGGER.info("【createWord】：==>方法进入");
        LOGGER.info("【templateName】：==>" + templateName);

        try {
            // 创建配置实例
            Configuration configuration = new Configuration();
            // 设置编码
            configuration.setDefaultEncoding("UTF-8");
            // 设置处理空值
            configuration.setClassicCompatible(true);

            // 设置ftl模板文件加载方式
            configuration.setClassForTemplateLoading(WordUtil.class, "/templates");

            //创建文件
            File file = new File(filePath+File.separator+fileName);
            // 如果输出目标文件夹不存在，则创建
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 将模板和数据模型合并生成文件
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            // 获取模板
            Template template = configuration.getTemplate(templateName);
            // 生成文件
            template.process(dataMap, out);

            // 清空缓存
            out.flush();
            // 关闭流
            out.close();

        } catch (Exception e) {
            LOGGER.info("【生成word文件出错】：==>" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 下载生成的文件(全局可用)
     *
     * @param fullPath 全路径
     * @param response
     */
    public static void downLoadFile(String fullPath, HttpServletResponse response) {
        LOGGER.info("【downLoadFile:fullPath】：==>" + fullPath);

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            //创建文件
            File file = new File(fullPath);
            String fileName = file.getName();

            //读文件流
            inputStream = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            //清空响应
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream; charset=utf-8");
            // response.setContentType("application/msword");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            response.setHeader("Content-Length", "" + file.length());

            //写文件流
            outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(buffer);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
