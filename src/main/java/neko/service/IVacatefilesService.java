package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Vacatefiles;
import neko.entity.vo.VacateDetail;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-03-31
 */
public interface IVacatefilesService extends IService<Vacatefiles> {
    Map<String, String> createVacateFile(String id, MultipartFile[] vfile);

    VacateDetail getVacateDetail(int vid);

    Map<String, String> getVacateFile(Integer vid);

    void getFile(String vfid, HttpServletResponse res);

    void getFiles(String vid, HttpServletResponse res);
}
