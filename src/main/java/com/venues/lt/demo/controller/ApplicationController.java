package com.venues.lt.demo.controller;

import com.venues.lt.demo.model.Application;
import com.venues.lt.demo.service.ApplicationService;
import com.venues.lt.demo.util.ResponseCode;
import com.venues.lt.demo.util.ResponseData;
import com.venues.lt.demo.util.ResponseMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping("/venues/application")
@Api(value = "申请单",description = "申请单操作api")
public class ApplicationController {
    @Autowired
    ApplicationService applicationService;


    @ResponseBody
    @PostMapping("/commit")
    @ApiOperation(value = "提交申请", notes = "提交申请")
    public ResponseData commit(@RequestBody Application application) {
        if(applicationService.commit(application) != null){
            return ResponseData.success();
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.UPDATE_FAILE);
        }

    }

    @ResponseBody
    @GetMapping("/list/{userId:.+}/{state:.+}")
    @ApiOperation(value = "查看我的申请我的审核", notes = "查看我的申请我的审核")
    public ResponseData getListByUserIdAndStatus(@PathVariable String userId, @PathVariable Integer state) {
        List<Application> list = applicationService.getListByUserIdAndState(userId,state);
        if(list!=null){
            return ResponseData.success(list);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }
    }

    @ResponseBody
    @GetMapping("/details/{applicationId:.+}")
    @ApiOperation(value = "根据id获得申请单详情", notes = "根据id获得申请单详情")
    public ResponseData getDetailsById(@PathVariable Integer applicationId) {
        Application  application = applicationService.getDetailsById(applicationId);
        if(application != null){
            return ResponseData.success(application);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }
    }

    @ResponseBody
    @GetMapping("/list/{userId:.+}")
    @ApiOperation(value = "查看待审核、待执行", notes = "查看待审核、待执行")
    public ResponseData getListByUserId(@PathVariable String userId) {
        List<Application> list = applicationService.getListByUserId(userId);
        if(list!=null){
            return ResponseData.success(list);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }
    }

    @ResponseBody
    @PostMapping("/update/{applicationId:.+}/{userId:.+}/{state:.+}")
    @ApiOperation(value = "对草稿申请、审核申请 ", notes = "修改申请单状态，申请者0是保存 1是申请 2是结束 审核者0是驳回 1是通过 场地管理者 1是执行")
    public ResponseData update(@PathVariable int applicationId,
                               @PathVariable String userId,
                               @PathVariable int state,
                                @RequestParam(value = "reason", required = false) String reason,
                               @RequestParam(value = "roomName", required = false) String roomName) {
        Application application = applicationService.update(applicationId, userId, state, reason, roomName);
        if(application != null){
            return ResponseData.success(application);
        }else{
            return ResponseData.fail(ResponseCode.FAIL, ResponseMsg.QUERY_FAIL);
        }
    }

    @GetMapping("/download/{applicationId:.+}")
    @ApiOperation(value = "下载申请书 ", notes = "下载申请书")
    public String download(HttpServletRequest request, HttpServletResponse response, @PathVariable int applicationId) {

        //使用流的形式下载文件
        try {
            //加载文件
            applicationService.createWord(response,applicationId);

            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "文件下载出错";
        }
    }
}
