package net.oicp.wego.controller.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.oicp.wego.controller.BaseController;
import net.oicp.wego.tools.Errors;
import net.oicp.wego.tools.Tool;

/** 
 * @author         lolog
 * @version        v1.0  
 * @date           2016.08.30
 * @company        WEGO
 * @description    KindEditor file upload and manage controller
*/
@Controller
@RequestMapping("/file/")
public class FileController extends BaseController {
	@RequestMapping("uploadFile")
	public ModelAndView uploadFile (
			@RequestParam(value="imgFile", required=false) MultipartFile multipartFile, 
			@RequestParam(value="type", required=false) String type,
			HttpServletResponse response
	) {
		// set response 
		response.setHeader("Content-type", "text/html");
		response.setCharacterEncoding("UTF-8");
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 1);
		
		// file name
		if (multipartFile == null || multipartFile.isEmpty() == true) {
			result.put("message", Errors.ERROR_MESSAGE_FILE_ZH);
			return json(result);
		}
		
		// file save path
		Date date = new Date();
		String directory = type + "/" + Tool.dateToString(date, "yyyyMMdd");
		String filePath  = propertyContext.uploadPath() + "/" + directory;
		
		// file suffix name
		String ext = multipartFile.getOriginalFilename();
		       ext = ext.substring(ext.lastIndexOf("."));
		String fileName  = getIDS("uploadFile") + ext;
		
		File file = new File(filePath);
		
		// create directory
		if(file.exists() == false || file.isFile() == false) {
			file.mkdirs();
		}
		
		file = new File(filePath, fileName);
		
		try {
			multipartFile.transferTo(file);
			result.put("error", 0);
			result.put("url", directory+"/"+fileName);
		} catch (Exception e) {
			result.put("message", Errors.ERROR_MESSAGE_FILE_ZH);
		}
		
		return json(result);
	}
	
	@RequestMapping("fileManage")
	public ModelAndView fileManage (
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="path",required=false) String path,
			@RequestParam(value="order", required=false) String order
	) {
		// parameter
		path = (path == null) ? "" : path;
		type = (type == null) ? "" : type;
		
		// 解决方案的文件路径
		String filePath    = propertyContext.uploadPath() + "/" + type;
			   filePath    = (path.length() == 0) ? filePath : (filePath + "/" + path);
			   
		File   currentPath = new File(filePath);
		
		// 遍历目录取的文件信息
		List<Object> fileList = new ArrayList<Object>();
		// 图片扩展名
		String[] fileTypes = {"gif", "jpg", "jpeg", "png", "bmp"};
		
		if (currentPath.listFiles() != null) {
			for(File oneFile: currentPath.listFiles()) {
				Map<String, Object> hashtable = new HashMap<String, Object>();
				
				String fileName = oneFile.getName();
				if(oneFile.isDirectory()) {
					hashtable.put("is_dir", true);
					hashtable.put("has_file", (oneFile.listFiles() != null));
					hashtable.put("filesize", 0L);
					hashtable.put("is_photo", false);
					hashtable.put("filetype", "");
				} else if(oneFile.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hashtable.put("is_dir", false);
					hashtable.put("has_file", false);
					hashtable.put("filesize", oneFile.length());
					hashtable.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hashtable.put("filetype", fileExt);
				}
				// 文件名
				hashtable.put("filename", path  + fileName);
				fileList.add(hashtable);
			}
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", "");
		result.put("current_url", servletContext.getContextPath()+"/upload/"+type + "/");
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		
		return json(result);
	}
}
