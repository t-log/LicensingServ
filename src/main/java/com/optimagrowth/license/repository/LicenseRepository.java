package com.optimagrowth.license.repository;

import com.optimagrowth.license.model.License;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends CrudRepository<License,String> {
    public List<License> findByOrganisationId(String organisationId);
    @Query(value = "SELECT l FROM License l WHERE l.organisationId =:organisationId AND l.licenseId =:licenseId")
    public License findByOrganisationIdAndLicenseId(@Param("organisationId")String organisationId,@Param("licenseId")String licenseId);
}
