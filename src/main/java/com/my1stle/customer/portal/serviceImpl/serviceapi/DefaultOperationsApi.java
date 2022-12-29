package com.my1stle.customer.portal.serviceImpl.serviceapi;

import com.my1stle.customer.portal.service.serviceapi.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class DefaultOperationsApi implements OperationsApi {

    private ServiceScheduleRequestApi serviceScheduleRequestApi;

    @Autowired
    public DefaultOperationsApi(ServiceScheduleRequestApi serviceScheduleRequestApi){
        this.serviceScheduleRequestApi = serviceScheduleRequestApi;
    }

    @Override
    public ServiceScheduleRequestApi scheduleRequest(){
       return this.serviceScheduleRequestApi;
    }

}
