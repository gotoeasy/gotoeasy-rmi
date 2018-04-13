package top.gotoeasy.framework.rmi.client;

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import top.gotoeasy.framework.rmi.server.RemoteInterface;
import top.gotoeasy.framework.rmi.strategy.RemoteMethodNameStrategy;

/**
 * RMI客户端拦截类
 * @since 2018/03
 * @author 青松
 */
public class ClientInterceptor implements MethodInterceptor {

	private static final Logger							log			= LoggerFactory.getLogger(ClientInterceptor.class);

	private String										rmiUrl;
	private RemoteMethodNameStrategy					strategy;
	private static final Map<String, RemoteInterface>	mapRemote	= new HashMap<>();

	/**
	 * 构造器
	 * @param rmiUrl RMI服务的URL（rmi://地址:端口/服务名）
	 */
	public ClientInterceptor(String rmiUrl) {
		this.rmiUrl = rmiUrl;
		this.strategy = new RemoteMethodNameStrategy() {
		};
	}

	/**
	 * 构造器
	 * @param rmiUrl RMI服务的URL（rmi://地址:端口/服务名）
	 * @param strategy 标识名策略
	 */
	public ClientInterceptor(String rmiUrl, RemoteMethodNameStrategy strategy) {
		this.rmiUrl = rmiUrl;
		this.strategy = strategy;
	}

	/**
	 * 拦截并做远程调用
	 * <p>
	 * 不做可否远程调用的检查（比如有无指定注解），便于服务端用非注解式发布
	 * </p>
	 * @param target 对象
	 * @param method 方法
	 * @param args 远程调用参数
	 * @param proxy 方法代理
	 */
	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {

		if ( "#equals#getClass#hashCode#notify#notifyAll#toString#wait#".contains("#" + method.getName() + "#") ) {
			// Object方法不做代理
			return proxy.invokeSuper(target, args);
		}

		try {
			log.debug("远程调用开始，方法：{}，参数：{}", method, args);

			RemoteInterface remote = mapRemote.get(rmiUrl);
			if ( remote == null ) {
				remote = (RemoteInterface)Naming.lookup(rmiUrl); // 远程接口
				mapRemote.put(rmiUrl, remote);
			}
			Object result = remote.execute(strategy.getName(method), args); // 远程调用

			log.debug("远程调用完成，返回结果：{}", result);
			return result;
		} catch (Exception e) {
			mapRemote.remove(rmiUrl);

			log.error("远程调用异常，方法：{}，参数：{}", method, args);
			throw new RuntimeException(e);
		}

	}

}
