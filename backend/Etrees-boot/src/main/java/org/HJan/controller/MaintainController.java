package org.HJan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.entity.EmpLog;
import org.HJan.entity.MaintainFeedback;
import org.HJan.service.EmpLogService;
import org.HJan.service.MaintainFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Slf4j
@RequestMapping("/maintain")
public class MaintainController {

    @Autowired
    private MaintainFeedbackService maintainFeedbackService;

    @Autowired
    private EmpLogService empLogService;

    /**
     * 获取用户反馈
     * @param page
     * @param pageSize
     * @param feedbackId
     * @param userId
     * @return
     */
    @GetMapping("/feedback/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('maintain:feedback','sys:maintain:manager')")
    public Result<IPage<MaintainFeedback>> page(@PathVariable int page,
                                   @PathVariable int pageSize,
                                   @RequestParam(required = false) Integer feedbackId,
                                   @RequestParam(required = false) Integer userId) {
        log.info("分页查询：page = {},pageSize={},feedback_id={},user_id={}", page, pageSize, feedbackId, userId);

        //构造分页构造器
        Page<MaintainFeedback> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<MaintainFeedback> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤-
        queryWrapper.eq(feedbackId !=null, MaintainFeedback::getId, feedbackId);
        queryWrapper.eq(userId != null, MaintainFeedback::getUserId, userId);
        //排序条件
        queryWrapper.orderByAsc(MaintainFeedback::getId);
        //执行查询
        maintainFeedbackService.page(pageInfo, queryWrapper);
        //将emp信息存入redis , 存在时间 120分钟
        return Result.success(pageInfo);
    }


    /**
     * 获取日志信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/log/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('sys:maintain:manager','maintain:log')")
    public Result<IPage<EmpLog>> page(@PathVariable int page,
                                      @PathVariable int pageSize,
                                      @RequestParam(required = false)Integer id,
                                      @RequestParam(required = false)String startTime,
                                      @RequestParam(required = false)String endTime) {
        log.info("分页查询：page = {},pageSize={}", page, pageSize);
        //反序列化为时间戳
        LocalDateTime start=null,end=null;
        if (startTime != null && !startTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            start = LocalDateTime.parse(startTime, formatter);
        }
        if (endTime != null && !endTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            end = LocalDateTime.parse(endTime, formatter);
        }
        //构造分页构造器
        Page<EmpLog> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<EmpLog> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤-
        queryWrapper.ge(start!=null, EmpLog::getCreateTime,start)
                .le(end!=null,EmpLog::getCreateTime,end)
                .eq(id!=null,EmpLog::getEmpId,id);
        //排序条件
        queryWrapper.orderByDesc(EmpLog::getCreateTime);
        //执行查询
        Page<EmpLog> recordPage = empLogService.page(pageInfo, queryWrapper);
        //将emp信息存入redis , 存在时间 120分钟
        return Result.success(recordPage);
    }




}
