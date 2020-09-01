package com.yc.property.Controller;

import com.yc.property.Bean.OrderDetail;
import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Service.OwnerService;
import com.yc.property.Service.WorkerService;
import com.yc.property.annotation.PassToken;
import com.yc.property.util.JsonResult;
import com.yc.property.util.TokenUtil;
import com.yc.property.vo.OwnerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashMap;

@RestController
@RequestMapping("/Owner/")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private OwnerDao ownerDao;

    //业主登录
    @PassToken
    @RequestMapping(value="ownerLogin",method= RequestMethod.POST)
        public JsonResult ownerLogin(@RequestBody HashMap<String ,String > map, HttpSession session){
//    public JsonResult ownerLogin(Owner owner, HttpSession session){
        System.out.println("业主登录，访问后端成功");
        System.out.println("phone:"+map.get("phone"));
        System.out.println("pwd:"+map.get("pwd"));
        System.out.println("访问后端成功");
        JsonResult result=new JsonResult();
        String password=map.get("pwd");
        String phone=map.get("phone");
        Owner owner1=new Owner();
        owner1=ownerService.getOwnerByPhone(phone);
        if(owner1!=null){
            if(password.equals(owner1.getPassword())){
                String token= TokenUtil.createJwtToken(phone,password);
                result.setMessage("登录成功！");
                System.out.println("登录成功，token：");
                System.out.println(token);
                OwnerVo ownerVo1=ownerDao.getValueByRoomId(owner1.getRoomId());
                OwnerVo ownerVo=new OwnerVo(owner1,ownerVo1,token);
                result.setData(ownerVo);
            }else{
                result.setMessage("密码错误！");
                result.setState(0);
            }
        }else{
            result.setMessage("用户不存在！");
            result.setState(0);
            /*//不存在该用户，进行注册
            ownerService.regeisterOwner(phone,password);
            //查询新注册的用户信息
            Owner owner2=ownerService.getOwnerByPhone(phone);
            //注册tooken
            String tooken=TokenUtil.createJwtToken(phone,password);
            OwnerVo ownerVo=new OwnerVo(owner2,tooken);
            result.setData(ownerVo);
            System.out.println("!!jiancha  :");
            System.out.println(ownerVo);
            result.setMessage("注册成功！");
            result.setState(-1);*/
        }
        return result;
    }

//    //忘记密码
//    @RequestMapping("forgetPassword")
//    public JsonResult forgetPassword(Owner owner,HttpSession session){
//        JsonResult result =new JsonResult();
//        System.out.println(owner);
//        Owner relOwner=new Owner();
//        relOwner=ownerService.forgetPassword(owner);
//        if(relOwner==null){
//            result.setMessage("输入信息有误，找回密码失败！");
//            result.setState(0);
//        }else{
//            result.setMessage("找回密码输入信息正确！");
//            result.setData(relOwner);
//        }
//        return result;
//    }

    //修改密码
    @RequestMapping("reSetPassword")
    public JsonResult reSetPassword(@RequestBody HashMap<String ,String > map,HttpSession session){
        JsonResult result=new JsonResult();
        Owner owner=new Owner();
        owner.setPhone(map.get("ownerPhone"));
        owner.setPassword(map.get("newPassword"));
        int result1=ownerService.reSetPassword(owner);
        if(result1==1){
            result.setMessage("修改密码成功！");
            Owner relOwner=ownerService.getOwnerByPhone(owner.getPhone());
            result.setData(relOwner);
            session.setAttribute("OWNER",relOwner);
        }else{
            result.setMessage("修改密码失败！");
            result.setState(0);
        }
        return result;
    }

    //判断业主是否登录
    @RequestMapping("isOwnerLogin")
    public JsonResult isOwnerLogin(HttpSession session){
        JsonResult result=new JsonResult();
        Owner owner=(Owner)session.getAttribute("OWNER");
        if(owner==null){
            result.setState(0);
            result.setMessage("未登录");
        }else{
            result.setMessage("已登录");
        }
        return result;
    }

    //修改个人信息
    @RequestMapping("updateOwnerMessage")
    public JsonResult updateOwnerMessage(@RequestBody HashMap<String ,String > map){
        JsonResult result=new JsonResult();
        int roomId=ownerDao.getRoomIdByBuildingIdAndAddress(Integer.parseInt(map.get("buildingId")),Integer.parseInt(map.get("address")));
        Owner owner=new Owner();
        owner.setPhone(map.get("ownerPhone"));
        owner.setPassword(map.get("password"));
        owner.setRoomId(roomId);
        owner.setName(map.get("name"));
        owner.setSex(map.get("sex"));
        owner.setMail(map.get("mail"));
        owner.setCardId(map.get("cardId"));
        int result1=ownerService.updateOwnerMessage(owner);
        if(result1==1){
            result.setMessage("修改个人信息成功！");
        }else{
            result.setState(0);
            result.setMessage("修改个人信息失败！");
        }
        return result;
    }

    //查询业主以往订单
    @RequestMapping("findOldOrderByPhone")
    public JsonResult findOldOrderByPhone(String phone){
        return new JsonResult(ownerService.findOldOrderByPhone(phone));
    }

    @PassToken
    @RequestMapping("getPicture")
    public void getPicture(String imgurl){

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!接收图片");
        System.out.println(imgurl);


    }
}
