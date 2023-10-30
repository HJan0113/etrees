package org.HJan.common;

import org.HJan.entity.Emp;
import org.HJan.entity.EmpLog;
import org.HJan.service.EmpLogService;
import org.HJan.service.EmpService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 切面处理操作日志
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private EmpLogService logService;
    @Autowired
    private EmpService empService;

    /**
     * 对标记有@Log注解的方法进行切面处理
     * @param joinPoint 切点
     */
    @AfterReturning(returning = "ret", pointcut = "@annotation(org.HJan.common.Log)")
    public void afterReturning(JoinPoint joinPoint, Object ret) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 获取注解
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            try {
                // 记录日志的相关信息到数据库中
                EmpLog logInfo = new EmpLog();
                logInfo.setAction(logAnnotation.value());
                String empName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Emp emp = empService.getByUsername(empName);
                logInfo.setEmpId(emp.getId());
                logInfo.setStatus("成功");
                logService.save(logInfo);
            } catch (Exception e) {
                //出现异常则记录失败
                EmpLog logInfo = new EmpLog();
                logInfo.setAction(logAnnotation.value());
                String empName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Emp emp = empService.getByUsername(empName);
                logInfo.setEmpId(emp.getId());
                logInfo.setStatus("失败");
                logService.save(logInfo);
                e.printStackTrace();
            }finally {
                //删除过早的记录
                logService.delete();
            }
        }
    }
}