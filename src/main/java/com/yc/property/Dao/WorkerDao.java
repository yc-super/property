package com.yc.property.Dao;

import com.yc.property.Bean.Worker;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface WorkerDao {
    Worker getWorkerByPhone(String phone);
    Worker forgetPassword(Worker worker);
    int reSetPassword(Worker worker);
}
