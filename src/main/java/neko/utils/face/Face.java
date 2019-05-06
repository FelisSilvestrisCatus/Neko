package neko.utils.face;

import neko.utils.generalMethod;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//有关人脸识别的所有东西都在这
public class Face {
    //前端数据转换为图片
    static {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    public static int getPhotoNum(String uid) {
        File file = new File("C:\\vfiles\\photo\\" + uid);
        String files[];
        files = file.list();
        int num = files.length;

        return num;
    }
    /**
     * @param    img    ：前端request 获得的图像数据
     * @param uid：用户id
     * @return
     */
    public static String base64StrToImage(String img, String uid) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        String dirpath = "C:\\vfiles\\photo_login\\" + uid;
        File dirFile = new File(dirpath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String imgStr = img.substring(22, img.length());

        if (imgStr == null)
            return "";
        BASE64Decoder decoder = new BASE64Decoder();
        String filename = dirpath + "\\" + f.format(new Date()) + ".png";
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            // 文件夹不存在则自动创建

            File tempFile = new File(filename);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return filename;
        } catch (Exception e) {
            return filename;
        }
    }

    //检测人脸  //若检测到 进行裁剪 并返回值

    /**
     * @param path:base64 转换后的图片的地址
     * @return
     */

    public static int facedetection(String path, String uid) {

        int flag = 0;
        Rect rect_cut = new Rect();// 裁剪后的
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        Size dsize = new Size(92, 112);
        CascadeClassifier faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
        Mat image = Imgcodecs.imread(path);
        System.out.println("图片" + image.size());
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);
        flag = faceDetections.toArray().length;
        System.out.println("人脸有几个" + flag
        );
        if (flag != 1) {
            return flag;
        }
        Rect rect = faceDetections.toArray()[0];
//            // 用绿色框匡助

        Imgproc.rectangle(image, new Point(rect.x, rect.y),
                new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        rect_cut.x = rect.x;
        rect_cut.y = rect.y;
        rect_cut.width = rect.width;
        rect_cut.height = rect.height;


        String dirpath = "C:\\vfiles\\photo\\" + 2;
        File dirFile = new File(dirpath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String filename = dirpath + "\\" + f.format(new Date()) + ".png";
        System.out.println(filename);
        rect_cut = new Rect(rect_cut.x, rect_cut.y, rect_cut.width, rect_cut.height);
        //dst裁剪后的
        Mat dst = new Mat(image, rect_cut);
        Mat afterreasize = new Mat();
        Imgproc.resize(dst, afterreasize, new Size(92, 112), 0, 0, Imgproc.INTER_LINEAR);
        System.out.println("像素" + dst.height() + "dd" + dst.width());
        Imgcodecs.imwrite(filename, afterreasize);
        return flag;
    }

}
