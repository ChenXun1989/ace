package wiki.chenxun.ace.core.base.remote.netty;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.AsciiString;
import wiki.chenxun.ace.core.base.common.AceApplicationConfig;
import wiki.chenxun.ace.core.base.common.ExtendLoader;
import wiki.chenxun.ace.core.base.config.ConfigBeanAware;
import wiki.chenxun.ace.core.base.config.ConfigBeanParser;
import wiki.chenxun.ace.core.base.config.DefaultConfig;
import wiki.chenxun.ace.core.base.remote.Dispatcher;

import java.util.Observable;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Description: Created by chenxun on 2017/4/8.
 */
public class HttpServerInboundHandler extends ChannelInboundHandlerAdapter implements ConfigBeanAware<AceApplicationConfig> {

    public static final String APPLICATION_JSON = "application/json";
    /**
     * 请求分发与处理类
     */
    private Dispatcher dispatcher;

    private AceApplicationConfig aceApplicationConfig;

    /**
     * http协议header属性
     */
    private static final AsciiString CONTENT_TYPE = new AsciiString("Content-Type");
    /**
     * http协议header属性
     */
    private static final AsciiString CONTENT_LENGTH = new AsciiString("Content-Length");
    /**
     * http协议header属性
     */
    private static final AsciiString CONNECTION = new AsciiString("Connection");
    /**
     * Http协议header属性
     */
    private static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ConfigBeanParser configBeanParser = DefaultConfig.INSTANCE.configBeanParser(AceApplicationConfig.class);
        AceApplicationConfig config = (AceApplicationConfig) configBeanParser.getConfigBean();
        setConfigBean(config);
        configBeanParser.addObserver(this);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            Object response = null;
            try {
                response = dispatcher.doDispatcher(request);
            } catch (Exception ex) {
                // TODO: 异常处理
                ex.printStackTrace();
            }
            ObjectMapper om = new ObjectMapper();
            String jsonStr = om.writer().writeValueAsString(response);


            sendResponse(ctx, request, jsonStr);
        }

    }

    /**
     * 响应HTTP的请求
     *
     * @param ctx     ChannelHandlerContext
     * @param req     FullHttpRequest
     * @param jsonStr String
     */
    private void sendResponse(ChannelHandlerContext ctx, FullHttpRequest req, String jsonStr) {
        boolean keepAlive = HttpUtil.isKeepAlive(req);
        byte[] jsonByteByte = jsonStr.getBytes();
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(jsonByteByte));
        response.headers().set(CONTENT_TYPE, APPLICATION_JSON);
        response.headers().setInt(CONTENT_LENGTH, response.content().readableBytes());
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // FIXME: 开发调试
        // System.err.println(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }


    @Override
    public void setConfigBean(AceApplicationConfig aceApplicationConfig) {
        this.aceApplicationConfig = aceApplicationConfig;
        dispatcher = ExtendLoader.getExtendLoader(Dispatcher.class).getExtension(aceApplicationConfig.getDispatch());
    }


    @Override
    public void update(Observable o, Object arg) {

    }
}
