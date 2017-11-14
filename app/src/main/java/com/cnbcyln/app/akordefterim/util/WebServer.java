package com.cnbcyln.app.akordefterim.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

//import com.cnbcyln.app.akordefterim.util.AkorDefterimSys;

import android.content.Context;
import android.content.SharedPreferences;

import com.cnbcyln.app.akordefterim.PageHandler;

@SuppressWarnings("deprecation")
public class WebServer extends Thread {

	//private AkorDefterimSys AkorDefterimSys;
	private ServerSocket ServerSocket;
	private SharedPreferences sharedPref;
	AkorDefterimSys AkorDefterimSys;

	private static final String SERVER_NAME = "AkorDefterimWebServer";
	private static final String ALL_PATTERN = "*";
	
	private boolean isRunning = false;
	private Context context = null;
	
	private BasicHttpProcessor httpproc = null;
	private BasicHttpContext httpContext = null;
	private HttpService httpService = null;
	private HttpRequestHandlerRegistry registry = null;

	public WebServer(Context context) {
		super(SERVER_NAME);
		this.setContext(context);

		AkorDefterimSys = new AkorDefterimSys(context);

		sharedPref = context.getSharedPreferences(AkorDefterimSys.PrefAdi, Context.MODE_PRIVATE);

		httpContext = new BasicHttpContext();

		httpproc = new BasicHttpProcessor();
		httpproc.addInterceptor(new ResponseDate());
		httpproc.addInterceptor(new ResponseServer());
		httpproc.addInterceptor(new ResponseContent());
		httpproc.addInterceptor(new ResponseConnControl());
		
		registry = new HttpRequestHandlerRegistry();
		
		httpService = new HttpService(httpproc, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());
		httpService.setHandlerResolver(registry);
	}

	@Override
	public void run() {
		super.run();

		try {
			ServerSocket = new ServerSocket();
			ServerSocket.setReuseAddress(true);
			ServerSocket.bind(new InetSocketAddress(Integer.parseInt(sharedPref.getString("prefWebYonetimPortNo", "9658"))));
			
			while (isRunning) {
				try {
					Socket clientSocket = ServerSocket.accept();

					DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();
					serverConnection.bind(clientSocket, new BasicHttpParams());
					registry.register(ALL_PATTERN, new PageHandler(context, clientSocket.getInetAddress().getHostAddress()));
					httpService.handleRequest(serverConnection, httpContext);
					serverConnection.shutdown();
				} catch (IOException | HttpException e) {
					e.printStackTrace();
				}
			}

			ServerSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		isRunning = false;
	}

	public synchronized void startThread() {
		isRunning = true;
		super.start();
	}

	public synchronized void stopThread() {
		isRunning = false;

		if (ServerSocket != null) {
			try {
				ServerSocket.close();
				this.interrupt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
}
