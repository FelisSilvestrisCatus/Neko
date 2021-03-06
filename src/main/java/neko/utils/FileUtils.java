package neko.utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

//文件下载
public class FileUtils {

    //获取文件
    public static void getFile(byte[] bfile, String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送文件
    public static void responseTo(File file, HttpServletResponse res) {
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + file.getName());

        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;

        try {
            OutputStream os = res.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            //设置长度，避免损坏office文件
            res.setHeader("Content-Length", String.valueOf(fis.getChannel().size()));


            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //打包文件夹为zip
    public static void responseToZip(String vid, HttpServletResponse res) {
        String srcPath = "c:\\vfiles\\files\\" + vid;
        String zipPath = "c:\\vfiles\\zip\\" + vid + ".zip";
        File srcdir = new File(srcPath);
        File zipFile = new File(zipPath);

        if (!srcdir.exists()) {
            srcdir.mkdirs();
        }

        Project prj = new Project();
        Zip zip = new Zip();
        zip.setProject(prj);
        zip.setDestFile(zipFile);
        FileSet fileSet = new FileSet();
        fileSet.setProject(prj);
        fileSet.setDir(srcdir);
        zip.addFileset(fileSet);
        zip.execute();

        File file = new File(zipPath);
        responseTo(file, res);
    }
}
