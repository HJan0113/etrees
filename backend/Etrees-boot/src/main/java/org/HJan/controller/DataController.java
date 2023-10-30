package org.HJan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Log;
import org.HJan.common.Result;
import org.HJan.entity.DataDirectory;
import org.HJan.entity.DataInfo;
import org.HJan.entity.DataTips;
import org.HJan.entity.IdentifyRecord;
import org.HJan.service.DataDirectoryService;
import org.HJan.service.DataInfoService;
import org.HJan.service.DataTipsService;
import org.HJan.service.IdentifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 数据管理
 */
@RestController
@Slf4j
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataDirectoryService dataDirectoryService;

    @Autowired
    private DataInfoService dataInfoService;

    @Autowired
    private DataTipsService dataTipsService;

    @Autowired
    private IdentifyRecordService identifyRecordService;

    //图鉴信息白名单（不可删除）
    private static final Integer DEL_WHITELIST[] = {
            108,110,150,148,149,121,172,173,123,125,133,114,113,115,134,109,124,174,176,175
    };

    /**
     * 添加目录
     *
     * @param dataDirectory
     * @return
     */
    @Log(value = "图鉴-添加目录")
    @PostMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> addDataDir(@RequestBody DataDirectory dataDirectory) {
        log.info("添加目录信息:{}", dataDirectory.toString());
        dataDirectoryService.save(dataDirectory);
        return Result.success("添加成功");
    }

    /**
     * 删除目录
     *
     * @param id
     * @return
     */
    @Log(value = "图鉴-删除目录")
    @DeleteMapping("/dir/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> deleteDir(@PathVariable Integer id) {
        log.info("删除目录信息,id:{}", id);
        dataDirectoryService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改目录名-查询回显
     *
     * @param id
     * @return
     */
    @GetMapping("/dir/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<DataDirectory> getById(@PathVariable Integer id) {
        log.info("编辑目录信息,id:{}", id);
        DataDirectory directory = dataDirectoryService.getById(id);
        return Result.success(directory);
    }

    /**
     * 修改目录名
     *
     * @param dataDirectory
     * @return
     */
    @Log(value = "图鉴-修改目录")
    @PutMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> update(@RequestBody DataDirectory dataDirectory) {
        log.info("修改目录信息");
        // 查询指定 ID 的原始数据，并获取版本号
        DataDirectory originalData = dataDirectoryService.getById(dataDirectory.getId());
        Timestamp originalVersion = originalData.getVersion();
        // 将修改数据对象中的版本号设置为原始数据版本号
        dataDirectory.setVersion(originalVersion);
        // 更新数据时添加版本号判断

        boolean success = dataDirectoryService.update(dataDirectory, new UpdateWrapper<DataDirectory>()
                .eq("id", dataDirectory.getId())
                .eq("version", originalVersion));
        if (success) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，数据已被其他用户修改。");
        }
    }

    /**
     * 目录查询
     *
     * @return
     */
    @GetMapping("/dir")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<List<DataDirectory>> getDir() {
        log.info("目录查询回显");
        List<DataDirectory> dataDirectoryList = dataDirectoryService.getAll();

        return Result.success(dataDirectoryList);
    }

    /**
     * 添加健康提示
     *
     * @param dataTips
     * @return
     */
    @Log(value = "图鉴-添加健康提示")
    @PostMapping("/tips")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> addDataTips(@RequestBody DataTips dataTips) {
        log.info("添加提示信息:{}", dataTips.toString());
        dataTipsService.save(dataTips);
        return Result.success("添加成功");
    }

    /**
     * 删除提示
     *
     * @param id
     * @return
     */
    @Log(value = "图鉴-删除健康提示")
    @DeleteMapping("/tips/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> deleteTips(@PathVariable Integer id) {
        log.info("删除提示信息,id:{}", id);
        dataTipsService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改提示信息-查询回显
     *
     * @param id
     * @return
     */
    @GetMapping("/tips/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<DataTips> getById2(@PathVariable Integer id) {
        log.info("编辑提示信息,id:{}", id);
        DataTips dataTips = dataTipsService.getById(id);
        return Result.success(dataTips);
    }

    /**
     * 修改提示信息
     *
     * @param dataTips
     * @return
     */
    @Log(value = "图鉴-修改健康提示")
    @PutMapping("/tips")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> update(@RequestBody DataTips dataTips) {
        log.info("修改提示信息");
        dataTipsService.updateById(dataTips);
        return Result.success("修改成功");
    }

    /**
     * 健康提示查询
     *
     * @return
     */
    @GetMapping("/tips")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<List<DataTips>> getTips() {
        log.info("目录查询回显");
        List<DataTips> dataTips = dataTipsService.getAll();
        return Result.success(dataTips);
    }


    /**
     * 添加图鉴信息
     *
     * @param dataInfo
     * @return
     */
    @Log(value = "图鉴-添加图鉴信息")
    @PostMapping("/info")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> addDataInfo(@RequestBody DataInfo dataInfo) {
        log.info("添加图鉴信息:{}", dataInfo.toString());
        Integer dirId = dataInfo.getDirId();
        String name = dataDirectoryService.getById(dirId).getName();
        dataInfo.setDirName(name);
        dataInfoService.save(dataInfo);
        return Result.success("添加成功");
    }

    /**
     * 删除图鉴信息
     *
     * @param id
     * @return
     */
    @Log(value = "图鉴-删除图鉴信息")
    @DeleteMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> deleteInfo(@PathVariable Integer id) {
        log.info("删除图鉴信息,id:{}", id);
        for(Integer ids : DEL_WHITELIST){
            if(ids.equals(id)){
                return Result.error("该图鉴信息不可删除");
            }
        }
        dataInfoService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改信息-查询回显
     *
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<DataInfo> getById3(@PathVariable Integer id) {
        log.info("编辑提示信息,id:{}", id);
        DataInfo dataInfo = dataInfoService.getById(id);
        return Result.success(dataInfo);
    }

    /**
     * 修改图鉴信息
     * @param dataInfo
     * @return
     */
    @Log(value = "图鉴-修改图鉴信息")
    @PutMapping("/info")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<String> update(@RequestBody DataInfo dataInfo) throws InterruptedException {
        log.info("修改图鉴信息");
        // 查询指定 ID 的原始数据，并获取版本号
        DataInfo originalData = dataInfoService.getById(dataInfo.getId());
        Timestamp originalVersion = originalData.getVersion();
        // 将修改数据对象中的版本号设置为原始数据版本号
        dataInfo.setVersion(originalVersion);
        // 更新数据时添加版本号判断

        boolean success = dataInfoService.update(dataInfo, new UpdateWrapper<DataInfo>()
                .eq("id", dataInfo.getId())
                .eq("version", originalVersion));
        if (success) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败，数据已被其他用户修改。");
        }
    }


    /**
     * 图鉴信息分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/info/{page}/{pageSize}")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    public Result<IPage<DataInfo>> page3(@PathVariable int page, @PathVariable int pageSize) {
        log.info("图鉴信息分页查询：page = {},pageSize={}");
        //构造分页构造器
        Page<DataInfo> pageInfo = new Page<>(page, pageSize);
        //执行查询
        dataInfoService.page(pageInfo);
        return Result.success(pageInfo);
    }

    /**
     * 设为常见/取消常见
     *
     * @param id
     * @return
     */
    @Log(value = "图鉴-设置(非)常见状态")
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:type')")
    @PostMapping("/status/{id}")
    public Result<String> setStatus(@PathVariable Integer id) {
        log.info("更改常见信息：{}", id);
        DataInfo dataInfo = dataInfoService.getById(id);
        if (dataInfo.getStatus() == 1) {
            dataInfo.setStatus(0);
        } else {
            dataInfo.setStatus(1);
        }
        log.info("更改状态{}", dataInfo);
        dataInfoService.updateById(dataInfo);
        return Result.success("修改成功");
    }

    /**
     * 识别记录查询
     *
     * @param page
     * @param pageSize
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    @PreAuthorize("hasAnyAuthority('sys:data:manager','data:record')")
    @GetMapping("/record/{page}/{pageSize}")
    public Result<Page<IdentifyRecord>> getRecord(@PathVariable int page,
                                                  @PathVariable int pageSize,
                                                  @RequestParam(required = false) Integer userId,
                                                  @RequestParam(required = false) String startTime,
                                                  @RequestParam(required = false) String endTime) {
        log.info("识别记录条件分页查询");
        //反序列化为时间戳
        LocalDateTime start = null, end = null;
        if (startTime != null && !startTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            start = LocalDateTime.parse(startTime, formatter);
        }
        if (endTime != null && !endTime.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            end = LocalDateTime.parse(endTime, formatter);
        }
        //建立条件
        Page<IdentifyRecord> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<IdentifyRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, IdentifyRecord::getUserId, userId);
        queryWrapper.ge(start != null, IdentifyRecord::getCreateTime, start).le(end != null, IdentifyRecord::getCreateTime, end);
        //排序方式：按时间降序
        queryWrapper.orderByDesc(IdentifyRecord::getCreateTime);
        Page<IdentifyRecord> recordPage = identifyRecordService.page(pageInfo, queryWrapper);
        return Result.success(recordPage);
    }
}
