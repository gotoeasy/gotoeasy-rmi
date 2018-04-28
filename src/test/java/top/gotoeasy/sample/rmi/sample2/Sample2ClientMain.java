package top.gotoeasy.sample.rmi.sample2;

import top.gotoeasy.framework.rmi.client.RmiClientProxy;

/**
 * 例子2的RMI客户端
 * 
 * @since 2018/03
 * @author 青松
 */
public class Sample2ClientMain {

    /**
     * 主入口方法
     * 
     * @param args 参数
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Sample2HelloRmi remote = RmiClientProxy.getProxy(Sample2HelloRmi.class, "rmi://127.0.0.1:1099/service",
                Sample2RemoteMethodNameShaStrategy.class);
        String msg = remote.hello("Sample2");
        System.out.println(msg);
    }

}
