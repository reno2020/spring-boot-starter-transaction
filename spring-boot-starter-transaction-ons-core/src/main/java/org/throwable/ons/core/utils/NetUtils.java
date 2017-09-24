package org.throwable.ons.core.utils;

import javax.management.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 15:10
 */
public abstract class NetUtils {

	public static InetAddress getLocalHostLanAddress() {
		try {
			if (isWindowsOS()) {
				return InetAddress.getLocalHost();
			} else {
				InetAddress candidateAddress = null;
				// 遍历所有的网络接口
				for (Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces(); networkInterfaces.hasMoreElements(); ) {
					NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
					// 在所有的接口下再遍历IP
					for (Enumeration inetAddresses = networkInterface.getInetAddresses(); inetAddresses.hasMoreElements(); ) {
						InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
						if (!inetAddress.isLoopbackAddress()) {// 排除loopback类型地址
							if (inetAddress.isSiteLocalAddress()) {
								// 如果是site-local地址，就是它了
								return inetAddress;
							} else if (null == candidateAddress) {
								// site-local类型的地址未被发现，先记录候选地址
								candidateAddress = inetAddress;
							}
						}
					}
				}
				if (null != candidateAddress) {
					return candidateAddress;
				}
			}
			return InetAddress.getLocalHost();
		} catch (Exception e) {
			//ignore
		}
		return null;
	}

	private static boolean isWindowsOS() {
		String osName = System.getProperty("os.name");
		return osName.toLowerCase().contains("windows");
	}

	public static String getServerPort(boolean secure) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
		MBeanServer mBeanServer = null;
		if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
			mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
		}
		if (mBeanServer == null) {
			return null;
		}
		Set<ObjectName> names;
		try {
			names = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
		} catch (Exception e) {
			return null;
		}
		Iterator<ObjectName> it = names.iterator();
		ObjectName objectName;
		while (it.hasNext()) {
			objectName = it.next();
			String protocol = (String) mBeanServer.getAttribute(objectName, "protocol");
			String scheme = (String) mBeanServer.getAttribute(objectName, "scheme");
			Boolean secureValue = (Boolean) mBeanServer.getAttribute(objectName, "secure");
			Boolean SSLEnabled = (Boolean) mBeanServer.getAttribute(objectName, "SSLEnabled");
			if (SSLEnabled != null && SSLEnabled) {// tomcat6开始用SSLEnabled
				secureValue = true;// SSLEnabled=true但secure未配置的情况
				scheme = "https";
			}
			if (protocol != null && ("HTTP/1.1".equals(protocol) || protocol.contains("http"))) {
				if (secure && "https".equals(scheme) && secureValue) {
					return (mBeanServer.getAttribute(objectName, "port")).toString();
				} else if (!secure && !"https".equals(scheme) && !secureValue) {
					return (mBeanServer.getAttribute(objectName, "port")).toString();
				}
			}
		}
		return null;
	}
}
