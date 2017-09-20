package org.throwable.ons.core.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2017/9/20 15:10
 */
public abstract class NetUtils {

    public static InetAddress getLocalHostLanAddress() throws Exception {
        InetAddress candidateAddress = null;
        // 遍历所有的网络接口
        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
            // 在所有的接口下再遍历IP
            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了
                        return inetAddr;
                    } else if (candidateAddress == null) {
                        // site-local类型的地址未被发现，先记录候选地址
                        candidateAddress = inetAddr;
                    }
                }
            }
        }
        if (null != candidateAddress) {
            return candidateAddress;
        }
        return InetAddress.getLocalHost();
    }

    public static void main(String[] args) throws Exception{
        System.out.println(getLocalHostLanAddress().getHostAddress().hashCode());
    }
}
