package com.my1stle.customer.portal.serviceImpl.task;

import com.my1stle.customer.portal.service.task.TaskService;
import com.my1stle.customer.portal.service.task.TaskServiceException;
import com.dev1stle.repository.result.Result;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SalesforceTaskService implements TaskService<SalesforceTask, SalesforceTaskSpecification> {

    private SalesforceObjectRepository<SalesforceTask> repository;

    @Autowired
    public SalesforceTaskService(SalesforceObjectRepository<SalesforceTask> repository) {
        this.repository = repository;
    }

    @Override
    public SalesforceTask add(SalesforceTaskSpecification taskSpecification) throws TaskServiceException {

        SalesforceTask task = SalesforceTask.from(taskSpecification);

        Result insert = this.repository.insert(Collections.singletonList(task));

        if (!insert.isSuccessful()) {
            throw new TaskServiceException(String.join(", ", insert.getErrorMessages()));
        }

        return task;

    }

}