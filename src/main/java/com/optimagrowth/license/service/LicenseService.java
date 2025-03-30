package com.optimagrowth.license.service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organisation;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganisationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganisationFeignClient;
import com.optimagrowth.license.service.client.OrganisationRestTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Service
public class LicenseService {
    @Autowired
    @Qualifier(value = "msgSrc")
    MessageSource messages;

    @Autowired
    LicenseRepository licenseRepository;


    @Autowired
    ServiceConfig config;

//    @Autowired
//    OrganisationFeignClient organisationFeignClient;

    @Autowired
    OrganisationRestTemplateClient organisationRestClient;

    @Autowired
    OrganisationDiscoveryClient organisationDiscoveryClient;

    public License getLicense(String licenseId, String organisationId) {
        List<License> license = licenseRepository.findByOrganisationIdAndLicenseId(organisationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null), licenseId, organisationId));
        }
        return license.get(0).withComment(config.getProperty());
    }

    public License createLicense(License license) {
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license) {
        licenseRepository.save(license);

        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId) {
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null), licenseId);
        return responseMessage;
    }
    public License getLicense(String organisationId, String licenseId, String
            clientType){
        List<License> licenseList = licenseRepository.findByOrganisationIdAndLicenseId(organisationId, licenseId);
        License license = licenseList.get(0);
        if (null == license) {
            throw new IllegalArgumentException(String.format(
                    messages.getMessage("license.search.error.message", null, null),
                    licenseId, organisationId));
        }
        Organisation organisation = retrieveOrganisationInfo(organisationId,
                clientType);
        if (null != organisation) {
            license.setOrganisationName(organisation.getName());
            license.setContactName(organisation.getContactName());
            license.setContactEmail(organisation.getContactEmail());
            license.setContactPhone(organisation.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }
    private Organisation retrieveOrganisationInfo(String organisationId, String clientType) {
        Organisation organisation = null;

        switch (clientType) {
            case "feign":
//                System.out.println("I am using the feign client");
//                organisation = organisationFeignClient.getOrganisation(organisationId);
                break;
            case "rest":
                System.out.println("I am using the rest client");
                organisation = organisationRestClient.getOrganisation(organisationId);
                break;
            case "discovery":
                System.out.println("I am using the discovery client");
                organisation = organisationDiscoveryClient.getOrganisation(organisationId);
                break;
            default:
                organisation = organisationRestClient.getOrganisation(organisationId);
                break;
        }

        return organisation;
    }

}
