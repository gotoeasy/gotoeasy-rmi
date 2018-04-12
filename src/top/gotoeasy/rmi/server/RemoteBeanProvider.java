package top.gotoeasy.rmi.server;

/**
 * RMI远程Bean对象提供者接口
 * <p/>
 * 远程Bean对象需自定义提供时使用<br/>
 * 比如需增强或已存在于WebApp的Spring容器中等情况
 * @since 2018/03
 * @author 青松
 */
@FunctionalInterface
public interface RemoteBeanProvider {

    /**
     * 根据类提供远程Bean对象
     * @param cls
     * @return 远程Bean对象
     */
    public Object provide(Class<?> cls);

}
