package com.yc.property.Controller;

import com.yc.property.Bean.Admin;
import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Type;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.AdminDao;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Service.AdminService;
import com.yc.property.Service.OwnerService;
import com.yc.property.Service.WorkerService;
import com.yc.property.annotation.PassToken;
import com.yc.property.util.JsonResult;
import com.yc.property.util.TokenUtil;
import com.yc.property.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@RestController
@RequestMapping("/Admin/")
public class AdminController{
    @Autowired
    private AdminService adminService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private AdminDao adminDao;
    //管理员登录
    @PassToken
    @RequestMapping("login")
    public JsonResult adminLogin(@RequestBody HashMap<String ,String > map, HttpSession session){
        JsonResult result=new JsonResult();
        System.out.println("管理员登录，访问后端成功");
        System.out.println("id:"+map.get("id"));
        System.out.println("password:"+map.get("password"));
        String id=map.get("id");
        String password=map.get("password");
        Admin admin =new Admin();
        admin.setId(id);
        admin.setPassword(password);

        Admin admin1=adminService.adminLogin(admin);
        //id密码都正确
        if(admin1!=null){
            result.setMessage("登录成功！");
            session.setAttribute("ADMIN",admin);
            result.setData(admin);
            String token= TokenUtil.createJwtToken(id,password);
            System.out.println("登录成功，token：");
            System.out.println(token);
            AdminVo adminVo=new AdminVo(admin,token);
            result.setData(adminVo);

        }else{
            //登录失败
            result.setState(0);
            result.setMessage("账号或密码错误！");
        }
        return result;
    }

    @PassToken
    @RequestMapping("getPagingOwnerList")
    public JsonResult getPagingOwnerList(int currentPage,int pageSize,String name,String phone,String buildingId){
        System.out.println("进入后台getPagingOwnerList");
        //验证参数，currentPage:要查询第几页的数据，pageSize：一页几条数据
        System.out.println("currentPage:"+currentPage+",pageSize:"+pageSize+",name:"+name+",phone:"+phone+",buildingId:"+buildingId);

        JsonResult result=new JsonResult();
        //查询一共几条数据,pageCount:一共几条数据
        int pageCount=adminService.getOwnerCount(name,phone,buildingId);
        //按照currentPage和pageSize,以及3中筛选条件 进行分页查询
        ArrayList<OwnerVo> ownerVos=adminService.getPagingOwnerList(currentPage,pageSize,name,phone,buildingId);

        OwnerPage ownerPage=new OwnerPage(ownerVos,pageCount);
        result.setData(ownerPage);
        return result;
    }

    @RequestMapping("getAllBuilding")
    public  JsonResult getAllBuilding(){
        return new JsonResult(adminService.getAllBuilding());
    }

    //添加业主账号
    //params:card_id, name, phone, sex, building_id, address, mail
    @RequestMapping("addOwner")
    public JsonResult addOwner(OwnerVo ownerVo){
        System.out.println("进入addOwner Controller");
        System.out.println(ownerVo);
//        String cardId=owner.getCardId();
//        //默认密码为身份证号后四位
//        owner.setPassword(cardId.substring(cardId.length()-4));
//        System.out.println("默认登录密码："+owner.getPassword());
        JsonResult result=new JsonResult();
        Owner result1=ownerService.getOwnerById(ownerVo.getPhone());
        int roomId=ownerDao.getRoomIdByBuildingIdAndAddress(ownerVo.getBuildingId(),ownerVo.getAddress());
        Owner result3=ownerService.getOwenrByRoomId(roomId);
        if(result1==null){
            if(result3==null){//表示该roomId没有被占用，即该房间无人住或无人正在申请
                int result2=adminService.addOwner(ownerVo);
                if(result2==1){
                    result.setMessage("添加业主账号成功！");
                }else{
                    result.setState(0);
                    result.setMessage("添加业主账号失败！");
                }

            }else{
                result.setMessage("该房间已被占用！");
                result.setState(0);
            }
        }else{
            result.setMessage("该手机号已注册！");
            result.setState(0);
        }
        return result;
    }

    //删除多个业主
    @RequestMapping("deleteOwners")
    public JsonResult deleteOwners(String phones){
        JsonResult result=new JsonResult();
        System.out.println("进入deleteOwners Controller");
        System.out.println(phones);
        String[] phones2=phones.split(",");
        int result1=adminService.deleteOwners(phones2);
        if(result1 >=1){
            result.setMessage("删除"+result1+"个业主成功！");
            result.setData(result1);
        }else{
            result.setState(0);
            result.setMessage("删除业主失败！");
        }
        return result;
    }

    @RequestMapping("updateOwners")
    public JsonResult updateOwners(OwnerVo ownerVo){
        JsonResult result=new JsonResult();
        System.out.println("进入updateOwners Controller");
        System.out.println(ownerVo);
        int data=adminService.updateOwners(ownerVo);
        if(data==1){
            result.setMessage("更新成功！");
        }else{
            result.setMessage("更新失败！");
        }
        return result;
    }

