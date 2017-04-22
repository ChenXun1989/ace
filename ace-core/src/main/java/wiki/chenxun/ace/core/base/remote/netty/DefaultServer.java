package wiki.chenxun.ace.core.base.remote.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import wiki.chenxun.ace.core.base.common.AceServerConfig;
import wiki.chenxun.ace.core.base.remote.Server;

import java.io.IOException;
import java.util.Observable;

/**
 * @Description: Created by chenxun on 2017/4/7.
 */
public class DefaultServer implements Server {

    private AceServerConfig aceServerConfig;

    /**
     * io线程
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * work线程
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup(100);

    /**
     * channelHandler注册
     */
    private ChannelInitializer channelInitializer = new HttpProtocolChannelInitializer();

    /**
     * 启动服务
     *
     * @throws Exception 异常
     */
    public void start() throws Exception {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(channelInitializer)
                    .option(ChannelOption.SO_BACKLOG, aceServerConfig.getBackSize())
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            ChannelFuture future = bootstrap.bind(aceServerConfig.getPort()).sync();
            System.out.println("ace server starter on port : " + aceServerConfig.getPort());
            future.channel().closeFuture().sync();
        } finally {
            close();
        }


    }


    /**
     * 关闭服务
     *
     * @throws IOException io异常
     */
    public void close()   {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    @Override
    public void setConfigBean(AceServerConfig aceServerConfig) {
        this.aceServerConfig = aceServerConfig;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
