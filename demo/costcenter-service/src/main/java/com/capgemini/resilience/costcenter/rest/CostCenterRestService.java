package com.capgemini.resilience.costcenter.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.capgemini.resilience.costcenter.model.CostCenter;
import com.capgemini.resilience.costcenter.service.CostCenterSearchCriteria;
import com.capgemini.resilience.costcenter.service.CostCenterService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kso on 15.02.16.
 */
@RestController
public class CostCenterRestService {

    @Inject
    private CostCenterService service;

    @RequestMapping(value = "/costcenter/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public CostCenter get(@PathVariable("id") Long id) {
        return service.read(id);
    }

    @RequestMapping(value = "/costcenter/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {

        this.service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/costcenter", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> saveOrUpdate(@RequestBody CostCenter costCenter) {

        this.service.saveOrUpdate(costCenter);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/costcenter", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    List<CostCenter> search(CostCenterSearchCriteria searchCriteria) {
        return service.search(searchCriteria);
    }
}
