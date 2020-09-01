package com.yc.property.Controller;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.Service.WorkerService;
import com.yc.property.annotation.PassToken;
import com.yc.property.annotation.UserLoginToken;
import com.yc.property.util.JsonResult;
import com.yc.property.util.TokenUtil;
import com.yc.property.vo.WorkerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@RequestMapping("/Worker/")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    //phone
    @UserLoginToken
    @RequestMapping("getWorkerById")
    public JsonResult getWorkerById(String phone){
        Worker worker=new Worker();
        worker=workerService.getWorkerByPhone(phone);
        return new JsonResult(worker);
    }

    //维修工登录
    @PassToken
    @RequestMapping("workerLogin")
    public JsonResult workerLogin(@RequestBody HashMap<String ,String > map, HttpSession session){
        Worker worker=new Worker();
        System.out.println("维修工登录，访问后端成功");
        System.out.println("phone:"+map.get("phone"));
        System.out.println("pwd:"+map.get("pwd"));
        JsonResult result=new JsonResult();
        String password=map.get("pwd");
        String phone=map.get("phone");
        worker.setPassword(password);
        worker.setPhone(phone);
        Worker worker1=workerService.getWorkerByPhone(worker.getPhone());
        if(worker1==null){
            result.setState(0);
            result.setMessage("不存在该用户！");
        }else{
            if(worker1.getPassword().equals(worker.getPassword())){
                session.setAttribute("WORKER",worker1);
                String token= TokenUtil.createJwtToken(phone,password);
                System.out.println("token:"+token);
                WorkerVo workerVo=new WorkerVo(worker1,token);
                System.out.println("workerVo:");
                System.out.println(workerVo);
                result.setMessage("登录成功！");
                result.setData(workerVo);
            }else{
                result.setState(0);
                result.setMessage("密码错误！");
            }
        }
        return result;
    }

    //维修工忘记密码
    @RequestMapping("forgetPassword")
    public JsonResult forgetPassword(Worker worker,HttpSession session){
        JsonResult result =new JsonResult();
        System.out.println(worker);
        Worker relWorker=new Worker();
        relWorker=workerService.forgetPassword(worker);
        if(relWorker==null){
            result.setMessage("输入信息有误，找回密码失败！");
            result.setState(0);
        }else{
            result.setData(relWorker);
        }
        return result;
    }

    //修改密码
    @RequestMapping("reSetPassword")
    public JsonResult reSetPassword(@RequestBody HashMap<String ,String > map,HttpSession session){
        JsonResult result=new JsonResult();
        Worker worker=new Worker();
        worker.setPhone(map.get("workerPhone"));
        worker.setPassword(map.get("newPassword"));
        int result1=workerService.reSetPassword(worker);
        if(result1==1){
            result.setMessage("修改密码成功！");
            Worker relWorer=workerService.getWorkerByPhone(worker.getPhone());
            result.setData(relWorer);
            session.setAttribute("WORKER",relWorer);
        }else{
            result.setMessage("修改密码失败！");
            result.setState(0);
        }
        return result;
    }

    //判断维修工是否登录
    @RequestMapping("isWorkerLogin")
    public JsonResult isWorkerLogin(HttpSession session){
        JsonResult result=new JsonResult();
        Worker worker=(Worker)session.getAttribute("WORKER");
        if(worker==null){
            result.setState(0);
            result.setMessage("维修工未登录");
        }else{
            result.setMessage("维修工已登录");
        }
        return result;
    }

    //
}
