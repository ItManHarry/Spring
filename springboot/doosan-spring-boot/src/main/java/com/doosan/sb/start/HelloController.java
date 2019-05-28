package com.doosan.sb.start;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@RequestMapping("/starter")
public class HelloController {

	@RequestMapping("/hello")
	@ResponseBody
	public Map<String, Object> helle(){
		result.put("name", "Harry");
		result.put("age", 36);
		return result;
	}
	
	private Map<String, Object> result = new HashMap<String, Object>();
}