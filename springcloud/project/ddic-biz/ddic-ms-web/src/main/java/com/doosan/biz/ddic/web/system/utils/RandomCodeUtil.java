package com.doosan.biz.ddic.web.system.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 生成随机码
 * @author 程国前
 * 20190311
 */
@Controller
@RequestMapping("/system/tool")
public class RandomCodeUtil {
	/**
	 * 生成随机码
	 * @param request
	 * @param response
	 * @param session
	 * @throws IOException
	 */
	@RequestMapping("/getRandomCode")
	public void getAuthCode(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
		int width = 80;
		  int height = 37;
		  Random random = new Random();
		  //设置response头信息
		  //禁止缓存
		  response.setHeader("Pragma", "No-cache");
		  response.setHeader("Cache-Control", "no-cache");
		  response.setDateHeader("Expires", 0);
		  //生成缓冲区image类
		  BufferedImage image = new BufferedImage(width, height, 1);
		  //产生image类的Graphics用于绘制操作
		  Graphics g = image.getGraphics();
		  //Graphics类的样式
		  g.setColor(new Color(240, 255, 240));
		  g.setFont(new Font("Times New Roman",0,28));
		  g.fillRect(0, 0, width, height);
		  //绘制干扰线
//		  for(int i=0;i<40;i++){
//		  		g.setColor(this.getRandColor(130, 200));
//		  		int x = random.nextInt(width);
//		  		int y = random.nextInt(height);
//		  		int x1 = random.nextInt(12);
//		  		int y1 = random.nextInt(12);
//		  		g.drawLine(x, y, x + x1, y + y1);
//		  }
		  // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		  StringBuffer randomCode = new StringBuffer();
		  int red = 0, green = 0, blue = 0;
		  // 随机产生codeCount数字的验证码。
		  for (int i = 0; i < codeCount; i++) {
				// 得到随机产生的验证码数字。
				String code = String.valueOf(codeSequence[random.nextInt(36)]);
				// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
				red = random.nextInt(255);
				green = random.nextInt(255);
				blue = random.nextInt(255);
				// 用随机产生的颜色将验证码绘制到图像中。
				g.setColor(new Color(25, 25, 112));
				g.drawString(code, (i + 1) * xx, codeY);
				// 将产生的四个随机数组合在一起。
				randomCode.append(code);
		  }
		  //绘制字符
		  String radomValidatorCd = randomCode.toString();
		  //将字符保存到session中用于前端的验证
		  session.setAttribute("radomValidatorCd", radomValidatorCd);
		  g.dispose();
		  ImageIO.write(image, "JPEG", response.getOutputStream());
		  response.getOutputStream().flush();
	}
	//创建颜色
	public Color getRandColor(int fc,int bc){
		Random random = new Random();
		if(fc>255)
		  fc = 255;
		if(bc>255)
		  bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r,g,b);
	}
	private static int codeCount = 4;// 定义图片上显示验证码的个数
	private static char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R','S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	private static int xx = 15;
	private static  int codeY = 25;
}