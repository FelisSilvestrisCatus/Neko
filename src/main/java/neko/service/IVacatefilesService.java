package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Vacatefiles;
import org.springframework.web.multipart.MultipartFile;

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
}