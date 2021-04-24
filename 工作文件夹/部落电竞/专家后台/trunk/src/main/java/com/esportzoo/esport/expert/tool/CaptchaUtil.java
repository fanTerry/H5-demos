package com.esportzoo.esport.expert.tool;  

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author jing.ming
 * @version 创建时间：2015年11月10日 上午11:06:25 
 * 程序的简单说明 
 */
public class CaptchaUtil {
	private static final Logger logger = LoggerFactory.getLogger("code");
	
	public static  Map<String,Object> getCaptcha(int width, int height) {
		 BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	        // 获取图形上下文
	        Graphics g = image.getGraphics();

	        // 生成随机类
	        Random random = new Random();
	        // 设定背景色
	        g.setColor(getRandColor(200, 250));
	        g.fillRect(0, 0, width, height);

	        // 设定字体
	        g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));

	        // 随机产生60条干扰线
	        g.setColor(getRandColor(180, 200));
	        for (int i = 0; i < 100; i++) {
	            int x = random.nextInt(width);
	            int y = random.nextInt(height);
	            int xl = random.nextInt(12);
	            int yl = random.nextInt(12);
	            g.drawLine(x, y, x + xl, y + yl);
	        }

	        // 取随机产生的认证码(4位数字)
	        String randCodeStr = randomCode(4);
	        char[] codeChar = randCodeStr.toCharArray();
	        for (int i = 0; i < codeChar.length; i++) {
	            // 将认证码显示到图象中
	            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
	            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
	            g.drawString(String.valueOf(codeChar[i]), 13 * i + 12, 22);
	        }
	        // 将认证码存入SESSION
	/*        UserCommonCacheMap cacheMap = getCacheMap(false, getSid());
	        cacheMap.put("validCode", randCodeStr);
	        setCacheMap(cacheMap, getSid());*/
	        // 图象生效
	        g.dispose();
	        Map<String,Object> map = new HashMap<String,Object>();
	        map.put("image", image) ;
	        map.put("code", randCodeStr) ;
	        return map;
	}
	
	private static Color getRandColor(int fc, int bc) {
       // 给定范围获得随机颜色
       Random random = new Random();
       if (fc > 255)
           fc = 255;
       if (bc > 255)
           bc = 255;
       int r = fc + random.nextInt(bc - fc);
       int g = fc + random.nextInt(bc - fc);
       int b = fc + random.nextInt(bc - fc);
       return new Color(r, g, b);
   }

   /**
    * 获得一个随机的数字+字母的字符串
    * @param length(随机字符长度)
    * @return
    * @create_time 2011-7-29
    */
   private static String randomCode(int length){
       StringBuffer stB=new StringBuffer();
       Random random=new Random();
       int r=0;
       for (int i = 0; i < length; i++) {
//           r=random.nextInt(2);
//           if (r==0) {//数字
//               stB.append(randomChar());
//           }else if(r==1) {//字母
//              stB.append(randomInt(10));
//               stB.append(randomChar());
//           }
           stB.append(String.valueOf(randomChar()).toUpperCase());

       }
       return stB.toString();
   }

   /**
    * 获得一个随机字母
    * @return
    * @create_time 2011-7-29
    */
   private static char randomChar(){
       Random random=new Random();
       int r=0;
       char temp;
       while(true){
           r=random.nextInt(123);
           if (r>=65&&r<=90&&r!=73&&r!=79&&r!=76) {//大写字母
               break;
           }else if(r>=97&&r<=122&&r!=105&&r!=108&&r!=111){//小写字母
               break;
           }
       }
       temp=(char)r;
       return temp;
   }

}
 