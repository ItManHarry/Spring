package com.doosan.biz.ddic.hr.system;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpClientConfig;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.saf.model.v20180919.ExecuteRequestRequest;
import com.aliyuncs.saf.model.v20180919.ExecuteRequestResponse;

public class SafeTool {

    public static String getDomain(String regionId, boolean isVpc) {
        String product = "saf";

        if (isVpc) {
            product += "-vpc";
        }

        if ("cn-shanghai".equals(regionId)) {
            return product + ".cn-shanghai.aliyuncs.com";
        }
        if ("cn-hangzhou".equals(regionId)) {
            return product + ".cn-hangzhou.aliyuncs.com";
        }
        if ("cn-shenzhen".equals(regionId)) {
            return product + ".cn-shenzhen.aliyuncs.com";
        }
        if ("cn-zhangjiakou".equals(regionId)) {
            return product + ".cn-zhangjiakou.aliyuncs.com";
        }
        if ("cn-beijing".equals(regionId)) {
            return product + ".cn-beijing.aliyuncs.com";
        }

        return "saf.cn-shanghai.aliyuncs.com";
    }
    
    public static Map<String, String> doSafeAction(String mobile, String userName, String certNo) throws UnsupportedEncodingException {
    	// Client HTTPS配置
    	HttpClientConfig clientConfig = HttpClientConfig.getDefault();
    	// 设置不校验服务端证书
    	clientConfig.setIgnoreSSLCerts(true);
    	Map<String, String> result = new HashMap<String, String>();
    	IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FxVhkot1iiWuEGVPUv7", "3tLEojs1gc3zbP8W9GYtF0QiCQ81Zp");
        System.out.println("Domain is : " + getDomain("cn-hangzhou", false));
        DefaultProfile.addEndpoint("cn-hangzhou", "saf", getDomain("cn-hangzhou", false));
        IAcsClient client = new DefaultAcsClient(profile);
        System.out.println("1.Profile is ok.");
        ExecuteRequestRequest executeRequestRequest = new ExecuteRequestRequest();
        executeRequestRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        //服务的产品码：address_validation/email_risk/coupon_abuse/account_abuse等
        executeRequestRequest.setService("yd_application_fraud");
        // 业务详细参数，具体见文档里的业务参数部分,不需要的参数就不需要设置
        System.out.println("2.Prepare parameters.");
        Map<String, Object> serviceParams = new HashMap<String, Object>();
        // 调用参数
        serviceParams.put("mobile", mobile);
        serviceParams.put("userName", userName);
        serviceParams.put("certNo", certNo);
        executeRequestRequest.setServiceParameters(JSONObject.toJSONString(serviceParams));
        /**
         * 请务必设置超时时间
        */
        System.out.println("3.Set Request.");
        executeRequestRequest.setReadTimeout(3000);
        executeRequestRequest.setHttpContent(JSONObject.toJSONString(serviceParams).getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        System.out.println("4.Do Request.");
        try {
            System.out.println("5.Get response.");
        	ExecuteRequestResponse httpResponse = client.getAcsResponse(executeRequestRequest);
            System.out.println("6.Reqeuest finished.");
            httpResponse.getData().getScore();
            result.put("score", httpResponse.getData().getScore());
            result.put("tags", httpResponse.getData().getTags());
            result.put("data", JSONObject.toJSONString(httpResponse.getData()));
            result.put("message", JSONObject.toJSONString(httpResponse.getMessage()));
            result.put("code", JSONObject.toJSONString(httpResponse.getCode()));
            result.put("requestId", JSONObject.toJSONString(httpResponse.getRequestId()));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static String getScore(String mobile, String userName, String certNo) throws UnsupportedEncodingException {
    	// Client HTTPS配置
    	HttpClientConfig clientConfig = HttpClientConfig.getDefault();
    	// 设置不校验服务端证书
    	clientConfig.setIgnoreSSLCerts(true);
    	String score = "";
    	IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FxVhkot1iiWuEGVPUv7", "3tLEojs1gc3zbP8W9GYtF0QiCQ81Zp");
        DefaultProfile.addEndpoint("cn-hangzhou", "saf", getDomain("cn-hangzhou", false));
        IAcsClient client = new DefaultAcsClient(profile);
        ExecuteRequestRequest executeRequestRequest = new ExecuteRequestRequest();
        executeRequestRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        //服务的产品码：address_validation/email_risk/coupon_abuse/account_abuse等
        executeRequestRequest.setService("yd_application_fraud");
        // 业务详细参数，具体见文档里的业务参数部分,不需要的参数就不需要设置
        Map<String, Object> serviceParams = new HashMap<String, Object>();
        // 调用参数
        serviceParams.put("mobile", mobile);
        serviceParams.put("userName", userName);
        serviceParams.put("certNo", certNo);
        executeRequestRequest.setServiceParameters(JSONObject.toJSONString(serviceParams));
        /**
        * 请务必设置超时时间
        */
        executeRequestRequest.setReadTimeout(3000);
        executeRequestRequest.setHttpContent(JSONObject.toJSONString(serviceParams).getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        try {
        	ExecuteRequestResponse httpResponse = client.getAcsResponse(executeRequestRequest);
            score = httpResponse.getData().getScore();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	Map<String, String> safe = doSafeAction("13780924007", "程国前", "37082919831102623X");
    	System.out.println("score=" + safe.get("score"));
    	System.out.println("tags=" + safe.get("tags"));
    	System.out.println("data=" + safe.get("data"));
        System.out.println("message=" + safe.get("message"));
        System.out.println("code=" + safe.get("code"));
        System.out.println("requestId=" + safe.get("requestId"));
        String score = getScore("13780924007", "程国前", "37082919831102623X");
        System.out.println("Score is : " + score);
    }
}