package com.aura.springboot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.aura.springboot.service.ApplicationService;
import com.aura.springboot.utils.ResponseResult;
import com.aura.springboot.utils.ResultCode;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/20.
 * @author Carry
 */
@Controller
@RequestMapping("/file")
public class FileUploadController  {
	private static Logger log = LoggerFactory.getLogger(ApplicationService.class);

    public String realPath="e://feedback//"; //文件保存路径的根路径

    private static List<String> uploadSuffix = new ArrayList<>();
    static {
        if(uploadSuffix.isEmpty()){
            uploadSuffix.add("jpg");
            uploadSuffix.add("png");
            uploadSuffix.add("bmp");
            uploadSuffix.add("jpeg");
            uploadSuffix.add("pdf");
        }
    }

    /**
     * 上传多个文件，后缀仅限于jpg、pdf
     * @param request
     * @return
     */
	public Map<String,Object> uploadFile(HttpServletRequest request,String email) {
        //将request请求的上下文转换为MultipartHttpServletRequest
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String,Object> result = new HashMap<String, Object>();
        String errorMsg = null;
        List<String> msgList = new ArrayList<String>();
        try {
            //获取文件
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            // multipartFile = null;//获取文件名
            boolean uploadFlag = true;
            for (Map.Entry<String, MultipartFile> set : fileMap.entrySet()) {
                MultipartFile multipartFile = set.getValue();// 文件名
                String uploadfileName = multipartFile.getOriginalFilename();
                if(StringUtils.isEmpty(uploadfileName)){
                    continue;
                }
                String suffix = uploadfileName.substring(uploadfileName.lastIndexOf(".")+1).toLowerCase();
                if(!uploadSuffix.contains(suffix)){
                    errorMsg = "文件格式仅限于jpg、png、jpeg、bmp、pdf";
                    uploadFlag = false;
                    break;
                }
                //System.out.println(1);
                String fileName = this.storeIOcFile(multipartRequest, multipartFile,email);
                //System.out.println(5+"=="+fileName);
                if(StringUtils.isEmpty(fileName)){
                    errorMsg = "文件上传失败";
                    uploadFlag = false;
                    break;
                }
                msgList.add(fileName.replace("\\","/"));
            }
            if(!uploadFlag){
                 result.put("result", errorMsg);
                 return result;
            }else{
                if(!msgList.isEmpty()){
                    result.put("result", msgList);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

//    private String storeIOc(HttpServletRequest request, MultipartFile file) {
//        String result = "";
//        if (file == null) {
//            return "";
//        }
//        String fileName = "";
//        String logImageName = "";
//        if (file.isEmpty()) {
//            //result = "文件未上传";
//            result = "1";
//        } else {
//            String _fileName = file.getOriginalFilename();
//            String suffix = _fileName.substring(_fileName.lastIndexOf(".")).toLowerCase();
//            if(!StringUtils.isEmpty(suffix)){
//                if(suffix.equalsIgnoreCase(".xls") || suffix.equalsIgnoreCase(".xlsx") || suffix.equalsIgnoreCase(".txt")|| suffix.equalsIgnoreCase(".png")
//                        || suffix.equalsIgnoreCase(".doc") || suffix.equalsIgnoreCase(".docx") || suffix.equalsIgnoreCase(".pdf")
//                        || suffix.equalsIgnoreCase(".ppt") || suffix.equalsIgnoreCase(".pptx")|| suffix.equalsIgnoreCase(".gif")
//                        || suffix.equalsIgnoreCase(".jpg")|| suffix.equalsIgnoreCase(".jpeg")|| suffix.equalsIgnoreCase(".bmp")
//                        || suffix.equalsIgnoreCase(".zip")|| suffix.equalsIgnoreCase(".rar")|| suffix.equalsIgnoreCase(".7z") || suffix.equalsIgnoreCase(".shp") || suffix.equalsIgnoreCase(".dbf")){
//                    // /**使用UUID生成文件名称**/
//                    logImageName = UUID.randomUUID().toString() +"_"+ _fileName;
//                    String todayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                    String uploadDir = request.getParameter("uploadDir");
//                    String resultStr =  File.separator + uploadDir + File.separator + todayString + File.separator +logImageName;
//                    fileName = realPath + resultStr;
//                    File restore = new File(fileName);
//                    if(!restore.getParentFile().exists()){
//                        restore.getParentFile().mkdirs();
//                    }
//                    try {
//                        //System.out.println(3+"=="+fileName);
//                        file.transferTo(restore);
//                        //System.out.println(4+"=="+restore);
//                        result = resultStr;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new RuntimeException(e);
//                    }
//                }else{
//                    //result = "文件格式不对，只能上传ppt、ptx、doc、docx、xls、xlsx、pdf、png、jpg、jpeg、gif、bmp格式";
//                    result = "3";
//                }
//            }
//        }
//        return result;
//    }

    private String storeIOcFile(HttpServletRequest request, MultipartFile file,String email) {
        String result = "";
        String _fileName = file.getOriginalFilename();
        log.info("-------------文件名为：------------------》"+_fileName);
        // /**使用UUID生成文件名称**/
        String logImageName = UUID.randomUUID().toString() +"_"+ _fileName;
        String todayString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //String uploadDir = request.getParameter("uploadDir");
        String resultStr =  File.separator  + todayString + File.separator +logImageName;
        String fileName = realPath + email + resultStr;
        log.info("-------------文件地址为----------------------》"+fileName);
        File restore = new File(fileName);
        if(!restore.getParentFile().exists()){
            restore.getParentFile().mkdirs();
        }
        try {
            file.transferTo(restore);
            result = resultStr;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 文件下载接口
     * @param filePath  文件上传时，返回的相对路径
     * @param response
     * @param isOnLine  传入true，表示打开，但是打开的是浏览器能识别的文件，比如图片、pdf，word等无法打开
     *                  传入false,只是下载，如果不传入这个参数默认为false
     * @throws Exception
     */
    @RequestMapping(value = "/downloadFile",method = RequestMethod.GET)
    public void downLoad(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(realPath+filePath);
        if (!f.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        String fileName = f.getName();
        fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;
        response.reset(); // 非常重要
        if (isOnLine) { // 在线打开方式
            URL u = new URL("file:///" + realPath+filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename=" + fileName);
            // 文件名应该编码成UTF-8
        } else { // 纯下载方式
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        br.close();
        out.close();
    }
}
