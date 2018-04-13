package top.gotoeasy.framework.rmi.server;

import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.gotoeasy.framework.core.util.CmnClass;

/**
 * 远程服务
 * @since 2018/03
 * @author 青松
 */
public class RemoteDispatcher extends UnicastRemoteObject implements RemoteInterface {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	log					= LoggerFactory.getLogger(RemoteDispatcher.class);

	private Map<String, Object>	mapBean				= new HashMap<>();
	private Map<String, Method>	mapMethod;
	private RemoteBeanProvider	provider;

	/**
	 * 默认构造器
	 * @throws RemoteException 异常
	 */
	public RemoteDispatcher() throws RemoteException {
		super();
	}

	/**
	 * 设定远程Bean对象提供者
	 * @param provider 远程Bean对象提供者
	 */
	public void setRemoteBeanProvider(RemoteBeanProvider provider) {
		log.debug("设定远程Bean对象提供者{}", provider);
		this.provider = provider;
	}

	/**
	 * 设定远程方法
	 * @param mapMethod 远程方法
	 */
	public void setMapMethod(Map<String, Method> mapMethod) {
		log.debug("设定远程方法{}", mapMethod);
		this.mapMethod = mapMethod;
	}

	/**
	 * 远程调用
	 * @param name 远程方法标识名
	 * @param args 远程调用参数
	 * @throws RemoteException 异常
	 */
	@Override
	public Object execute(String name, Object ... args) throws RemoteException {

		log.debug("start [{}]({})", name, args);

		Method method = mapMethod.get(name);
		if ( method == null ) {
			log.error("指定方法不支持远程调用：{}", name);
			throw new UnsupportedOperationException("指定方法不支持远程调用：" + name);
		}

		Object bean = mapBean.get(name);
		if ( bean == null ) {
			Class<?> beanClass = method.getDeclaringClass();
			if ( provider == null ) {
				provider = cls -> CmnClass.createInstance(cls, null, null);
				log.debug("没有特定RemoteBeanProvider，按无参数构造器创建远程Bean对象", name, args);
			} else {
				log.debug("使用特定对象提供者RemoteBeanProvider取远程Bean对象", name, args);
			}

			bean = provider.provide(beanClass);
			if ( bean == null ) {
				log.error("对象提供者RemoteBeanProvider返回空对象：{}", name);
				throw new RemoteException("对象提供者RemoteBeanProvider返回空对象：" + name);
			}
			mapBean.put(name, bean); // Bean缓存
		}

		try {
			return method.invoke(bean, args);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RemoteException(e.getMessage(), e);
		} finally {
			log.debug("end   [{}]({})", name, args);
		}
	}

}
