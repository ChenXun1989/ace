package wiki.chenxun.ace.core.base.remote.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
public class HttpProtocolChannelInitializer extends ChannelInitializer {

    /**
     * 请求体最大长度
     */
    public static final int MAX_CONTENT_LENGTH = 1024 * 10;

    /**
     *  初始化 chanelHandler
     * @param channel 请求通道
     * @throws Exception 异常基类
     */
    @Override
    public void initChannel(Channel channel) throws Exception {

        // http协议编码解码
        channel.pipeline().addLast(new HttpServerCodec());
        //合并成一个
        channel.pipeline().addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));

        channel.pipeline().addLast(new HttpServerInboundHandler());
    }
}
