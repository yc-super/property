package com.yc.property.Service.ServiceImpl;

import com.yc.property.Bean.Owner;
import com.yc.property.Bean.Worker;
import com.yc.property.Dao.WorkerDao;
import com.yc.property.Service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class WorkerServiceImpl implements WorkerService {
    @Autowired
    private WorkerDao workerDao;
    @Override
    public Worker getWorkerByPhone(String phone) {
        return workerDao.getWorkerByPhone(phone);
    }

    @Override
    public Worker forgetPassword(Worker worker) {
        if(StringUtils.isEmpty(worker.getCardId())||StringUtils.isEmpty(worker.getName())||StringUtils.isEmpty(worker.getPhone())||StringUtils.isEmpty(worker.getMail())){
            return null;
        }else{
            return workerDao.forgetPassword(worker);
        }
    }

    @Override
    public int reSetPassword(Worker worker) {
        return workerDao.reSetPassword(worker);
    }
}
