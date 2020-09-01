package com.yc.property.Service;

import com.yc.property.Bean.Worker;


public interface WorkerService {
    Worker getWorkerByPhone(String phone);
    Worker forgetPassword(Worker worker);

    int reSetPassword(Worker worker);
}
