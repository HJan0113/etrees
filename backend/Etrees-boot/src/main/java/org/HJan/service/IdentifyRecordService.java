package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.IdentifyRecord;

import java.util.List;
import java.util.Map;

/**
* @author HJsir
* @description 针对表【identify_record(识别记录)】的数据库操作Service
* @createDate 2023-05-29 16:34:04
*/
public interface IdentifyRecordService extends IService<IdentifyRecord> {


    long selectToday();

    List<Map<String, Object>> getTrend();
}
