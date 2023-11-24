package github.wx.v4.util;

import github.wx.v4.util.SpringContextUtil;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author wx
 * @description  获取 ip 地址和端口号
 * @date 2023/11/16 11:23
 */
public class MessageSourceUtil {

    public static String getMsgSource() throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        Environment env = SpringContextUtil.getBean(Environment.class);
        String port = env.getProperty("server.port");
        return hostAddress + ":" + port;
    }
}
