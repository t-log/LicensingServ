package com.optimagrowth.license.service.client;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.optimagrowth.license.model.Organisation;

//@FeignClient("organisation-service")
@Component
public interface OrganisationFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="/v1/organisation/{organisationId}",
            consumes="application/json")
    Organisation getOrganisation(@PathVariable("organisationId") String organisationId);
}