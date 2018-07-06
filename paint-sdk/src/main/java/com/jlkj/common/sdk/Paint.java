package com.jlkj.common.sdk;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.support.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Shang
 */
public class Paint {
    private static final String NAME_FONT = ".萍方-简";
    private static final String POSITION_FONT = ".萍方-简";
    private static final String MOBILE_FONT = ".萍方-简";
    private static final String COMOANY_NAME_FONT = ".萍方-简";
    private static final String ADDRESS_FONT = ".萍方-简";

    private static final Integer NAME_FONT_SIZE = 40;
    private static final Integer POSITION_FONT_SIZE = 24;
    private static final Integer MOBILE_FONT_SIZE= 36;
    private static final Integer COMOANY_NAME_FONT_SIZE = 24;
    private static final Integer ADDRESS_FONT_SIZE = 24;

    //位置
    private static final int CODE_URL_SIZE = 224;
    private static final int CODE_URL_X = 263;
    private static final int CODE_URL_Y = 489;
    private static final int PHOTO_SIZE = 84;

    public static byte[] cPoster(String format, Map map) throws IOException {
       //获取背景图
       StringBuilder coverFileBuild = new StringBuilder();
        String root = RequestContext.class.getResource("/").getFile();
        coverFileBuild.append(root).append("model/").append("model.png");
        String coverFile = coverFileBuild.toString();
            File icon = new File(coverFile);
            BufferedImage img = null;
            if(icon.exists()){
                img = ImageIO.read(icon);
            }else {
                return null;
            }
        BufferedImage BufImage = new BufferedImage((int)img.getWidth(), (int)img.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = BufImage.createGraphics();
            //绘制背景图
        double width = img.getWidth();
        double high = img.getHeight();
        gs.drawImage(img, 0, 0, (int)width, (int)high, null);
        System.out.println("绘制背景图");
        //用户二维码
        String userCodeUrl = (String) map.get("codeUrl");
        if (StringUtil.isNotEmpty(userCodeUrl)){
            InputStream userCodeInput = getInputStreamByUrl(userCodeUrl);
            BufferedImage userCodeImg = ImageIO.read(userCodeInput);
            int two_high =CODE_URL_SIZE;
            int two_width = CODE_URL_SIZE;
            BufferedImage convertImage = convertCircular(userCodeImg);
            gs.drawImage(convertImage, CODE_URL_X, CODE_URL_Y, two_width, two_high, null);
            System.out.println("绘制二维码");
        }
            //头像
        String photoUrl = (String) map.get("photoUrl");
        int trird_high = PHOTO_SIZE;
        int trird_width = PHOTO_SIZE;
        if (StringUtil.isNotEmpty(photoUrl)){
            InputStream photoInput = getInputStreamByUrl(photoUrl);
            BufferedImage photoImg = ImageIO.read(photoInput);
            BufferedImage photoImage = convertCircular(photoImg);
            gs.drawImage(photoImage, (int) (trird_width*1.628), (int) (trird_high*3.47), trird_width, trird_high, null);
            //拼接文字
            Color color = Color.white;
            gs.setColor(color);
            System.out.println("绘制头像");
        }

        //1.名字
        String name = (String) map.get("name");
        if (StringUtil.isEmpty(name)){
            name = "";
        }
        writeText(gs,NAME_FONT_SIZE,NAME_FONT,name,(int) (trird_width*2.85), (int) (trird_high*3.95));
        System.out.println("绘制名字");
        //2.职位
        String position = (String) map.get("position");
        if (StringUtil.isEmpty(position)){
            position = "";
        }
        writeText(gs,POSITION_FONT_SIZE,POSITION_FONT,position,(int) (trird_width*2.85), (int) (trird_high*4.4));
        System.out.println("绘制职位");
        //3.地址
        int hithX = (int) (trird_high*11.7);
        String address = (String) map.get("address");
        if (StringUtil.isEmpty(address)){
            address = "";
        }
        Font font1=new Font(ADDRESS_FONT,Font.PLAIN,ADDRESS_FONT_SIZE);
        int hithX1 = printText(address,hithX, (int) (COMOANY_NAME_FONT_SIZE*5.125),width,gs,font1);
        System.out.println("绘制地址");
        // 4.公司名
        if (StringUtil.isEmpty(address)){
            hithX1 = 943;
        }
        String companyName = (String) map.get("companyName");
        if (StringUtil.isEmpty(companyName)){
            companyName = "";
        }
        Font font=new Font(COMOANY_NAME_FONT,Font.PLAIN,COMOANY_NAME_FONT_SIZE);
        int hithX2 = printText(companyName,hithX1, (int) (COMOANY_NAME_FONT_SIZE*5.125),width,gs,font);
        System.out.println("绘制公司名");
        //5.手机号码
        if (StringUtil.isEmpty(address)&& StringUtil.isEmpty(companyName)){
            hithX2 = 904;
        }
        String mobile = (String) map.get("mobile");
        if (StringUtil.isEmpty(mobile)){
            mobile = "";
        }
        StringBuilder mobileStr = new StringBuilder();
        if (mobile.length() >7){
            mobileStr.append(mobile.substring(0,3)).append(" ").append(mobile.substring(3,7)).append(" ").append(mobile.substring(7,mobile.length()));
        }
        mobile = mobileStr.toString();
        writeText(gs,MOBILE_FONT_SIZE,MOBILE_FONT,mobile, (int) (COMOANY_NAME_FONT_SIZE*5.125), hithX2);
        System.out.println("绘制手机号码");
            //释放对象
            gs.dispose();
            BufImage.flush();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try{
                if (!ImageIO.write(BufImage, format,out)) {
                    throw new IOException("Could not write an image of format "
                            + format + " to ");
                }
                byte[] bytes =out.toByteArray();
                return bytes;
            }catch (IOException e){
                throw  e;
            }finally {
                out.flush();
                out.close();
            }

        }

    private static int printText(String textStr,int hithX,int trird_width,double width,Graphics2D gs,Font font) {
        if (StringUtil.isNotEmpty(textStr)){
            FontMetrics fm = gs.getFontMetrics(font);
            int textWidth = fm.stringWidth(textStr);
            System.out.println("输入的字体长度:"+textWidth);

            List<String> testList = new ArrayList<>();
            int tWidth = (int)(width-10.25*font.getSize());//每行显示的长度
            int index = (int) Math.ceil(tWidth/(font.getSize()));

            char[] charArray = textStr.toCharArray();
            StringBuilder textTemp = new StringBuilder();
            int count = 0;
            int total = 0;
            for (char temp : charArray) {
                count += isCnorEn(temp);
                total += 1;
                textTemp = textTemp.append(temp);
                if (total == charArray.length || count*font.getSize()/2 > tWidth-font.getSize()){
                    String text = textTemp.toString();
                    testList.add(text);
                    textTemp = new StringBuilder();
                    count = 0;
                }

            }
            //倒叙排列
            Collections.reverse(testList);
            //按顺序绘制文字内容
            for (String text : testList) {
                if(StringUtils.isNotEmpty(text)){
                    if(!"<br>".equals(text)){ //<br>只换行，不打印内容
                        writeText(gs,font.getSize(),font.getFontName(),text,trird_width, hithX);
                    }
                }
                hithX -= 1.6*(font.getSize());
            }
        }
        return hithX;
    }

    public static int isCnorEn(char c){
        if(c >= 0x0391 && c <= 0xFFE5) //中文字符
            return 2;
        if(c>=0x0000 && c<=0x00FF){ //英文字符
            return 1;
        }
        return 3;
    }

    private static void writeText(Graphics2D gs,Integer fontSize,String fontName,String text,int width,int high) {
        Font font=new Font(fontName,Font.PLAIN,fontSize);
        gs.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gs.setFont(font);
        gs.drawString(text,width, high);
    }

    /**
     * 传入的图像必须是正方形的 才会 圆形 如果是长方形的比例则会变成椭圆的
     *
     * 用户头像地址
     * @return
     * @throws IOException
     */
    public static BufferedImage convertCircular(BufferedImage bi1) throws IOException {

//      BufferedImage bi1 = ImageIO.read(new File(url));

        // 这种是黑色底的
//      BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 透明底的图片
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1.getHeight());
        Graphics2D g2 = bi2.createGraphics();
        g2.setClip(shape);
        // 使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);
        // 设置颜色
        g2.setBackground(Color.green);
        g2.dispose();
        return bi2;
    }


    public static InputStream getInputStreamByUrl(String strUrl){
            try {
                URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
                return inStream;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
}
