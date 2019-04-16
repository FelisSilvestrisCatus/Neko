package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Vacatefiles;
import neko.entity.vo.AuditVacateByTeacher;
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
                //文件路径
                String filePath = dirpath + "\\" + mf.getOriginalFilename();
                try {
                    //保存文件
                    mf.transferTo(new File(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    map = generalMethod.getErrorMap();
                    map.put("msg", "无法存储附件");
                }

                Vacatefiles vacatefiles = new Vacatefiles();
                vacatefiles.setVid(Integer.parseInt(id));
                vacatefiles.setFilepath(filePath);
                save(vacatefiles);

                System.out.println("vacatefiles = " + vacatefiles);
            }

            map.put("msg", "请假申请已提交");
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

}
