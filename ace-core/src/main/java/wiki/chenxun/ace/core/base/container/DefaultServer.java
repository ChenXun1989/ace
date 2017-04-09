package wiki.chenxun.ace.core.base.container;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */

@Service
public class DefaultServer implements Server {
    /**
     * 请求等待队列长度
     */
    public static final int BACK_SIZE = 1024;
    /**
     *http协议默认端口
     */
    private final int  port = 8080;
    /**
     *  io线程
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     *  work线程
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * channelHandler注册
     */
    @Autowired
    private ChannelInitializer channelInitializer;

    /**
     * 启动服务
     * @throws Exception 异常
     */
    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, BACK_SIZE)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            close();
        }


    }

    /**
     *  关闭服务
     * @throws IOException io异常
     */
    @PreDestroy
    public void close() throws IOException {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
