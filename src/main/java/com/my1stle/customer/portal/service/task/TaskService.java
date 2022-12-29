package com.my1stle.customer.portal.service.task;

import com.my1stle.customer.portal.service.model.Task;

public interface TaskService<R extends Task, T extends TaskSpecification> {

    /**
     * Adds a task to call center
     *
     * @param taskSpecification
     * @return task
     * @throws TaskServiceException
     */
    R add(T taskSpecification) throws TaskServiceException;

}