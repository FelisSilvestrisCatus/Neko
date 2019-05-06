package neko.neko;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Classteacher;
import neko.service.IClassService;
import neko.service.IClassteacherService;
import neko.service.IVacateService;
import neko.service.IVacatefilesService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NekoApplicationTests {
    static {

        System.loadLibrary("opencv_java401");
    }

    @Test
    public void contextLoads() throws IOException {


//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("uid",1);
//        //获取老师创建的班级
//        List<Classteacher> classlistbyuid=classteacherServic.list(queryWrapper);
//        Iterator it = classlistbyuid.iterator();
//        List<Integer> cid_list=new ArrayList<>();
//        while(it.hasNext()) {
//            int cid= ( (Classteacher)it.next()).getCid();
//            cid_list.add(cid);
//        }
//        System.out.println("该老师开的总共课程个数"+cid_list.size());
//        QueryWrapper queryWrapper1 = new QueryWrapper();
//        queryWrapper1.in("cid",cid_list);
//        List<Class> classList=classService.list(queryWrapper1);
//        System.out.println("打印"+classList.size());
        URL u = new URL("http://apis.juhe.cn/simpleWeather/query?city=临沂&key=f5e272f5f52ffb93a0902474025efb16");
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        String result = new String(b, "utf-8");

        //可以使用fastjson解析

        JSONObject json = JSONObject.parseObject(result);
        System.out.println(json);

        String now = JSONObject.parseObject(json.getString("result")).getString("realtime");
        JSONObject now_ = JSONObject.parseObject(now);
        now_.remove("wid");
        now_.remove("aqi");

        System.out.println(now_);

//
//        Date date = new Date();
//        int[] weekDays = {7, 1, 2, 3, 4, 5, 6};
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        int cday = weekDays[w];
//        System.out.println(cday);


    }

    @Test
    public void facedetection() {
        int flag=0;
        Rect rect_cut = new Rect();// 裁剪后的
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Size dsize = new Size(92, 112);
        CascadeClassifier faceDetector = new CascadeClassifier("./lbpcascade_frontalface.xml");
        Mat image = Imgcodecs.imread("C:\\Users\\13395\\Desktop\\ww.png");
        System.out.println("图片" + image.size());
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
         flag = faceDetections.toArray().length;
        System.out.println("人脸有几个" + flag
        );
        if (flag > 1) {

        }
        Rect rect = faceDetections.toArray()[0];
//            // 用绿色框匡助

        Imgproc.rectangle(image, new Point(rect.x, rect.y),
                new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        rect_cut.x = rect.x;
        rect_cut.y = rect.y;
        rect_cut.width = rect.width;
        rect_cut.height = rect.height;


        String dirpath = "c:\\vfiles\\photo\\" + 2;
        File dirFile = new File(dirpath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String filename = dirpath+"\\"+ f.format(new Date()) + ".png";
        System.out.println(filename);
        rect_cut = new Rect(rect_cut.x, rect_cut.y, rect_cut.width, rect_cut.height);
        //dst裁剪后的
        Mat dst = new Mat(image, rect_cut);
        Mat afterreasize = new Mat();
        Imgproc.resize(dst, afterreasize, new Size(92, 112), 0, 0, Imgproc.INTER_LINEAR);
        System.out.println("像素" + dst.height() + "dd" + dst.width());
        Imgcodecs.imwrite(filename, afterreasize);


        ;

    }
}

