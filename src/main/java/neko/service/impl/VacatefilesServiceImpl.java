package neko.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import neko.entity.Vacatefiles;
import neko.entity.vo.VacateDetail;
import neko.mapper.VacatefilesMapper;
import neko.service.IVacatefilesService;
import neko.utils.generalMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-31
 */
@Service
public class VacatefilesServiceImpl extends ServiceImpl<VacatefilesMapper, Vacatefiles> implements IVacatefilesService {

    @Override
    public Map<String, String> createVacateFile(String id, MultipartFile[] vfile) {
        Map<String, String> map = generalMethod.getSuccessMap();
        //创建存储文件夹
        String dirpath = "c:\\vfiles\\" + Integer.parseInt(id);
        if (new File(dirpath).mkdirs()) {
            for (MultipartFile mf : vfile) {
                String filename = mf.getOriginalFilename();
                //文件路径
                String filePath = dirpath + "\\" + filename;
                try {
                    //保存文件
                    mf.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    map = generalMethod.getErrorMap();
                    map.put("msg", "无法存储附件");
                    return map;
                }

                Vacatefiles vacatefiles = new Vacatefiles();
                vacatefiles.setVid(Integer.parseInt(id));
                vacatefiles.setFilepath(filePath);

                //处理文件类型

                String filetype;
                ContentInfoUtil util = new ContentInfoUtil();
                ContentInfo info = null;
                try {
                    info = util.findMatch(filePath);
                    if (info == null) {
                        filetype = "无类型";
                    } else {
                        filetype = info.getName();
                        if ("other".equals(filetype)) {
                            File file = new File(filePath);
                            String fileName = file.getName();
                            int length = fileName.length();
                            int index = fileName.lastIndexOf(".") + 1;
                            if (index == 0) {
                                filetype = "无类型";
                            } else {
                                filetype = fileName.substring(index, length);
                            }
                        }
                    }
                } catch (IOException e) {
                    filetype = "无类型";
                }

                vacatefiles.setFiletype(filename);
                if (save(vacatefiles)) {
                    map.put("msg", "请假申请已提交");
                } else {
                    map = generalMethod.getErrorMap();
                    map.put("msg", "无法存储附件" + filename);
                }
            }
        } else {
            map = generalMethod.getErrorMap();
            map.put("msg", "无法存储附件");
        }
        return map;
    }


    @Override
    public VacateDetail getVacateDetail(int vid) {
        return this.baseMapper.getVacateDetail(vid);
    }

    @Override
    public Map<String, String> getVacateFile(Integer vid) {
        Map<String, String> map = generalMethod.getErrorMap();


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid", vid);
        List<Vacatefiles> list = this.baseMapper.selectList(queryWrapper);


        return map;
    }

}
