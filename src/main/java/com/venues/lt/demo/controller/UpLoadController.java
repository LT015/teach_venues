package com.venues.lt.demo.controller;


import com.venues.lt.framework.utils.ExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

@Controller
@ApiIgnore
public class UpLoadController {

    @RequestMapping("file/upload")
    @ResponseBody
    public  List<List<String>> pubggupload(@RequestParam("file") MultipartFile file) throws Exception {
        String name = file.getOriginalFilename();
        if(name.length() < 6 || !name.substring(name.length()-5).equals(".xlsx")){
            List<Object> li = new ArrayList<>();
            li.add("文件格式错误");
            return null;
        }
        List<List<String>> list = ExcelUtil.excelToObjectList(file.getInputStream());

        return list;

    }
}
