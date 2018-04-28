package top.gotoeasy.sample.rmi.sample2;

import top.gotoeasy.framework.rmi.server.RmiServerBuilder;

/**
 * 例子2的RMI服务端
 * 
 * @since 2018/03
 * @author 青松
 */
public class Sample2ServerMain {

    /**
     * 主入口方法
     * 
     * @param args 参数
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        RmiServerBuilder.get() // RMI服务创建器
                .server("127.0.0.1", 1099, "service") // rmi://127.0.0.1:1099/service
                .scan("top.gotoeasy.sample.rmi") // 扫描远程服务对象和方法
                .customTypeAnnotation(Sample2MyRmi.class) // 自定义远程类注解
                .customMethodAnnotation(Sample2RemoteMethod.class) // 自定义远程方法注解
                .strategy(new Sample2RemoteMethodNameShaStrategy()) // 自定义远程方法标识名策略
                .start(); // 启动RMI服务
    }

}
