package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.OrganizationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, Long> {
    List<OrganizationMember> findByOrganizationId(Long organizationId);
    Optional<OrganizationMember> findByOrganizationIdAndMemberId(Long organizationId, Long memberId);
}