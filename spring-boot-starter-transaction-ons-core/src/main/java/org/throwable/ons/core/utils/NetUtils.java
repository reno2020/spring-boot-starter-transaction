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
        return InetAddress.getLocalHost();
    }

    public static void main(String[] args) throws Exception{
        System.out.println(getLocalHostLanAddress().getHostAddress().hashCode());
    }
}
