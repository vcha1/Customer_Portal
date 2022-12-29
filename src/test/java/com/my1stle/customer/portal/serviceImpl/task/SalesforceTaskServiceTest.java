package com.my1stle.customer.portal.serviceImpl.task;

import com.my1stle.customer.portal.service.model.Task;
import com.my1stle.customer.portal.service.task.TaskService;
import com.my1stle.customer.portal.service.task.TaskServiceException;
import com.dev1stle.repository.result.Result;
import com.dev1stle.repository.salesforce.SalesforceObjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit Test for {@link SalesforceTaskService}
 */
@RunWith(MockitoJUnitRunner.class)
public class SalesforceTaskServiceTest {

    private TaskService<SalesforceTask, SalesforceTaskSpecification> taskService;

    @Mock
    private SalesforceObjectRepository<SalesforceTask> mockRepository;


    @Before
    public void setUp() throws Exception {

        this.taskService = new SalesforceTaskService(mockRepository);

    }

    @Test
    public void shouldAddTask() {

        // Given
        String owner = "salesforce user id";
        String subject = "unit test";
        String status = "High";
        String relatedTo = "record id";
        SalesforceTaskSpecification specification = new SalesforceTaskSpecification.Builder(owner, subject, status, relatedTo).build();

        Result stubResult = mock(Result.class);
        when(stubResult.isSuccessful()).thenReturn(true);
        when(mockRepository.insert(anyList())).thenReturn(stubResult);

        // When
        try {
            Task task = this.taskService.add(specification);
        } catch (TaskServiceException e) {
            fail(e.getMessage());
        }

        // Then
        verify(mockRepository, times(1)).insert(anyList());

    }

    @Test
    public void shouldThrowServiceExceptionWhenUnableToInsertIntoSalesforce() {

        // Given
        String owner = "salesforce user id";
        String subject = "unit test";
        String status = "High";
        String relatedTo = "record id";
        SalesforceTaskSpecification specification = new SalesforceTaskSpecification.Builder(owner, subject, status, relatedTo).build();

        Result stubResult = mock(Result.class);
        when(stubResult.isSuccessful()).thenReturn(false);
        when(stubResult.getErrorMessages()).thenReturn(Collections.singletonList("Stubbed Error!"));
        when(mockRepository.insert(anyList())).thenReturn(stubResult);

        // When
        try {
            Task task = this.taskService.add(specification);
        } catch (TaskServiceException e) {
            // Then
            verify(mockRepository, times(1)).insert(anyList());
            return;
        }

        fail("Expected Task Service Exception to be thrown!");

    }

}