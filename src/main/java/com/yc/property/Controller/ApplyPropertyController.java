package com.yc.property.Controller;

import com.yc.property.Bean.Owner;
import com.yc.property.Dao.ApplyPropertyDao;
import com.yc.property.Service.ApplyPropertyService;
import com.yc.property.annotation.PassToken;
import com.yc.property.annotation.UserLoginToken;
import com.yc.property.util.JsonResult;
import com.yc.property.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SimpleTimeZone;

@RestController
@RequestMapping("/ApplyProperty/")
public class ApplyPropertyController {
    @Autowired
    private ApplyPropertyService applyPropertyService;
    @Autowired
    private ApplyPropertyDao applyPropertyDao;

    @RequestMapping("getApplyNumByOwnerPhone")
    public JsonResult getApplyNumByOwnerPhone(String ownerPhone){
        return new JsonResult(applyPropertyService.getApplyNumByOwnerPhone(ownerPhone));
    }

    @UserLoginToken
    @RequestMapping("getTypeOrderList")
    public JsonResult getTypeOrderList(String ownerPhone,String keyWords,String values){
        System.out.println("keywords:"+keyWords+" values:"+values);
        return new JsonResult(applyPropertyService.getTypeOrderList(ownerPhone,keyWords,values));
    }

    //获得所有维修类别
    @RequestMapping("getAllType")
    @PassToken
    public JsonResult getAllType(){
        return new JsonResult(applyPropertyService.getAllType());
    }

    //获得所有维修工
    @RequestMapping("getAllWorker")
    public JsonResult getAllWoker(){
        return new JsonResult(applyPropertyService.getAllWorker());
    }

    //获得某一维修工待维修工单数量
    @RequestMapping("getWorkerUnfinishedOrderNum")
    public JsonResult getWorkerUnfinishedOrderNum(String workerId){
        return new JsonResult(applyPropertyService.getWorkerUnfinishedOrderNum(workerId));
    }

    //检查该业主是否有结束成订单,返回data：该业主未结束订单数量
    @RequestMapping("getOwnerUnfinishedOrderNum")
    public JsonResult getOwnerUnfinishedOrderNum(String ownerPhone){
        return new JsonResult(applyPropertyService.getOwnerUnfinishedOrderNum(ownerPhone));
    }

    //业主提交维修申请
    @RequestMapping(value = "ApplyProperty",method = RequestMethod.POST)
    public JsonResult ApplyProperty(@RequestBody HashMap<String ,String > map){
        System.out.println("验证文件接收是否成功：");
        System.out.println(map);
//        MultipartFile file1=MultipartFile(map.get("x1"));
//        MultipartFile file1=map.get("x1");
//        MultipartFile file1=map.get("x1");
        OrderVo orderVo=new OrderVo();
        orderVo.setWorkerId(map.get("workerPhone"));
        orderVo.setOwnerId(map.get("ownerPhone"));
        orderVo.setRemarks(map.get("remarks"));
        orderVo.setTypeId(Integer.parseInt(map.get("typeId")));
        System.out.println("前端传过来的orderVo:");
        System.out.println(orderVo);
        //******************涉及到事务，还没有实现，以后实现************************************
        JsonResult result=new JsonResult();
        //1.判断业主是否选择维修工
        if(orderVo.getWorkerId()==null){
            //**********************如果没有选择，则系统自动生成对应的维修工，此处没有实现，以后完善，打算想一个算法************************
            orderVo.setWorkerId("15857195182");
        }
        //2.创建一个Order

            //创建前先找该业主最大orderId
             String secondOrderId=applyPropertyService.getMaxOrderId(orderVo.getOwnerId());

        int result1= applyPropertyService.createOrder(orderVo.getOwnerId(),orderVo.getWorkerId());
        if(result1!=1){
            result.setState(0);
            result.setMessage("创建Order失败！");
        }else{
            //3.查询该业主最近的一条Order的Id,即刚刚创建的order的id
            String orderId=applyPropertyService.getMaxOrderId(orderVo.getOwnerId());
            System.out.println("最大id:"+orderId);
            orderVo.setOrderId(orderId);
            //4.增加一条Order_detail
            int result2=applyPropertyService.createOrderDetail(orderVo);
            if(result2!=1){
                result.setMessage("创建orderDetail失败！");
                result.setState(0);
            }else{
                //设置图片id,img_id:secondOrderId(上面获得的第二大orderId)
                applyPropertyDao.setImgIdByOrderId(secondOrderId,orderId);
                result.setMessage("申请维修成功！");
                OrderVo orderVo1=applyPropertyService.getOrderDetailByOrderId(orderId);
                orderVo1.setWorkerId(orderVo.getWorkerId());
                orderVo1.setOwnerId(orderVo.getOwnerId());
                System.out.println(orderVo1);
                result.setData(orderVo1);
            }
        }
        return result;
    }

    //查询某业主最新一个维修申请，需要业主ownerPhone
    @RequestMapping("getNewestPropertyOrder")
    public JsonResult getNewestPropertyOrder(String ownerPhone){
        JsonResult result=new JsonResult();
//        String ownerPhone=map.get("ownerPhone");
        System.out.println("getNewestPropertyOrder+ownerPhone:"+ownerPhone);
        result.setData(applyPropertyService.getNewestPropertyOrder(ownerPhone));
        return result;
    }

