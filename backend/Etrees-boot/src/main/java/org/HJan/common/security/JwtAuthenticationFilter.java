package org.HJan.common.security;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.HJan.common.Result;
import org.HJan.entity.Emp;
import org.HJan.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private MyEmpDetailsServiceImpl myEmpDetailsService;

    private static final String URL_WHITELIST[] = {
            "/api/**",
            "/sendMsg",
            "/upload",
            "/emps/loginByMsg",
            "/emps/login"
    };

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader("Authorization");
        log.info("请求url:{}", request.getRequestURI());
        //放行OPTIONS请求
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            System.out.println("OPTIONS请求，放行");
            return;
        }
        // token为空（匿名访问） 或 url在白名单里则放行
        if (!StringUtils.hasLength(jwt) || "null".equals(jwt) || isMatch(request.getRequestURI(), URL_WHITELIST)) {
            chain.doFilter(request, response);
            return;
        }
        //解析token，如果解析失败，返回错误结果(未登录)
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解析失败，返回未登录信息：");
            Result error = Result.error("NOT_LOGIN");
            //手动转换  对象->json  （利用阿里巴巴fastJSON工具包）
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return;
        }
        Object value = redisTemplate.opsForValue().get(jwt);
        if(value instanceof Emp) {
            Emp emp = (Emp) redisTemplate.opsForValue().get(jwt);
            if (null == emp) {
                //如果用户信息不存在，拦截
                log.info("员工信息不存在");
                Result<Object> error = Result.error("该用户不存在");
                String notLogin = JSONObject.toJSONString(error);
                response.getWriter().write(notLogin);
                return;
            }
//            BaseThreadLocal.setCurrentId(Math.toIntExact(emp.getId()));
            //认证
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(emp.getEmpName(), emp.getPassword(), myEmpDetailsService.getEmpAuthority(emp.getId()));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            log.info("令牌合法，放行");
            chain.doFilter(request, response);
        }
    }


    private boolean isMatch(String uri, String[] patterns) {
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : patterns) {
            if (matcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }
}