    ///////////////////////////////////以下关于worker

    @PassToken
    @RequestMapping("getPagingWorkerList")
    public JsonResult getPagingWorkerList(int currentPage,int pageSize,String name,String phone){
        System.out.println("进入后台getPagingWorkerList");
        //验证参数，currentPage:要查询第几页的数据，pageSize：一页几条数据
        System.out.println("currentPage:"+currentPage+",pageSize:"+pageSize+",name:"+name+",phone:"+phone);

        JsonResult result=new JsonResult();
        //查询一共几条数据,pageCount:一共几条数据
        int pageCount=adminService.getWorkerCount(name,phone);
        //按照currentPage和pageSize,以及3中筛选条件 进行分页查询
        ArrayList<WorkerVo> workerVos=adminService.getPagingWorkerList(currentPage,pageSize,name,phone);

        WorkerPage workerPage=new WorkerPage(workerVos,pageCount);
        result.setData(workerPage);
        return result;
    }

    //添加维修工账号
    //param:card_id, name, phone, sex, mail
    @RequestMapping("addWorker")
    public JsonResult addWorker(WorkerVo workerVo){
        System.out.println("进入addWorker Controller");
        System.out.println(workerVo);
//        String cardId=owner.getCardId();
//        //默认密码为身份证号后四位
//        owner.setPassword(cardId.substring(cardId.length()-4));
//        System.out.println("默认登录密码："+owner.getPassword());
        JsonResult result=new JsonResult();
        Worker result1=workerService.getWorkerByPhone(workerVo.getPhone());
        if(result1==null){
            int result2=adminService.addWorker(workerVo);
            if(result2==1){
                result.setMessage("添加业主账号成功！");
            }else{
                result.setState(0);
                result.setMessage("添加业主账号失败！");
            }

        }else{
            result.setMessage("该手机号已注册！");
            result.setState(0);
        }
        return result;
    }

    //删除多个业主
    @RequestMapping("deleteWorkers")
    public JsonResult deleteWorkers(String phones){
        JsonResult result=new JsonResult();
        System.out.println("进入deleteWorkers Controller");
        System.out.println(phones);
        String[] phones2=phones.split(",");
        int result1=adminService.deleteWorkers(phones2);
        if(result1 >=1){
            result.setMessage("删除"+result1+"个维修工成功！");
            result.setData(result1);
        }else{
            result.setState(0);
            result.setMessage("删除维修工失败！");
        }
        return result;
    }

    @RequestMapping("updateWorkers")
    public JsonResult updateWorkers(WorkerVo workerVo){
        JsonResult result=new JsonResult();
        System.out.println("进入updateOWorkers Controller");
        System.out.println(workerVo);
        int data=adminService.updateWorkers(workerVo);
        if(data==1){
            result.setMessage("更新成功！");
        }else{
            result.setMessage("更新失败！");
        }
        return result;
    }

    //以下关于订单
    //获取所有维修工姓名
    @RequestMapping("getAllWorkerName")
    public JsonResult getAllWorkerName(){
        return new JsonResult(adminService.getAllWorkerName());
    }

    @RequestMapping("getPagingOrderList")
    public JsonResult getPagingOrderList(int currentPage,int pageSize,String orderId,String ownerName,String workerName,String status){
        System.out.println("进入后台getPagingOrderList");
        //验证参数，currentPage:要查询第几页的数据，pageSize：一页几条数据
        System.out.println("currentPage:"+currentPage+",pageSize:"+pageSize+",orderId:"+orderId+",owenrName:"+ownerName+",workerName:"+workerName+",status:"+status);

        JsonResult result=new JsonResult();
        //查询一共几条数据,pageCount:一共几条数据
        //先处理下status
        int status2;
        if(StringUtils.isEmpty(status)){
            status2=-2;
        }else{
            status2=Integer.parseInt(status);
        }



        int pageCount=adminService.getOrderCount(orderId,ownerName,workerName,status2);
        System.out.println("pageCount:"+pageCount);
        //按照currentPage和pageSize,以及3中筛选条件 进行分页查询
        ArrayList<OrderVo> orderVos=adminService.getPagingOrderList(currentPage,pageSize,orderId,ownerName,workerName,status2);

        OrderPage orderPage=new OrderPage(orderVos,pageCount);
        result.setData(orderPage);
        return result;
    }

    //删除多个order
    @RequestMapping("deleteOrders")
    public JsonResult deleteOrders(String orderIds){
        JsonResult result=new JsonResult();
        System.out.println("进入deleteOrders Controller");
        System.out.println(orderIds);
        String[] orderIds2=orderIds.split(",");
        int result1=adminService.deleteOrders(orderIds2);
        if(result1 >=1){
            result.setMessage("删除"+result1+"个订单成功！");
            result.setData(result1);
        }else{
            result.setState(0);
            result.setMessage("删除订单失败！");
        }
        return result;
    }