    //开始维修点击“开始维修”按钮
    @RequestMapping("startProperty")
    public JsonResult startProperty(String orderId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date date){
        System.out.println("startProperty:");
        System.out.println("date:");
        System.out.println(date);
        return new JsonResult(applyPropertyService.startProperty(orderId,date));
    }

    //结束维修点击“结束维修”按钮
    @RequestMapping("endProperty")
    public JsonResult endProperty(String orderId,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date date){
        JsonResult result=new JsonResult();
        int i = applyPropertyService.endProperty(orderId, date);
        if(i==1){
            result.setMessage("维修工结束维修时间已记录！此次维修已结束!");
        }else{
            result.setMessage("维修工结束维修时间记录失败！");
            result.setState(0);
        }
        return result;
    }

    //业主进行评价
    @RequestMapping("createAsess")
    public JsonResult createAsess(OrderVo orderVo){
        System.out.println(orderVo);
        return new JsonResult(applyPropertyService.createAsess(orderVo));
    }

    //业主取消本次维修
    @RequestMapping("cancelProperty")
    public JsonResult cancelProperty(String orderId){
        JsonResult result=new JsonResult();
        return new JsonResult(applyPropertyService.cancelProperty(orderId));
    }

    //业主申请维修时上传图片
    @PassToken
    @RequestMapping("uploadPicture")
    public JsonResult uploadPicture(@RequestParam("file") MultipartFile file,@RequestParam("phone") String phone)throws IOException {
        System.out.println("访问后端uploadPicture接口");
        System.out.println("phone:"+phone);
        JsonResult result=new JsonResult();
        InputStream in=file.getInputStream();//getInputStream()：返回InputStream读取的内容
        String originalFilename=file.getOriginalFilename();//得到原来的文件名在客户机的文件系统名称



        //设置名字,取该业主最大订单号来命名，图片名字以及存储在数据库中这张图片的id,都是该业主最大订单号。
        //相当于某一个业主，有关他的订单order_detail中，img_id总是等于他的上一个oder_id
        //若该业主第一次申请，没有查询到最大order_id,为null,则设置一个初始值：当前年月日+00，因为order_id最小是年月日+01
        String fileName="";
        String orderId=applyPropertyDao.getNewestPropertyOrderId(phone);
        if(StringUtils.isEmpty(orderId)){
            SimpleDateFormat df=new SimpleDateFormat("yyMMdd");
            String index1=df.format(new Date());
            System.out.println(index1);
            fileName=index1+"00";
        } else{
            fileName=orderId;
        }
        //加上后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") );
        String fullName=fileName+suffix;
        System.out.println("fileName:"+fileName);
        System.out.println("fullName:"+fullName);


        //存储到硬盘中
        File f=new File("D:\\development-tool\\1Documents\\images\\"+fullName);
        if(f.exists()){
            System.out.println("f存在");
        }else{
            System.out.println("f不存在");
        }
        file.transferTo(f);
        //将图片id：fileName,路径存储到img表中,如果image表中已经有此id，则更新路径。到时候业主新建一个订单时，它的img_id就取该业主上一个orderId
        int kk=applyPropertyService.savePicture(fileName,"D:\\development-tool\\1Documents\\images\\"+fullName);
        return result;
    }

    @RequestMapping("setFirstOrder")
    public JsonResult setFirstOrder(String orderId,String workerPhone){
        System.out.println("参数检查："+orderId+","+workerPhone);
        int maxPriority=applyPropertyService.getMaxPriorityByWorkerPhone(workerPhone);
        return new JsonResult(applyPropertyService.setMaxPriorityByOrderId(orderId,maxPriority+1));
    }

    //
    @RequestMapping("setPingjiaByOrderId")
    public JsonResult setPingjiaByOrderId(String orderId,int asess){
        return new JsonResult(applyPropertyService.setPingjiaByOrderId(orderId,asess));
    }

    @RequestMapping("getCalTimeByWorkerPhone")
    public JsonResult getCalTimeByWorkerPhone(String workerPhone){
        JsonResult result=new JsonResult();
        result.setData(applyPropertyService.getCalTimeByWorkerPhone(workerPhone));
        return result;
    }

    @RequestMapping("getAvgAsessByWorkerPhone")
    public JsonResult getAvgAsessByWorkerPhone(String workerPhone){
        JsonResult result=new JsonResult();
        result.setData(applyPropertyService.getAvgAsessByWorkerPhone(workerPhone));
        return result;
    }

    @PassToken
    @RequestMapping("getPicture")
    public void getPicture(HttpServletResponse response, String path)throws IOException {
        File file = new File(path);
        FileInputStream fis;
        fis = new FileInputStream(file);

        long size = file.length();
        byte[] temp = new byte[(int) size];
        fis.read(temp, 0, (int) size);
        fis.close();
        byte[] data = temp;
        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        out.write(data);
        out.flush();
        out.close();
    }

}
