package top.gotoeasy.rmi.strategy;

import java.lang.reflect.Method;

import top.gotoeasy.core.util.CmnMessageDigest;

/**
 * 远程方法标识名策略
 * @since 2018/03
 * @author 青松
 */
public interface RemoteMethodNameStrategy {

    /**
     * 远程方法标识名
     * <p/>
     * 默认MD5加密
     * @param method 方法
     * @return 远程方法标识名
     */
    public default String getName(Method method) {
        return CmnMessageDigest.md5(method.toGenericString());
    }
}
