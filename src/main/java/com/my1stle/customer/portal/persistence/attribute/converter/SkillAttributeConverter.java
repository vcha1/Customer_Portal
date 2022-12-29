package com.my1stle.customer.portal.persistence.attribute.converter;

import com.dev1stle.scheduling.system.v1.model.Skill;
import com.dev1stle.scheduling.system.v1.service.skill.SkillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Converter
@Configurable
public class SkillAttributeConverter implements AttributeConverter<Set<Skill>, String> {

    /**
     * @see <a href="https://stackoverflow.com/questions/47219421/accessing-spring-beans-inside-attributeconverter-class">Accessing Spring Beans inside AttributeConverter class</a>
     */
    private static SkillService skillService;

    @Autowired
    private void init(SkillService skillService) {
        SkillAttributeConverter.skillService = skillService;
    }

    @Override
    public String convertToDatabaseColumn(Set<Skill> attribute) {

        List<String> skillIds = new ArrayList<>();

        for (Skill skill : attribute) {
            skillIds.add(skill.getId().toString());
        }

        return String.join(",", skillIds);

    }

    @Override
    public Set<Skill> convertToEntityAttribute(String dbData) {

        if (StringUtils.isBlank(dbData))
            return new HashSet<>();

        // TODO find set of ids instead of iterating
        Set<Skill> skills = new HashSet<>();
        for (String skillId : dbData.split(",")) {
            Optional<Skill> skill = skillService.findById(Long.parseLong(skillId));
            if (skill.isPresent()) {
                skills.add(skill.get());
            }
        }

        return skills;

    }

}
