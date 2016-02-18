package com.capgemini.resilience.travel.rest;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import com.capgemini.resilience.travel.model.Travel;
import com.capgemini.resilience.travel.service.CostCenterProxy;
import com.capgemini.resilience.travel.service.EmployerProxy;
import com.capgemini.resilience.travel.service.TravelService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by kso on 16.02.16.
 */
@RestController
public class TravelRestService {

    @Inject
    private TravelService service;
    @Inject
    private CostCenterProxy costCenterProxy;
    @Inject
    private EmployerProxy employerProxy;

    @HystrixCommand
    @RequestMapping(value = "/travel/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<TravelTO> get(@PathVariable("id") Long id) {
        Travel travel = service.read(id);
        CostCenterTO costCenterTO = costCenterProxy.get(travel.getCostCenterNumber());
        if(costCenterTO == null)
            return new ResponseEntity<TravelTO>(HttpStatus.PRECONDITION_FAILED);
//        if(costCenterTO.getId() == null) // fallback
//            return new ResponseEntity<TravelTO>(HttpStatus.FAILED_DEPENDENCY);
        EmployerTO employerTO = employerProxy.get(travel.getEmployerNumber());
        if(employerTO == null)
            return new ResponseEntity<TravelTO>(HttpStatus.PRECONDITION_FAILED);
//        if(employerTO.getId() == null) // fallback
//            return new ResponseEntity<TravelTO>(HttpStatus.FAILED_DEPENDENCY);

        TravelTO travelTO = new TravelTO(
                travel.getId(),
                travel.getNumber(),
                travel.getDescription(),
                travel.getStartDate(),
                travel.getEndDate(),
                travel.getStatus(),
                costCenterTO,
                employerTO);

        return new ResponseEntity<TravelTO>(travelTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/travel/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        this.service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @HystrixCommand
    @RequestMapping(value = "/travel", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON})
    @ResponseBody
    public ResponseEntity<String> saveOrUpdate(@RequestBody TravelTO travel) {
        CostCenterTO costCenterTO = costCenterProxy.get(travel.getCostCenter().getNumber());
        if(costCenterTO == null)
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
//        if(costCenterTO.getId() == null) // fallback
//            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);
        EmployerTO employerTO = employerProxy.get(travel.getEmployer().getNumber());
        if(employerTO == null)
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
//        if(employerTO.getId() == null) // fallback
//            return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);


        this.service.saveOrUpdate(new Travel(
                travel.getId(),
                travel.getNumber(),
                travel.getDescription(),
                travel.getStartDate(),
                travel.getEndDate(),
                travel.getStatus(),
                travel.getCostCenter().getNumber(),
                travel.getEmployer().getNumber()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
