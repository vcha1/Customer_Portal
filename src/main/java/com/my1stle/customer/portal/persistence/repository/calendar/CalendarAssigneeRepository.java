package com.my1stle.customer.portal.persistence.repository.calendar;

import com.my1stle.customer.portal.service.model.Product;
import com.dev1stle.scheduling.system.v1.model.Calendar;
import com.dev1stle.scheduling.system.v1.model.Skill;
import com.dev1stle.scheduling.system.v1.model.Tag;
import com.dev1stle.scheduling.system.v1.service.assignee.AssigneeService;
import com.dev1stle.scheduling.system.v1.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CalendarAssigneeRepository {

    private TagService tagService;
    private AssigneeService assigneeService;

    @Autowired
    public CalendarAssigneeRepository(TagService tagService, AssigneeService assigneeService) {
        this.tagService = tagService;
        this.assigneeService = assigneeService;
    }


    /**
     * This implementation finds the assignees by operational area and assignees with all required skills
     * for the product
     */
    public List<Calendar> selectBySalesforceCustomerAndProduct(String operationalArea, Product product) {

        List<Tag> operationalAreas = this.tagService.findByNameIn(Collections.singletonList(operationalArea));
        Set<Skill> requiredSkills = product.getSkills();

        List<Calendar> calendars = this.assigneeService.findBySkillsAndTags(
                requiredSkills.stream().map(Skill::getId).collect(Collectors.toSet()),
                operationalAreas.stream().map(Tag::getId).collect(Collectors.toSet())
        ).stream().filter(assignee -> assignee.getSkills().containsAll(requiredSkills)).collect(Collectors.toList());

        return calendars;

    }

    public List<Calendar> selectByOperationalAreaAndSkillIds(String operationalArea, Set<Long> skillIds) {

        Set<Long> operationalAreasIds = this.tagService.findByNameIn(Collections.singletonList(operationalArea))
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        List<Calendar> assignees = this.assigneeService.findBySkillsAndTags(skillIds, operationalAreasIds);

        return assignees.stream()
                .filter(assignee -> assignee.getSkills().stream().map(Skill::getId).collect(Collectors.toSet()).containsAll(skillIds))
                .collect(Collectors.toList());


    }

}
