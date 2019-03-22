package com.ba.nettyLearn.http.fileserver;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import static java.lang.System.out;
import static java.lang.System.err;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final String url;

	public HttpFileServerHandler(String url) {
		this.url = url;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		if (ctx.channel().isActive())
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		//
		if (!request.decoderResult().isSuccess()) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		if (request.method() != HttpMethod.GET) {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}

		// 解析url
		final String uri = request.uri();
		final String path = sanitizeUri(uri);
		if (path == null) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		if (file.isDirectory()) {
			if (uri.endsWith("/"))
				sendLists(ctx, file);
			else
				sendRedirect(ctx, uri + "/");
			return;
		}
		if (!file.isFile()) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		RandomAccessFile racfile = new RandomAccessFile(file, "r");
		long fileLength = racfile.length();
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

		response.headers().set("Content-Type", file.getPath());
		response.headers().set("Content-Length", fileLength);
		if (HttpUtil.isKeepAlive(request))
			response.headers().set("Connection", "keep-alive");
		ctx.write(response);
		ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(racfile, 0, fileLength, 8192),
				ctx.newProgressivePromise());
		sendFileFuture.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				out.println("Trasfer complete");
			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				if (total < 0)
					err.println("Transfer progress:" + progress);
				else
					err.print("Transfer progress:" + progress + "/" + total);
			}
		});
		ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if (!HttpUtil.isKeepAlive(request))
			lastContentFuture.addListener(ChannelFutureListener.CLOSE);
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set("Content-Type", "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private void sendLists(ChannelHandlerContext ctx, File dir) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		response.headers().set("Content-Type", "text/html;charset=UTF-8");
		StringBuilder sb = new StringBuilder();
		String dirPath = dir.getPath();
		sb.append("<!DOCTYPE html>\r\n");
		sb.append("<html><head><title>");
		sb.append(dirPath);
		sb.append("</title></head><body>\r\n");
		sb.append("<h3>");
		sb.append(dirPath);
		sb.append("</h3>\r\n");
		sb.append("<ul>");
		sb.append("<li><a href = \"../\">..</a></li>\r\n");
		for (File f : dir.listFiles()) {
			if (f.isHidden() || !f.canRead())
				continue;
			String name = f.getName();
			sb.append("<li><a href=\"" + name + "\">" + name + "</a></li>\r\n");
		}
		sb.append("</ul></bdoy></html>\r\n");
		ByteBuf buff = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);
		response.content().writeBytes(buff);
		buff.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private void sendRedirect(ChannelHandlerContext ctx, String uri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
		response.headers().set("Location", uri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	// 解析文件路径
	private String sanitizeUri(String uri) throws UnsupportedEncodingException {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw e1;
			}
		}
		if (!uri.startsWith(url))
			return null;
		if (!uri.startsWith("/"))
			return null;
		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + '/') || uri.contains('.' + File.separator) || uri.startsWith(".")
				|| uri.endsWith("."))
			return null;
		return System.getProperty("user.dir") + File.separator + uri;
	}

}
