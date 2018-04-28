package top.gotoeasy.sample.rmi.sample2;

/**
 * 声明式提供RMI远程服务的例子2
 * @since 2018/03
 * @author 青松
 */
@Sample2MyRmi
public class Sample2HelloRmi {

    /**
     * RMI远程方法例子
     */
    @Sample2RemoteMethod
    public String hello(String name) {
        return "Hello " + name;
    }
}
