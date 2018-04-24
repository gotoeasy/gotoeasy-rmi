package top.gotoeasy.framework.rmi.client;

import java.lang.reflect.Method;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.Map;

import top.gotoeasy.framework.aop.SuperInvoker;
import top.gotoeasy.framework.aop.annotation.Aop;
import top.gotoeasy.framework.aop.annotation.Around;
import top.gotoeasy.framework.core.log.Log;
import top.gotoeasy.framework.core.log.LoggerFactory;
import top.gotoeasy.framework.rmi.server.RemoteInterface;
import top.gotoeasy.framework.rmi.strategy.RemoteMethodNameStrategy;

@Aop
public class AopAround {

	private static final Log							log			= LoggerFactory.getLogger(AopAround.class);

	private String										rmiUrl;
	private RemoteMethodNameStrategy					strategy;
	private static final Map<String, RemoteInterface>	mapRemote	= new HashMap<>();

	/**
	 * 构造器
	 * @param rmiUrl RMI服务的URL（rmi://地址:端口/服务名）
	 */
	public AopAround(String rmiUrl) {
		this.rmiUrl = rmiUrl;
		this.strategy = new RemoteMethodNameStrategy() {
		};
	}

	/**
	 * 构造器
	 * @param rmiUrl RMI服务的URL（rmi://地址:端口/服务名）
	 * @param strategy 标识名策略
	 */
	public AopAround(String rmiUrl, RemoteMethodNameStrategy strategy) {
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
	@Around
	public Object around(Object target, Method method, SuperInvoker superInvoker, Object ... args) {

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
