package org.HJan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.HJan.entity.MaintainFeedback;

/**
* @author HJsir
* @description 针对表【maintain_feedback(用户反馈表)】的数据库操作Service
* @createDate 2023-05-28 14:26:28
*/
public interface MaintainFeedbackService extends IService<MaintainFeedback> {

    long selectToday();
}
