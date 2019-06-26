package com.doosan.sb.app.response;
import com.doosan.sb.app.results.ResponseResults;
/**
 * 返回Json数据
 * @author 20112004
 *
 */
public class ResponseResultJson {
	/**
	 * 无结果返回成功
	 * @return
	 */
	public static ResponseResultObject success(){
		return success(null);
	}
	/**
	 * 有数据返回成功
	 * @param object
	 * @return
	 */
	public static ResponseResultObject success(Object object){
		ResponseResultObject resultOject = new ResponseResultObject(ResponseResults.SUCCESS);
		resultOject.setData(object);
		return resultOject;
	}
	/**
	 * 返回错误
	 * @param results
	 * @return
	 */
	public static ResponseResultObject error(ResponseResults results){
		ResponseResultObject resultOject = new ResponseResultObject(results);
		return resultOject;
	}
	
}
