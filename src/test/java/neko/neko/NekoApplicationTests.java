package neko.neko;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NekoApplicationTests {
    static {
//        System.loadLibrary("opencv_java401");
        System.load("C:\\vfiles\\opencv\\opencv_java401.dll");
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
//
//    @Test
//    public void facedetection() {
//        int flag=0;
//        Rect rect_cut = new Rect();// 裁剪后的
//        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
//        Size dsize = new Size(92, 112);
//        CascadeClassifier faceDetector = new CascadeClassifier("C:\\vfiles\\opencv\\lbpcascade_frontalface.xml");
//        Mat image = Imgcodecs.imread("C:\\vfiles\\test\\ww.png");
//        System.out.println("图片" + image.size());
//        MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(image, faceDetections);
//         flag = faceDetections.toArray().length;
//        System.out.println("人脸有几个" + flag
//        );
//        if (flag > 1) {
//
//        }
//        Rect rect = faceDetections.toArray()[0];
////            // 用绿色框匡助
//
//        Imgproc.rectangle(image, new Point(rect.x, rect.y),
//                new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
//        rect_cut.x = rect.x;
//        rect_cut.y = rect.y;
//        rect_cut.width = rect.width;
//        rect_cut.height = rect.height;
//
//
//        String dirpath = "c:\\vfiles\\photo\\" + 2;
//        File dirFile = new File(dirpath);
//        if (!dirFile.exists()) {
//            dirFile.mkdirs();
//        }
//        String filename = dirpath+"\\"+ f.format(new Date()) + ".png";
//        System.out.println(filename);
//        rect_cut = new Rect(rect_cut.x, rect_cut.y, rect_cut.width, rect_cut.height);
//        //dst裁剪后的
//        Mat dst = new Mat(image, rect_cut);
//        Mat afterreasize = new Mat();
//        Imgproc.resize(dst, afterreasize, new Size(92, 112), 0, 0, Imgproc.INTER_LINEAR);
//        System.out.println("像素" + dst.height() + "dd" + dst.width());
//        Imgcodecs.imwrite(filename, afterreasize);
//
//
//        ;
//
//    }
//    @Test
//    public void a(){
//        boolean flag = false;
//        //获取文件夹下的文件列表
//        String basefile = "C:\\vfiles\\photo";
//        File file = new File(basefile);
//        // 获得该文件夹内的所有文件
//        File[] array = file.listFiles();
//        System.out.println("获取指定文件下文件夹的个数" + array.length);
//        List<String> list = new ArrayList<>();
//        // 循环遍历
//        for (int i = 0; i < array.length; i++) {
//            //此时能够获取已上传图片的学生相册
//            File file_ = new File(array[i].getPath());
//            File[] array_ = file_.listFiles();
//            //获取相册文件夹名字 即学生学号
//            String uname = array[i].getName();
//            for (int a = 0; a < array_.length; a++) {
//                File img_file = array_[a];
//                String path_img = img_file.getPath();
//                String imgname = img_file.getName();
//                String listitem = path_img + imgname + ";" + uname;
//                System.out.println("要写入的数据" + listitem);
//                list.add(listitem);
//
//
//            }
//
//
//        }
//        //sh创建文件 且将集合写入
//        File filename = new File("C:\\vfiles\\a.text");
//
//        if (!filename.exists()) {
//            try {
//                filename.createNewFile();
//
//                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\vfiles\\a.text"));
//                int count = 1;
//                for (String l : list) {
//                    if (count != list.size()) {
//                        writer.write(l + "\r\n");
//                    } else {
//                        writer.write(l);
//                    }
//
//                    count++;
//                }
//                writer.close();
//                flag = true;
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } else {
//
//            //删除该文件
//            Boolean succeedDelete = filename.delete();
//            if (succeedDelete) {
//                System.out.println("删除单个文件" + filename.getName() + "成功！");
//
//                try {
//                    filename.createNewFile();
//                    System.out.println("文件一开始存在但被我删除" + "成功！");
//                    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\vfiles\\a.text"));
//                    int count = 1;
//                    for (String l : list) {
//                        if (count != list.size()) {
//                            writer.write(l + "\r\n");
//                        } else {
//                            writer.write(l);
//                        }
//
//                        count++;
//                    }
//                    writer.close();
//                    flag = true;
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("删除单个文件" + filename.getName() + "失败！");
//
//            }
//        }
//
//
//    }

}


