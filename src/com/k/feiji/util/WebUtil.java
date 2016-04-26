package com.k.feiji.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class WebUtil {
	public static String COMMON_PATH = "samwangzhibo.xicp.net:17955";
	public static RequestQueue requestQueue;
	public static void init(Context context){
		/**
		 * common data
		 */
		if (requestQueue != null) requestQueue.stop();
		requestQueue = Volley.newRequestQueue(context);
		requestQueue.start();
	}
	public static RequestQueue getRequestQueue(){
		return requestQueue;
	}

}
