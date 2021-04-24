package com.esportzoo.esport.controller.ws.client;


import com.esportzoo.common.util.NetUtil;
import com.esportzoo.esport.controller.ws.constants.NettyExecType;
import com.esportzoo.esport.controller.ws.jms.LoginWebPublisher;
import com.esportzoo.esport.util.JsonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.channelMap;
import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.usrIdAndChanIdMap;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * @author tingjun.wang
 * @date 2019/9/29 11:06
 */

public class LoginWebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
	private static final Logger logger = LoggerFactory.getLogger(LoginWebSocketServerHandler.class);

	private WebSocketServerHandshaker handshaker;

	private final String webSocketUri;
	
	@Autowired
	private LoginWebPublisher loginWebPublisher;
	private static final String WEBSOCKET_URI_ROOT_PATTERN = "ws://%s:%s";

	public LoginWebSocketServerHandler(String port) throws UnknownHostException, SocketException {
		 super();  
        this.webSocketUri = String.format(WEBSOCKET_URI_ROOT_PATTERN, NetUtil.getMyIp(), port);
	}
	

	/**
	 * 连接成功后会触发该方法
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		logger.info("netty-websocket,connected success");
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.info(" WebSocket消息：{}",msg.getClass());
		// 通过Http进行首次握手连接
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
		// WebSocket接入
		else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
		// 如果HTTP解码失败，返回HHTP异常
		if (!req.getDecoderResult().isSuccess() || (!"websocket".equalsIgnoreCase(req.headers().get("Upgrade")))) {
			logger.info("-handleHttpRequest- 连接信息异常,getDecoderResult：{}，Upgrade：{}",req.getDecoderResult().isSuccess(),req.headers().get("Upgrade"));
			sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
			return;
		}
		// 构造握手响应返回
		WebSocketServerHandshakerFactory wsFactory =
				new WebSocketServerHandshakerFactory(webSocketUri, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			logger.info("-handleHttpRequest- handshaker 为空");
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			logger.info("-handleHttpRequest- 握手处理");
			handshaker.handshake(ctx.channel(), req); //握手处理
		}
	}

	/**
	 *  主要处理websocket通道的业务关联，
	 *  1、一些与通道相关的初始化处理：如：当前用户与当前用户的通道进行关联、心跳处理
	 *  2、通过wsMessageHandlerDispatch根据场景类型找到对应到对应的场景处理handler类,进行具体的业务处理。
	 * @param ctx 获取通道
	 * @param frame 获取数据
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断是否是关闭链路的指令(连接关闭的操作)
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			logger.error("---netty.websocket.close---,接收到关闭链路的指令！正常关闭连接通道");
			return;
		}

		// 判断是否是Ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
        // 判断是否是Pong消息,ie的心跳会发送pong消息 经测试直接return不用处理即可
        if (frame instanceof PongWebSocketFrame) {
            return;
        }

		// 仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
		}

		// 拿到应答消息
		String msgJson = ((TextWebSocketFrame) frame).text();
		logger.info("scanLogin客户端发送的信息，channle：{}，sceneStr:{}", ctx.channel(),msgJson);
		Channel currChannel = ctx.channel();
		if (msgJson!=null) {
			usrIdAndChanIdMap.put(msgJson, currChannel.id());
		}
		logger.info("scanLogin客户端存入关联用户信息，sceneStr:{},usrIdAndChanIdMap:{}", msgJson,usrIdAndChanIdMap);
	}

	/**
	 * @Title: exceptionCaught
	 * @Description: 连接发生错误时候的操作
	 * @param @param cause 发生的错误
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("netty-websocket,发生异常！连接被迫关闭，异常信息：{}",cause.getMessage());
		cause.printStackTrace();
		ctx.close();
	}

	/**
	 * @Title: sendMessageToUser
	 * @Description:  发送消息给应用下的所有房间
	 * @param message 发送的消息
	 */
	public void pushMsgToAll(String message) {
		logger.info("channelMap:{}", channelMap);
		for (SocketChannel channel : channelMap.values()) {
			logger.info("channelMap:{}", channelMap.values());
			if (!Objects.isNull(channel)){
				logger.info("channel:{}", channel);
				channel.writeAndFlush(new TextWebSocketFrame(message));
			}
		}
	}

	/**
	 * @Title: pushSingleBysceneStr
	 * @Description:  发送消息给单个用户
	 * @param sceneStr
	 * @param message
	 */
	public boolean pushSingleBysceneStr(String sceneStr, String message) {
		logger.info("scanLogin服务器接收的信息，sceneStr:{},message:{}", sceneStr,message);
		ChannelId channelId = usrIdAndChanIdMap.get(sceneStr);
		logger.info("scanLogin服务器与用户关联的channelId:{}",channelId);
		if (!Objects.isNull(channelId)){
			SocketChannel socketChannel = channelMap.get(channelId);
			if(Objects.isNull(socketChannel)){
				logger.error("scanLogin服务器关联sceneStr【{}】没有对应的连接通道【失败fail】，用户信息为：【{}】,channelMap===>{}",sceneStr ,message, channelMap);
				return false;
			}
			socketChannel.writeAndFlush(new TextWebSocketFrame(message));
			logger.info("scanLogin服务器成功响应客户端，该用户信息:{}",message);
			return true;
		} else {
			if (sceneStr!=null) {
				logger.info("该服务器未找到该用户:{}websocket连接信息",sceneStr);
				try {
					loginWebPublisher.sendMessage(sceneStr, message);
				} catch (Exception e) {
					logger.info("发送MQ消息出现异常{}",e);
				}
			}
			logger.error("scanLogin服务器关联sceneStr【{}】没有对应的连接通道【失败fail】，用户信息为：【{}】",sceneStr ,message);
			return false;
		}
	}

	/**
	 * 构建心跳信息
	 * @return
	 */
	private String buildHeatBeat(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("execType", NettyExecType.HEART_BEAT.getIndex());
		String beatMsg = JsonUtil.map2StrJson(map);
		return beatMsg;
	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
		// 返回应答给客户端
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(),
					CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
			setContentLength(res, res.content().readableBytes());
		}

		// 如果是非Keep-Alive，关闭连接
		ChannelFuture f = ctx.channel().writeAndFlush(res);
		if (!isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

}
