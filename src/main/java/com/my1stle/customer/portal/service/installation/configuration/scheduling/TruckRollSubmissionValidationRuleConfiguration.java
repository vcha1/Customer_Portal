package com.my1stle.customer.portal.service.installation.configuration.scheduling;

import com.dev1stle.scheduling.system.v1.service.truck.roll.ExistingTruckRollService;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.CaseMultipleAssigneeValidationRule;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollAlreadyExistsValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollChooseStartDateTimeValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollDurationValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollFixedAppointmentValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollNameValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollNeedsConfirmationValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollRelatedRecordValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollSkillValidation;
import com.dev1stle.scheduling.system.v1.service.truck.roll.validation.TruckRollSubmissionValidationRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TruckRollSubmissionValidationRuleConfiguration {

    @Bean
    @Autowired
    public List<TruckRollSubmissionValidationRule> defaultRules(ExistingTruckRollService existingTruckRollService) {
        List<TruckRollSubmissionValidationRule> rules = new ArrayList<>();
        rules.add(new TruckRollAlreadyExistsValidation(existingTruckRollService));
        rules.add(new TruckRollChooseStartDateTimeValidation());
        rules.add(new TruckRollNeedsConfirmationValidation());
        rules.add(new TruckRollDurationValidation());
        rules.add(new TruckRollFixedAppointmentValidation());
        rules.add(new TruckRollNameValidation());
        rules.add(new TruckRollRelatedRecordValidation());
        rules.add(new TruckRollSkillValidation());
        rules.add(new CaseMultipleAssigneeValidationRule());
        return rules;
    }

}
