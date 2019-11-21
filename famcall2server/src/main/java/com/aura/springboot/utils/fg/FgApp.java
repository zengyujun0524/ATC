package com.aura.springboot.utils.fg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aura.springboot.service.UserService;

/**
 * Hello world!
 *
 */
public class FgApp {
	static String requestUrl = "http://api.feige.ee/SmsService/Send";
	private static Logger log = LoggerFactory.getLogger(FgApp.class);

	public static void sendCode(String countryCode,String phone,Integer ran) {
		System.out.println("Hello World!");
		try {
			log.info("------------您发送的手机号为------------》" + countryCode + phone +"，验证码为：" + ran);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			formparams.add(new BasicNameValuePair("Account", "18814468057"));
			formparams.add(new BasicNameValuePair("Pwd", "b0ee2892080260aefce6e798c"));
			formparams.add(new BasicNameValuePair("Content", "尊敬的用户您好，您的验证码是：" + ran));
			formparams.add(new BasicNameValuePair("Mobile", phone));
			formparams.add(new BasicNameValuePair("SignId", "85875"));
			Post(formparams);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Post(List<NameValuePair> formparams) throws Exception {
		CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

		httpClient.start();

		HttpPost requestPost = new HttpPost(requestUrl);

		requestPost.setEntity(new UrlEncodedFormEntity(formparams, "utf-8"));

		httpClient.execute(requestPost, new FutureCallback<HttpResponse>() {

			public void failed(Exception arg0) {

				System.out.println("Exception: " + arg0.getMessage());
			}

			public void completed(HttpResponse arg0) {
				System.out.println("Response: " + arg0.getStatusLine());
				try {

					InputStream stram = arg0.getEntity().getContent();
					BufferedReader reader = new BufferedReader(new InputStreamReader(stram));
					System.out.println(reader.readLine());

				} catch (UnsupportedOperationException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

			public void cancelled() {
				// TODO Auto-generated method stub

			}
		}).get();

		System.out.println("Done");
	}

}
