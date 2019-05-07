package neko.utils.face;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.librealsense.intrinsics;
import org.bytedeco.javacpp.DoublePointer;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_face.*;
import org.bytedeco.opencv.opencv_ml.DTrees.Split;

import org.opencv.imgcodecs.Imgcodecs;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_face.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

public class FaceTrainAndValidate {

    //模型训练 用户上传完照片后自动调用模型训练  将生成后的模型保存 后期直接使用
    public static void train() throws IOException {
        String trainingDir = "C:\\vfiles\\a.text";


        // 封装数据源
        BufferedReader br = new BufferedReader(new FileReader(trainingDir));
        // 封装目的地(创建集合对象)
        ArrayList<String> array = new ArrayList<String>();

        // 读取数据存储到集合中
        String line = null;
        while ((line = br.readLine()) != null) {
            array.add(line);
        }

        // 释放资源
        br.close();
        MatVector images = new MatVector(array.size());

        Mat labels = new Mat(array.size(), 1, CV_32SC1);
        IntBuffer labelsBuf = labels.createBuffer();
        int counter = 0;
        System.out.println(array.size());
        for (String s : array) {


            Mat img = imread(s.split(";")[0], Imgcodecs.IMREAD_GRAYSCALE);

            System.out.println("加载" + counter + "张" + "像素" + img.arrayHeight() * img.arrayWidth());
            System.out.println(counter + "张" + "通道数目" + img.channels());
            int label = Integer.parseInt(s.split(";")[1]);
            System.out.println(label + "label");
            images.put(counter, img);

            labelsBuf.put(counter, label);

            counter++;
        }

        FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = EigenFaceRecognizer.create();
        // FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
        System.out.println("模型标签的数目" + labels.size());
        System.out.println("模型开始训练 ");
        faceRecognizer.train(images, labels);

        //   IntPointer label = new IntPointer(1);
        //  DoublePointer confidence = new DoublePointer(1);
        //faceRecognizer.predict(testImage, label, confidence);
        //   int predictedLabel = label.get(0);
//
        System.out.println("模型训练完毕 ");

    }

    //人脸识别
    //学生用来登录或者点名时所需（）
    public static boolean validate(String uid, String img) {

        boolean flag = false;
        //将照片字节码转为指定格式的照片  若通过返回值


        return flag;
    }
}
