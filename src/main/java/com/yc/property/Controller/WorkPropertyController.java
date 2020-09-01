package com.yc.property.Controller;

import com.yc.property.Service.WorkPropertyService;
import com.yc.property.annotation.PassToken;
import com.yc.property.annotation.UserLoginToken;
import com.yc.property.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/WorkProperty/")
public class WorkPropertyController {
    @Autowired
    private WorkPropertyService workPropertyService;

    //获取该维修工所有订单
    @RequestMapping("getOrderList")
    public JsonResult getOrderList( String workerPhone){
        System.out.println("workerPhone:"+workerPhone);
        return new JsonResult(workPropertyService.getOrderList(workerPhone));
    }

    //根据维修类别查询某维修工订单
    @RequestMapping("getTypeOrderList")
    public JsonResult getTypeOrderList(String workerPhone,String keyWords,String values){
        System.out.println("keywords:"+keyWords+" values:"+values);
        return new JsonResult(workPropertyService.getTypeOrderList(workerPhone,keyWords,values));
    }

    //上传图片测试
    @RequestMapping("uploadPicture")
    public JsonResult uploadPicture(MultipartFile file)throws IOException {
        System.out.println("访问WorkProperty/uploadPicture!");
        JsonResult result=new JsonResult();
        String originalFilename=file.getOriginalFilename();
        System.out.println(originalFilename);
        File f=new File("D:\\development-tool\\1Documents\\images\\WorkerProperty\\"+originalFilename);
        file.transferTo(f);
        return result;
    }

    //维修工反馈
//    @RequestMapping("feedBack")
//    public JsonResult feedBack(String feedBack,String cardId){
//        JsonResult result=new JsonResult();
//        if(StringUtils.isEmpty(feedBack)||StringUtils.isEmpty(cardId)){
//            result.setMessage("反馈或身份证号为空！");
//            result.setState(0);
//        }else{
//            int result1=workPropertyService.feedBack(feedBack,cardId);
//            if(result1==1){
//                result.setMessage("反馈成功！");
//            }else{
//                result.setState(0);
//                result.setState(0);
//            }
//        }
//        return result;
//    }
}
