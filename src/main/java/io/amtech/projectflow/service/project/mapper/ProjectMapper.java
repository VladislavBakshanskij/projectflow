package io.amtech.projectflow.service.project.mapper;

import io.amtech.projectflow.dto.response.project.LeadDto;
import io.amtech.projectflow.dto.response.project.ProjectDirectionDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ProjectMapper {
    public static ProjectDto toDto(final ProjectWithEmployeeDirection project) {
        return new ProjectDto()
                .setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setProjectStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setLead(new LeadDto()
                                 .setId(project.getLead().getId())
                                 .setName(project.getLead().getName()))
                .setDirection(new ProjectDirectionDto()
                                      .setId(project.getDirection().getId())
                                      .setName(project.getDirection().getName()));
    }
}
