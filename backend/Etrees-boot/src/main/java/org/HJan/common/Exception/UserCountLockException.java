package org.HJan.common.Exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 锁定阈值异常处理
 */

public class UserCountLockException extends AuthenticationException {

    public UserCountLockException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserCountLockException(String msg) {
        super(msg);
    }
}
