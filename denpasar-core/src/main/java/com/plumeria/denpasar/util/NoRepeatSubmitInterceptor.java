package com.plumeria.denpasar.util;


import com.qbao.store.core.exception.BizException;
import com.qbao.store.core.web.servlet.AppSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chenwei on 2016/9/1.
 */
public class NoRepeatSubmitInterceptor implements HandlerInterceptor {

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private AppSession appSession;

    private ThreadLocal<RepeatSubmitConfig> localKeyMap = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            NoRepeatSubmit repeatSubmit = handlerMethod.getMethod().getAnnotation(NoRepeatSubmit.class);
            if (null == repeatSubmit) {
                return true;
            }
            RepeatSubmitConfig repeatSubmitConfig = new RepeatSubmitConfig(repeatSubmit);

            String submitKey = uniqueSubmitKey(request, repeatSubmitConfig.getKeys());
            if (hasLock(submitKey, repeatSubmitConfig.getTime())) {
                repeatSubmitConfig.setLockKey(submitKey);
                localKeyMap.set(repeatSubmitConfig);
                return true;
            }
            log.debug("请求被拦截，用户重复提交！");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-type", "charset=UTF-8");
            response.getWriter().write("{\"responseCode\":1005,\"errorMsg\":\"请求过于频繁！\"}");
            return false;
        }
        return true;
    }

    private boolean hasLock(String submitKey, int time) {
        return redisUtil.incrWithExpire(submitKey, time) == 1L;
    }

    private String uniqueSubmitKey(HttpServletRequest request, String[] keys) throws NoSuchAlgorithmException,
            UnsupportedEncodingException, BizException {
        if (keys.length == 0) {
            throw new BizException("重复提交参数异常");
        }

        StringBuffer lockKey = new StringBuffer();

        List<Integer> offset = offset(keys.length);
        getKeyFromRequest(offset, request, lockKey, keys);
        getKeyFromCookie(offset, request, lockKey, keys);
        getKeyFromHeader(offset, request, lockKey, keys);
        getKeyFromSession(offset, request, lockKey, keys);

        if (!offset.isEmpty()) {
            log.debug("参数{}没有定义", offset.get(0));
            throw new BizException("参数未定义");
        }

        log.debug("{}", lockKey.toString());

        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update(lockKey.toString().getBytes("UTF-8"));
        byte[] digest = sha.digest();
        String key = new sun.misc.BASE64Encoder().encode(digest);
        return key;
    }

    private void getKeyFromSession(List<Integer> offset, HttpServletRequest request, StringBuffer lockKey, String[]
            keys) {
        if (offset.isEmpty()) {
            return;
        }
        Iterator<Integer> iterator = offset.iterator();
        while (iterator.hasNext()) {
            Integer idx = iterator.next();
            String key = keys[idx];
            if (StringUtils.isNotBlank(key) && key.equals("userId") && appSession.getUserId() != null) {
                lockKey.append(appSession.getUserId());
                iterator.remove();
            }
        }
    }

    private void getKeyFromHeader(List<Integer> offset, HttpServletRequest request, StringBuffer lockKey, String[]
            keys) {
        if (offset.isEmpty()) {
            return;
        }

        Iterator<Integer> iterator = offset.iterator();
        while (iterator.hasNext()) {
            Integer idx = iterator.next();
            if (StringUtils.isNotBlank(request.getHeader(keys[idx]))) {
                lockKey.append(request.getHeader(keys[idx]));
                iterator.remove();
            }
        }
    }

    private void getKeyFromCookie(List<Integer> offset, HttpServletRequest request, StringBuffer lockKey, String[]
            keys) {
        if (offset.isEmpty()) {
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length == 0) {
            return;
        }

        Iterator<Integer> iterator = offset.iterator();
        while (iterator.hasNext()) {
            Integer idx = iterator.next();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(keys[idx])) {
                    lockKey.append(cookie.getValue());
                    iterator.remove();
                }
            }
        }
    }

    private void getKeyFromRequest(List<Integer> offset, HttpServletRequest request, StringBuffer lockKey, String[]
            keys) {
        Iterator<Integer> iterator = offset.iterator();
        while (iterator.hasNext()) {
            Integer idx = iterator.next();
            if (StringUtils.isNotBlank(request.getParameter(keys[idx]))) {
                lockKey.append(request.getParameter(keys[idx]));
                iterator.remove();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView mv)
            throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e)
            throws Exception {
        RepeatSubmitConfig repeatSubmitConfig = localKeyMap.get();
        if (repeatSubmitConfig != null && !repeatSubmitConfig.isFix() && StringUtils.isNotBlank(repeatSubmitConfig
                .getLockKey())) {
            redisUtil.del(repeatSubmitConfig.getLockKey());
            log.debug("返回用户请求，释放key:{}", repeatSubmitConfig.getLockKey());
        }
    }

    private List<Integer> offset(int length) {
        List<Integer> offset = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            offset.add(i, i);
        }
        return offset;
    }

    private class RepeatSubmitConfig {

        private int time;

        private String value;

        private String[] keys;

        private boolean fix;

        private String lockKey;

        public RepeatSubmitConfig(NoRepeatSubmit repeatSubmit) {
            this.time = repeatSubmit.time();
            this.value = repeatSubmit.value();
            this.keys = repeatSubmit.keys();
            this.fix = repeatSubmit.fix();
        }

        public String getLockKey() {
            return lockKey;
        }

        public void setLockKey(String lockKey) {
            this.lockKey = lockKey;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String[] getKeys() {
            return keys;
        }

        public void setKeys(String[] keys) {
            this.keys = keys;
        }

        public boolean isFix() {
            return fix;
        }

        public void setFix(boolean fix) {
            this.fix = fix;
        }
    }

}