    //获取所有type
    @RequestMapping("getAllType")
    public JsonResult getAllType(){
        return new JsonResult(adminDao.getAllType());
    }

    @RequestMapping("updateOrder")
    public JsonResult updateOrder(OrderVo orderVo){
        JsonResult result=new JsonResult();
        System.out.println("进入updateOWorkers Controller");
        System.out.println(orderVo);
        int data=adminService.updateOrder(orderVo);
        if(data==1){
            result.setMessage("更新成功！");
        }else{
            result.setMessage("更新失败！");
        }
        return result;
    }

    //下面关于type
    @RequestMapping("addType")
    public JsonResult addType(String  name){
        System.out.println("进入addType Controller");
        System.out.println(name);
//        String cardId=owner.getCardId();
//        //默认密码为身份证号后四位
//        owner.setPassword(cardId.substring(cardId.length()-4));
//        System.out.println("默认登录密码："+owner.getPassword());
        JsonResult result=new JsonResult();
        String typeName2=adminDao.isTypeExits(name);
        if(StringUtils.isEmpty(typeName2)){
            int result2=adminDao.addType(name);
            result.setMessage("添加维修类别成功！");

        }else{
            result.setMessage("该手机号已注册！");
            result.setState(0);
        }
        return result;
    }
    //获得type列表
    @PassToken
    @RequestMapping("getPagingTypeList")
    public JsonResult getPagingTypeList(int currentPage,int pageSize){
        System.out.println("进入后台getPagingTypeList");
        //验证参数，currentPage:要查询第几页的数据，pageSize：一页几条数据
        System.out.println("currentPage:"+currentPage+",pageSize:"+pageSize);

        JsonResult result=new JsonResult();
        //查询一共几条数据,pageCount:一共几条数据
        int pageCount=adminDao.getTypeCount();
        //按照currentPage和pageSize,以及3中筛选条件 进行分页查询
        ArrayList<Type> types=adminDao.getPagingTypeList((currentPage-1)*pageSize,pageSize);

        TypePage typePage=new TypePage(types,pageCount);
        result.setData(typePage);
        System.out.println(typePage);
        return result;
    }
    //删除多个type
    @RequestMapping("deleteTypes")
    public JsonResult deleteTypes(String typeIds){
        JsonResult result=new JsonResult();
        System.out.println("进入deleteTypes Controller");
        System.out.println(typeIds);
        String[] typeIds2=typeIds.split(",");
        int[] typeIds3=new int[typeIds2.length];
        for(int i=0;i<typeIds2.length;i++){
            typeIds3[i]=Integer.parseInt(typeIds2[i]);
        }
        int result1=adminDao.deleteTypes(typeIds3);
        if(result1 >=1){
            result.setMessage("删除"+result1+"个维修类别成功！");
            result.setData(result1);
        }else{
            result.setState(0);
            result.setMessage("删除维系类别失败！");
        }
        return result;
    }


    //修改密码
    @RequestMapping("updatePwd")
    public JsonResult updatePwd(String oldPwd,String newPwd){
        JsonResult result=new JsonResult();
        System.out.println("updatePwd Controller");
        System.out.println(oldPwd+","+newPwd);
        Admin admin=adminDao.getAdminByPwd(oldPwd);
        if(admin!=null){
            int num=adminDao.updatePwd(newPwd);
            if(num>=1){
                result.setMessage("修改密码成功,请重新登录！");
            }else {
                result.setState(0);
                result.setMessage("修改密码失败！");
            }
        }else{
            result.setState(0);
            result.setMessage("旧密码错误！");
        }
        return result;
    }


    ////////////////////////////////////////////////////////////以下用不到

    //获取所有反馈消息
    @RequestMapping("getAllFeedBack")
    public JsonResult getAllFeedBack(){
        return new JsonResult(adminService.getAllFeedBack());
    }

    //回复某一反馈
    //params:id(feedback表id),backValue
    @RequestMapping("backFeedBackByWorkId")
    public JsonResult backFeedBackByWorkId(int id,String backValue){
        JsonResult result=new JsonResult();
        int result1=adminService.backFeedBackByWorkId(id,backValue);
        if(result1==1){
            result.setMessage("回复反馈成功！");
        }else{
            result.setState(0);
            result.setMessage("回复反馈失败！");
        }
        return result;
    }





//    //查询所有业主信息
//    @RequestMapping("getAllOwners")
//    public JsonResult getAllOwners(){
//        return new JsonResult(adminService.getAllOwners());
//    }
//
//    //查询所有业主信息
//    @RequestMapping("getAllWorkers")
//    public JsonResult getAllWorkers(){
//        return new JsonResult(adminService.getAllWorkers());
//    }
//
//    //查询所有工单信息
//    @RequestMapping("getAllOrders")
//    public JsonResult getAllOrders(){
//        return new JsonResult(adminService.getAllOrders());
//    }

}
