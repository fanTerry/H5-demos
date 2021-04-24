package com.esportzoo.esport.controller.ws.server;

import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.channelMap;
import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.sceneAndUsrIdMap;
import static com.esportzoo.esport.controller.ws.constants.NettyChannelRelatedMaps.usrIdAndChanIdMap;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.memcached.MemcachedClient;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.NetUtil;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.controller.ws.constants.NettyExecType;
import com.esportzoo.esport.controller.ws.server.scene.SceneHandler;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.JsonUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;



public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);
    
    private WebSocketServerHandshaker handshaker;
    
    private final String webSocketUri;

    /*private MemcachedClient memcachedClient;*/
    
    private RedisClient redisClient;
    
    private static final String WEBSOCKET_URI_ROOT_PATTERN = "ws://%s:%s"; 
    
	private final static String PRESCENEKEY_PREFIX = "prescene_";
	
	@Autowired
	private UsrConsumerCacheManager usrConsumerCacheManager;
	
	@Autowired
	private WsMessageHandlerDispatch wsMessageHandlerDispatch;
	
    private static final String room_scene_usrid_map_cache ="app_sceneId_usrids_cache";
	
    public WebSocketServerHandler(String port, RedisClient redisClient) throws UnknownHostException, SocketException {  
        super();  
        this.webSocketUri = String.format(WEBSOCKET_URI_ROOT_PATTERN, NetUtil.getMyIp(), port);
        this.redisClient = redisClient;
    }
    
	public WebSocketServerHandler(String host, String port, RedisClient redisClient) throws UnknownHostException, SocketException {  
        super();  
        this.webSocketUri = String.format(WEBSOCKET_URI_ROOT_PATTERN, host, port);
        this.redisClient = redisClient;
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
		    sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
		    return;
		}
		QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());  
        Map<String, List<String>> param = decoder.parameters();  
        List<String> paramBySign= param.get("sign");
        HttpHeaders httpHeaders = req.headers();
        String reqHost = httpHeaders.get("Host");
        String reqUsrAgent = httpHeaders.get("User-Agent");
        logger.info("客户端请求信息：Host ===> {}，User-Agent===> {}", reqHost, reqUsrAgent);
        if(CollectionUtils.isEmpty(paramBySign)){
        	 throw new BusinessException("【" + reqHost + "】非法连接请求，拒绝【"+ reqUsrAgent + "】连接！, sign.is.null");
        }
        String sign = paramBySign.get(0);
        if(!usrConsumerCacheManager.checkSocketUrlSign(sign)){
        	 throw new BusinessException("【" +  reqHost + "】非法连接请求，拒绝【"+ reqUsrAgent + "】连接！");
        }
		// 构造握手响应返回
		WebSocketServerHandshakerFactory wsFactory = 
				new WebSocketServerHandshakerFactory(webSocketUri, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
		    WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
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
		
		// 仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
		    throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
		}

		// 拿到应答消息
		String msgJson = ((TextWebSocketFrame) frame).text();
		logger.info("--------- ws-server接收到信息，msgJson:{}", msgJson);
 		Channel currChannel = ctx.channel();
		JSONObject msgObj = JSONObject.parseObject(msgJson);
		if(Objects.isNull(msgObj)){
			 logger.error("handleWebSocketFrame-发送的文本消息不能为空");
			 return;
		}
		int execType = msgObj.getIntValue("execType");
		String playload = msgObj.getString("playload");
		JSONObject bodyObj = JSONObject.parseObject(playload);
		
		if(execType != 15)
			logger.info(String.format("%s handleWebSocketFrame.received %s", currChannel, msgJson));
		
		if(NettyExecType.INIT_USR_CHANNEL.getIndex() == execType) {
			String oldUsrId = "";
			String usrId = "";
			if(bodyObj != null){
				usrId = bodyObj.getString("usrId");
				oldUsrId = bodyObj.getString("oldUsrId");				
			}
			if(StringUtils.isBlank(usrId) || "null".equals(usrId)){
				 currChannel.writeAndFlush(new TextWebSocketFrame(buildlinkedRet("fail")));
				 throw new BusinessException("用户与连接通道进行关联时，用户usrId不能为空");
			}
			
			//新旧usrId切换（游客Id--->usrId、usrId--->游客Id）
			transformUsrIdIdWithOldUsrId(oldUsrId, usrId);
			usrIdAndChanIdMap.put(usrId, currChannel.id());

			//给客户端相应一个关联成功的信息
			currChannel.writeAndFlush(new TextWebSocketFrame(buildlinkedRet("success")));
			logger.info("用户【usrId：{}】与【通道：{}】相关联，usrIdAndChanIdMap==>{}", usrId, currChannel.id(), usrIdAndChanIdMap);
			return;
		}
		//忽略
		if(NettyExecType.HEART_BEAT.getIndex() == execType) {
			logger.debug("receive client heartbeat message:---->{}",msgJson);
			currChannel.writeAndFlush(new TextWebSocketFrame(buildHeatBeat()));
			return;
		}
		
		//切换场景并找到对应的handler处理类
		SceneHandler sceneHandler = wsMessageHandlerDispatch.changeSceneHanderDispatch(msgJson);
		//执行该场景内的相关动作
		sceneHandler.executeData(msgJson);
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
		for (SocketChannel channel : channelMap.values()) {
			if (!Objects.isNull(channel))
				channel.writeAndFlush(new TextWebSocketFrame(message));
		}
	}
	
	/**
	 * @Title: pushSingleByUsrId
	 * @Description:  发送消息给单个用户
	 * @param usrId
	 * @param message
	 */
	public boolean pushSingleByUsrId(String usrId, String message) {
		ChannelId channelId = usrIdAndChanIdMap.get(usrId);
		logger.info("pushSingleByUsrId-推送给单独用户usrId【{}】，发送数据message：{}，用户当前通道ChannelId【{}】，usrIdAndChanIdMap===>{}",usrId, message,channelId, usrIdAndChanIdMap);
		if (!Objects.isNull(channelId)){
			SocketChannel socketChannel = channelMap.get(channelId);
			if(Objects.isNull(socketChannel)){
				logger.error("pushSingleByUsrId-推送用户【{}】没有对应的连接通道【失败fail】，推送信息为：【{}】,channelMap===>{}",usrId ,message, channelMap);
				return false;
			}
			socketChannel.writeAndFlush(new TextWebSocketFrame(message));
			return true;
		} else {
			 logger.error("pushSingleByUsrId-推送用户【{}】没有对应的连接通道【失败fail】，推送信息为：【{}】",usrId ,message);
			 return false;
		}
	}
	
	private void transformUsrIdIdWithOldUsrId(String oldUsrId, String usrId) {
		if(StringUtils.isNotBlank(oldUsrId)) {
			logger.info("【用户切换Id-before,oldUsrId：{} ----> currUsrId：{}】当前netty服务usrIdAndChanIdMap==>{}，sceneAndUsrIdMap===>{}", oldUsrId,usrId,usrIdAndChanIdMap,sceneAndUsrIdMap);
			String preSceneId = redisClient.getObj(PRESCENEKEY_PREFIX + oldUsrId);
			if(StringUtils.isNotBlank(preSceneId)){
				redisClient.setObj(PRESCENEKEY_PREFIX + usrId, CachedKeyAndTimeLong.setHour(24), preSceneId);
				redisClient.del(PRESCENEKEY_PREFIX + oldUsrId);
				usrIdAndChanIdMap.remove(oldUsrId);
				
				//替换场景下的oldUsrId--->usrId
				for (String sceneKey : sceneAndUsrIdMap.keySet()) {
					CopyOnWriteArraySet<String> usrIdSet = sceneAndUsrIdMap.get(sceneKey);
					if(usrIdSet.contains(oldUsrId)){
						usrIdSet.remove(oldUsrId);
						usrIdSet.add(usrId);
					}
				}
			}
			logger.info("【用户切换Id-after,oldUsrId：{} ----> currUsrId：{}】当前netty服务usrIdAndChanIdMap==>{}，sceneAndUsrIdMap===>{}", oldUsrId,usrId,usrIdAndChanIdMap,sceneAndUsrIdMap);
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
	
	 private String buildlinkedRet(String ret) {
		 JSONObject json = new JSONObject();
		 json.put("execType", NettyExecType.INIT_USR_CHANNEL.getIndex());
		 if("fail".equals(ret)){
			 json.put("ret", "fail");
			 return json.toJSONString();
		 } else {
			 json.put("ret", "success");
			 return json.toJSONString();
		 }
	}
 
}
