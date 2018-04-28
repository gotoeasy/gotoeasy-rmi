package top.gotoeasy.sample.rmi.sample2;

import java.lang.reflect.Method;

import top.gotoeasy.framework.core.util.CmnMessageDigest;
import top.gotoeasy.framework.rmi.strategy.RemoteMethodNameStrategy;

/**
 * 自定义的远程方法标识名策略例子
 * 
 * @since 2018/03
 * @author 青松
 */
public class Sample2RemoteMethodNameShaStrategy implements RemoteMethodNameStrategy {

    /**
     * 远程方法标识名
     * <p/>
     * 采用SHA加密
     * 
     * @param method 方法
     * @return 远程方法标识名
     */
    @Override
    public String getName(Method method) {
        return CmnMessageDigest.sha(method.toGenericString());
    }
}
