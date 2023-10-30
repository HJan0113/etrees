package org.HJan.controller;

import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private DataDirectoryService dataDirectoryService;

    @Autowired
    private IdentifyRecordService identifyRecordService;

    @Autowired
    private ShopOrdersService shopOrdersService;

    @Autowired
    private MaintainFeedbackService maintainFeedbackService;

    @Autowired
    private DataInfoService dataInfoService;
    /**
     * 首页得到数据
     */
    @GetMapping
    public Result<Map<String, Long>> getInfo(){
        Map<String, Long> dataMap = new HashMap<>();
        //识别数量+总数量
        long t_recordCount = identifyRecordService.count();//总记录数
        long recordCount = identifyRecordService.selectToday();
        dataMap.put("t_recordCount",t_recordCount);
        dataMap.put("recordCount",recordCount);
        //订单数量+总数量
        long t_orderCount = shopOrdersService.count();
        long orderCount = shopOrdersService.selectToday();
        dataMap.put("t_orderCount",t_orderCount);
        dataMap.put("orderCount",orderCount);
        //反馈数量+总数
        long t_feedbackCount = maintainFeedbackService.count();//总记录数
        long feedbackCount = maintainFeedbackService.selectToday();
        dataMap.put("t_feedbackCount",t_feedbackCount);
        dataMap.put("feedbackCount",feedbackCount);
        //未处理订单数量
        long pending = shopOrdersService.getNum();
        dataMap.put("pending",pending);
        //数据总量
        long dataInfoCount = dataInfoService.count();
        dataMap.put("dataInfoCount",dataInfoCount);
        //植物总量
        long plantCount = dataDirectoryService.count();
        dataMap.put("plantCount",plantCount);
        //病种总量
        long diseaseCount =dataInfoService.getNameSet().size();
        dataMap.put("diseaseCount",diseaseCount);
        //折线趋势
        List<Map<String, Object>> mapList = identifyRecordService.getTrend();
        return Result.success(dataMap).add("mapList",mapList);
    }

    @GetMapping("/shop")
    public Result<List<Map<String,Map<String,Object>>>> getCount(){
        log.info("商品销售榜单");
        //获取商品销售榜单
        List<Map<String,Map<String,Object>>> mapList1 = shopOrdersService.getTrend();
        return Result.success(mapList1);
    }



}
