package org.HJan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.IdentifyRecord;
import org.HJan.mapper.IdentifyRecordMapper;
import org.HJan.service.IdentifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author HJsir
 * @description 针对表【identify_record(识别记录)】的数据库操作Service实现
 * @createDate 2023-05-29 16:34:04
 */
@Service
public class IdentifyRecordServiceImpl extends ServiceImpl<IdentifyRecordMapper, IdentifyRecord>
        implements IdentifyRecordService {

    @Autowired
    private IdentifyRecordMapper identifyRecordMapper;
    @Autowired

    @Override
    public long selectToday() {
        return identifyRecordMapper.selectToday();
    }

    @Override
    @Transactional
    public List<Map<String, Object>> getTrend() {
        //获取七天内识别记录
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> map = new HashMap<>();
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDate datex = LocalDate.now().minusDays(i-1);
            LocalDate lDatex = LocalDate.now().minusDays(i);
            LambdaQueryWrapper<IdentifyRecord> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.ge(IdentifyRecord::getCreateTime, lDatex).le(IdentifyRecord::getCreateTime, datex);
            Long count = identifyRecordMapper.selectCount(queryWrapper);
            map.put("date", date.toString());
            map.put("count", count);
            mapList.add(map);
        }
        return mapList;
    }
}




