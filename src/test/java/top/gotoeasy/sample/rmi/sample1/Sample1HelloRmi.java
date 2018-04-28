package top.gotoeasy.sample.rmi.sample1;

import top.gotoeasy.framework.rmi.annotation.RemoteMethod;
import top.gotoeasy.framework.rmi.annotation.Rmi;

/**
 * 声明式提供RMI远程服务的例子1
 * <p/>
 * 类和方法必须同时声明
 * 
 * @since 2018/03
 * @author 青松
 */
@Rmi
public class Sample1HelloRmi {

    /**
     * RMI远程方法例子
     */
    @RemoteMethod
    public String hello(String name) {
        return "Hello " + name;
    }
}
