package neko.utils.face;


import sun.misc.BASE64Decoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//该类只是用来查询相关数目 以及负责图片转换
public class Face {


    public static int getPhotoNum(String uid) {
        File file = new File("C:\\vfiles\\photo\\" + uid);
        String files[];
        files = file.list();
        int num = 0;
        try {
            num = files.length;
        } catch (Exception e) {
            e.getStackTrace();
        }


        return num;
    }

    /**
     * @param img      ：前端request 获得的图像数据
     * @param uid：用户id
     * @return
     */
    public static String base64StrToImage(String img, String uid) {
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        String dirpath = "C:\\vfiles\\photo_temp\\" + uid;
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

    //获取训练图片的csv文件
    public boolean getCsv() {
        boolean flag = false;
        //获取文件夹下的文件列表
        String basefile = "C:\\vfiles\\photo";
        File file = new File(basefile);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();
        System.out.println("获取指定文件下文件夹的个数" + array.length);
        List<String> list = new ArrayList<>();
        // 循环遍历
        for (int i = 0; i < array.length; i++) {
            //此时能够获取已上传图片的学生相册
            File file_ = new File(array[i].getPath());
            File[] array_ = file_.listFiles();
            //获取相册文件夹名字 即学生学号
            String uname = array[i].getName();
            for (int a = 0; a < array_.length; a++) {
                File img_file = array_[a];
                String path_img = img_file.getPath();
                String imgname = img_file.getName();
                String listitem = path_img + imgname + ";" + uname;
                System.out.println("要写入的数据" + listitem);
                list.add(listitem);


            }


        }
        //sh创建文件 且将集合写入
        File filename = new File("C:\\vfiles\\a.text");

        if (!filename.exists()) {
            try {
                filename.createNewFile();
                System.out.println("文件一开始存在但被我删除" + "成功！");
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\vfiles\\a.text"));
                int count = 1;
                for (String l : list) {
                    if (count != list.size()) {
                        writer.write(l + "\r\n");
                    } else {
                        writer.write(l);
                    }

                    count++;
                }
                writer.close();
                flag = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {

            //删除该文件
            Boolean succeedDelete = filename.delete();
            if (succeedDelete) {
                System.out.println("删除单个文件" + filename.getName() + "成功！");

                try {
                    filename.createNewFile();
                    System.out.println("文件一开始存在但被我删除" + "成功！");
                    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\vfiles\\a.text"));
                    int count = 1;
                    for (String l : list) {
                        if (count != list.size()) {
                            writer.write(l + "\r\n");
                        } else {
                            writer.write(l);
                        }

                        count++;
                    }
                    writer.close();
                    flag = true;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("删除单个文件" + filename.getName() + "失败！");

            }
        }

        return flag;
    }


}



