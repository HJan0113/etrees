package org.HJan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.HJan.entity.MaintainFeedback;
import org.HJan.mapper.MaintainFeedbackMapper;
import org.HJan.service.MaintainFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author HJsir
* @description 针对表【maintain_feedback(用户反馈表)】的数据库操作Service实现
* @createDate 2023-05-28 14:26:28
*/
@Service
public class MaintainFeedbackServiceImpl extends ServiceImpl<MaintainFeedbackMapper, MaintainFeedback>
    implements MaintainFeedbackService{

    @Autowired
    private MaintainFeedbackMapper maintainFeedbackMapper;

    @Override
    public long selectToday() {
        return maintainFeedbackMapper.selectToday();
    }
}




