package com.qfieldcloud.core.application.controller;

import com.qfieldcloud.core.application.dto.OrganizationMemberDto;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import com.qfieldcloud.core.domain.model.Organization;
import com.qfieldcloud.core.domain.model.OrganizationMember;
import com.qfieldcloud.core.domain.repository.OrganizationMemberRepository;
import com.qfieldcloud.core.domain.repository.OrganizationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/members")
public class OrganizationMemberController {

    private final OrganizationMemberRepository memberRepository;
    private final OrganizationRepository organizationRepository;

    public OrganizationMemberController(OrganizationMemberRepository memberRepository,
                                        OrganizationRepository organizationRepository) {
        this.memberRepository = memberRepository;
        this.organizationRepository = organizationRepository;
    }

    @GetMapping("/{organization}")
    public ResponseEntity<List<OrganizationMemberDto>> listMembers(@PathVariable String organization) {
        Organization org = organizationRepository.findByUsername(organization)
                .orElseThrow(() -> new ObjectNotFoundException("Organization not found: " + organization));
        List<OrganizationMember> members = memberRepository.findByOrganizationId(org.getId());
        List<OrganizationMemberDto> dtos = members.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private OrganizationMemberDto toDto(OrganizationMember member) {
        return OrganizationMemberDto.builder()
                .id(member.getId())
                .organizationId(member.getOrganization() != null ? member.getOrganization().getId() : null)
                .organizationUsername(member.getOrganization() != null ? member.getOrganization().getUsername() : null)
                .memberId(member.getMember() != null ? member.getMember().getId() : null)
                .memberUsername(member.getMember() != null ? member.getMember().getUsername() : null)
                .role(member.getRole())
                .isPublic(member.getIsPublic())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}