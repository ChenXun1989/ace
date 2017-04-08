package wiki.chenxun.ace.core.base.container;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.*;
import org.springframework.stereotype.Component;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
@Component("httpProtocolChannelInitializer")
public class HttpProtocolChannelInitializer extends ChannelInitializer {



    protected void initChannel(Channel channel) throws Exception {

        // http协议编码解码
        channel.pipeline().addLast(new HttpServerCodec());
        //合并成一个
        channel.pipeline().addLast(new HttpObjectAggregator(1024*10));

        channel.pipeline().addLast(new HttpServerInboundHandler());
    }
}
