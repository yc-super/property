package com.yc.property.Service.ServiceImpl;

import com.yc.property.Bean.Order;
import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Type;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.ApplyPropertyDao;
import com.yc.property.Dao.OwnerDao;
import com.yc.property.Dao.WorkPropertyDao;
import com.yc.property.Dao.WorkerDao;
import com.yc.property.Service.ApplyPropertyService;
import com.yc.property.Service.OwnerService;
import com.yc.property.vo.OrderVo;
import com.yc.property.vo.OwnerVo;
import com.yc.property.vo.TypeAndNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ApplyPropertyImpl implements ApplyPropertyService {
    @Autowired
    private ApplyPropertyDao applyPropertyDao;
    @Autowired
    private OwnerDao ownerDao;
    @Autowired
    private WorkerDao workerDao;
    @Autowired
    private WorkPropertyDao workPropertyDao;
    @Override
    public ArrayList<Type> getAllType() {
        return applyPropertyDao.getAllType();
    }

    @Override
    public ArrayList<Worker> getAllWorker() {
        return applyPropertyDao.getAllWorker();
    }

    @Override
    public int getOwnerUnfinishedOrderNum(String ownerPhone){
        return applyPropertyDao.getOwnerUnfinishedOrderNum(ownerPhone);
    }

    @Override
    public int getWorkerUnfinishedOrderNum(String workerId) {
        return applyPropertyDao.getWorkerUnfinishedOrderNum(workerId);
    }

    @Override
    public int createOrder(String ownerPhone, String workerPhone) {
        //下面创建订单号：
        // 之前的设想：16位：年+月+日+楼号2位+房间号3位+1位标识（同一天第几次，要求每天申请次数不得多于5次，再每次申请维修时判断）+4位维修工手机号后4位
        //现在：年+月+日+标志位，如20060103,20年6月1号，当天第3个申请的人
        String orderId;

//        //1位标识，表示这是该业主今天第几次申请维修
//        int num=applyPropertyDao.getApplyNumByOwnerPhone(ownerPhone);
//        int num2=num+1;
//        //维修工标识，维修工手机号后4位
//        String workerState=workerPhone.substring(workerPhone.length()-4,workerPhone.length());//获得后4位,subString:前闭后闭

        //获取当天日期，年月日
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        String nowDay = df.format(System.currentTimeMillis()).toString();
        System.out.println(nowDay);//结果200602
        //根据ownerPhone查业主信息
//        Owner owner = ownerDao.getOwnerByPhone(ownerPhone);
//        String buildingState=String.valueOf(owner.getBuildingId());
//        if(owner.getBuildingId()<10){
//            buildingState="0"+owner.getBuildingId();
//        }
        //查询最大订单号
        String maxOrderId=applyPropertyDao.getMaxOrderId();
        String index=maxOrderId.substring(0,maxOrderId.length()-2);//得到几年几月几号，如200601:20年6月1号
        if(nowDay.equals(index)){//判断最大的订单Id是否是今天的
            String index2=maxOrderId.substring(maxOrderId.length()-2,maxOrderId.length());//获取后两位
            int index3=Integer.valueOf(index2);
            index3+=1;
            String index4=String.valueOf(index3);
            if(index3<10){
                index4="0"+index4;//得到标志位
            }
            orderId=index+index4;//比如：20060103
        }else{
            orderId=nowDay+"01";
        }
//        String orderId=nowDay+workerState+buildingState+owner.getAddress()+num2;
        System.out.println("applyPropertyImpl+orderId:"+orderId);
        return applyPropertyDao.createOrder(orderId,ownerPhone,workerPhone);
    }

    @Override
    public String  getMaxOrderId(String ownerPhone) {
        return applyPropertyDao.getNewestPropertyOrderId(ownerPhone);
    }

    @Override
    public int createOrderDetail(OrderVo orderVo) {
        return applyPropertyDao.createOrderDetail(orderVo);
    }

    @Override
    public OrderVo getOrderDetailByOrderId(String orderId) {
        return applyPropertyDao.getOrderDetailByOrderId2(orderId);
    }

    @Override
    public int startProperty(String orderId , Date date) {
        return applyPropertyDao.startProperty(orderId,date);
    }

    @Override
    public int endProperty(String orderId,Date date) {
        //此处应有spring事务
        //设置结束时间
        int i=applyPropertyDao.endProperty(orderId,date);
        //将该订单的状态status设置为1(已完成)
        int j=applyPropertyDao.setStatus(orderId,1);
        return i==1&&j==1?1:0;
    }
    @Override
    public int createAsess(OrderVo orderVo){
        return  applyPropertyDao.createAsess(orderVo);
    }

    @Override
    public OrderVo getNewestPropertyOrder(String ownerPhone) {
        OrderVo orderVo=new OrderVo();
        //获取订单Id
//        try{
        //查询该用户之前是否有订单
        String newestOrderId=applyPropertyDao.getNewestPropertyOrderId(ownerPhone);
        if(StringUtils.isEmpty(newestOrderId)){
            orderVo.setOrderId("0");
            return orderVo;
        }else{
            String orderId=applyPropertyDao.getNewestPropertyOrderId(ownerPhone);
            System.out.println("orderId:"+orderId);
            //获取orderdetail
            orderVo=applyPropertyDao.getOrderDetailByOrderId2(orderId);
            //设置下asess
            if(orderVo.getAsess()==0){
                orderVo.setAsess(-1);
                orderVo.setIfAsess(false);
            }


            //设置ownerId,实际上是owner的phone
            orderVo.setOwnerId(ownerPhone);
            //获取order

            Order order=applyPropertyDao.getOrderByOrderId(orderId);
            //设置维修工手机号
            String workerPhone=order.getWorkerId();
            orderVo.setWorkerId(workerPhone);
            //设置订单状态
//            applyPropertyDao.get
            orderVo.setStatus(order.getStatus());
            //设置维修类别名称
            String typeName=applyPropertyDao.getTypenameByTypeid(orderVo.getTypeId());
            orderVo.setTypeName(typeName);
            System.out.println("typename:"+typeName);
            //        //设置该维修订单业主姓名
            //        Owner owner=ownerDao.getOwnerById(ownerCardId);
            //        orderVo.setOwnerName(owner.getName());
            //设置该维修订单维修工姓名
            System.out.println("workerPhone:"+workerPhone);
            Worker worker=workerDao.getWorkerByPhone(workerPhone);
            System.out.println("worker:--");
            System.out.println(worker);
            orderVo.setWorkerName(worker.getName());
            //设置该维修工待维修工单数（排队人数）,需要订单id和workerPhone
            int num=applyPropertyDao.getQueueNum(workerPhone,orderId);
            //若已开始本次维修，及start_time不为null,则排队人数为0
            Order order1=applyPropertyDao.getOrderByOrderIdAndStartTime(orderId);
            if(order1!=null){
                num=0;
            }
            orderVo.setQueueNum(num);

            //以下计算预计时间
            String  waitTime=null;
            //若已开始维修，则排队预计时间为0
            if(order1!=null){
                waitTime="0小时0分钟";
            }
            //查询该维修工待维修工单中有哪些维修类别,以及各种类别对应的个数
            List<TypeAndNum> types=applyPropertyDao.getUnfinishedTypes(workerPhone,orderId);
            int time=0;//单位：秒，用于累计预计总时间
            System.out.println("开始遍历TypeAndNum");
            for(TypeAndNum type:types){
                System.out.println(type.getTypeId()+":"+type.getNum());
                time+=calWaitTimeByTypeAndQueueNum(type.getTypeId(),type.getNum(),workerPhone);
            }
//            for(HashMap<Integer,Integer> type:types){
//                Iterator it=type.entrySet().iterator();
//                Integer typeId=type.get("typeId");
//                Integer typeNum=type.get("num");
//                time+=calWaitTimeByTypeAndQueueNum(typeId,typeNum);
//            }

            //将 秒 转化为 几天几小时几分钟
            int day=time/(60*60*24);
            int hours=(time-day*(60*60*24))/(60*60);
            int minutes=(time-day*(60*60*24)-hours*(60*60))/60;
            System.out.println(hours+"小时"+minutes+"分钟");
            if(day==0){
                waitTime=hours+"小时"+minutes+"分钟";
            }else if(hours==0){
                waitTime=minutes+"分钟";
            }else{
                waitTime=day+"天"+hours+"小时"+minutes+"分钟";
            }

            //设置预计时间
            orderVo.setWaitTime(waitTime);
//            calWaitTimeByTypeAndQueueNum(orderVo.getTypeId(),num);
            return orderVo;
        }

//        }catch(Exception e){
//            System.out.println("此处抛出异常，获得值为null");
//            return orderVo;
//        }
    }

    //预计某维修类别时间，返回：秒，int类型
    private int calWaitTimeByTypeAndQueueNum(int typeId,int typeNum,String workerPhone){
        //计算某种维修类别的预计时间，计算规则：取该维修工该维修类别的最近50个订单中的中位数作为该维修类别的预计时间
        System.out.println("!!执行calWaitTimeByTypeAndQueueNum方法");
        ArrayList<Integer> times=applyPropertyDao.getTimesByTypeId(typeId,workerPhone);
        ArrayList<Integer> times2=null;
        System.out.println("times长度为"+times.size()+"，开始遍历：");
        if(times.size()==0){
            System.out.println("times长度为0，结束遍历");
            return 0;//初始化数据,若该维修类别之前没有记录，即该维修工之前就没有维修过该类别的订单，那预计时间就为0
        }
        for(Integer time:times){
            System.out.println(time+",");
        }
        if(times.size()>50){
            times2.addAll(times);
            times.clear();
            times.addAll(times2);
        }
        int centerTime;//中间值，单位：秒
        if(times.size()%2==1){
            centerTime=times.get(times.size()/2);
        }else{
            centerTime=(times.get(times.size()/2)+times.get(times.size()/2-1))/2;
        }
        System.out.println("typeId是"+typeId+",预估时间为："+centerTime/60+"分钟，排队人数为："+typeNum);
        //以上是某一个维修类别的预估时间
        centerTime=centerTime*typeNum;
        return centerTime;

    }

    @Override
    public int cancelProperty(String orderId) {
        applyPropertyDao.endProperty(orderId,new Date());
        return applyPropertyDao.setStatus(orderId,-1);
    }

    @Override
    public int getApplyNumByOwnerPhone(String ownerPhone) {
        return applyPropertyDao.getApplyNumByOwnerPhone(ownerPhone);
    }

    @Override
    public int getMaxPriorityByWorkerPhone(String workerPhone) {
        return applyPropertyDao.getMaxPriorityByWorkerPhone(workerPhone);
    }

    @Override
    public int setMaxPriorityByOrderId(String orderId, int maxPriority) {
        return applyPropertyDao.setMaxPriorityByOrderId(orderId,maxPriority);
    }

    @Override
    public int setPingjiaByOrderId(String orderId, int asess) {
        return applyPropertyDao.setPingjiaByOrderId(orderId,asess);
    }

    @Override
    public String getCalTimeByWorkerPhone(String workerPhone) {
        //以下计算预计时间
        String  waitTime=null;
        //查询该维修工待维修工单中有哪些维修类别,以及各种类别对应的个数
        List<TypeAndNum> types=applyPropertyDao.getUnfinishedTypes2(workerPhone);
        int time=0;//单位：秒，用于累计预计总时间
        System.out.println("开始遍历TypeAndNum");
        for(TypeAndNum type:types){
            System.out.println(type.getTypeId()+":"+type.getNum());
            time+=calWaitTimeByTypeAndQueueNum(type.getTypeId(),type.getNum(),workerPhone);
        }

        //将 秒 转化为 几天几小时几分钟
        int day=time/(60*60*24);
        int hours=(time-day*(60*60*24))/(60*60);
        int minutes=(time-day*(60*60*24)-hours*(60*60))/60;
        System.out.println(hours+"小时"+minutes+"分钟");
        if(day==0){
            waitTime=hours+"小时"+minutes+"分钟";
        }else if(hours==0){
            waitTime=minutes+"分钟";
        }else{
            waitTime=day+"天"+hours+"小时"+minutes+"分钟";
        }

        return waitTime;
    }

    @Override
    public String getAvgAsessByWorkerPhone(String workerPhone) {
        return applyPropertyDao.getAvgAsessByWorkerPhone(workerPhone);
    }

    @Override
    public ArrayList<OrderVo> getTypeOrderList(String ownerPhone,String keyWords,String values) {
        ArrayList<OrderVo> orderVos=new ArrayList<OrderVo>();
        if(keyWords.equals("待维修")){
            orderVos=applyPropertyDao.getUnfinishedOrderByOwnerPhone(ownerPhone);
        }else if(keyWords.equals("未评价")){
            orderVos=applyPropertyDao.getNoAsessOrderByOwnerPhone(ownerPhone);
        }else if(keyWords.equals("已维修")){
            orderVos=applyPropertyDao.getFinishedOrderByOwnerPhone(ownerPhone);
        }else if(keyWords.equals("已取消")){
            orderVos=applyPropertyDao.getCancelOrderByOwnerPhone(ownerPhone);
        }else if(keyWords.equals("全部订单")){
            orderVos=ownerDao.findOldOrderByPhone(ownerPhone);
        }else if(keyWords.equals("按订单号搜索")){
            System.out.println("values:"+values);
            String orderId=values;
            OrderVo orderVo2=applyPropertyDao.isOrderExistByOrderIdAndOwnerPhone(orderId,ownerPhone);
            if(orderVo2==null){
                return orderVos;
            }else{
                orderVos=workPropertyDao.getOrderDetailByOrderId(orderId);
            }
        }else if(keyWords.equals("按维修工姓名搜索")){
            System.out.println("values:"+values);
            String worderName=values;
            orderVos=applyPropertyDao.getOrderByOwnerPhoneAndWorkerPhone(ownerPhone,worderName);
        }else {
            System.out.println("keywords不合法！");
            return orderVos;
        }
        orderVos=getCompleteOrder(orderVos);
        return orderVos;
    }

    private ArrayList<OrderVo> getCompleteOrder(ArrayList<OrderVo> orderVos){
        for(OrderVo orderVo:orderVos){
            String orderId=orderVo.getOrderId();
            Worker worker=workPropertyDao.getWorkerByOrderId(orderId);
            Owner owner=workPropertyDao.getOwnerByOrderId(orderId);
            int status=workPropertyDao.getStatusByOrderId(orderId);
            int typeId=orderVo.getTypeId();
            String typeName=workPropertyDao.getTypeNameByTypeId(typeId);
            orderVo.setStatus(status);
            orderVo.setTypeName(typeName);
            orderVo.setWorkerName(worker.getName());
            orderVo.setWorkerId(worker.getPhone());
            orderVo.setOwnerName(owner.getName());

            OwnerVo ownerVo=ownerDao.getValueByRoomId(owner.getRoomId());

            orderVo.setOwnerId(owner.getPhone());
            orderVo.setBuildingId(ownerVo.getBuildingId());
            orderVo.setAddress(ownerVo.getAddress());


            orderVo.setImgPath(applyPropertyDao.getImgPathByImgId(orderVo.getImgId()));
            System.out.println("orderVo  imgId:"+orderVo.getImgId()+"图片path:"+orderVo.getImgPath());
        }
        return orderVos;
    }

    @Override
    public int savePicture(String id,String path){
        //查找是否有id==id的图片，有则更改路径，没有则增加一条
        int num=applyPropertyDao.getImgNumByImgId(id);
        int result;
        if(num==0){
            result = applyPropertyDao.savePicture(id, path);
        }else{
            result = applyPropertyDao.updateImg(id,path);
        }
        return result;
    }
}
