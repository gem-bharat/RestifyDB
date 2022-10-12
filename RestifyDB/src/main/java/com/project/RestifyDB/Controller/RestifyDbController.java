package com.project.RestifyDB.Controller;

import com.project.RestifyDB.Service.DataRetrievalService;
import com.project.RestifyDB.Service.ScanTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;

@RestController
public class RestifyDbController {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Autowired
    private ScanTables getTables;

    @Autowired
    private DataRetrievalService dataRetrievalService;


    @PostConstruct
    public void init() throws Exception {

        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());
        ResultSet tables = getTables.getTables();
        while (tables.next()) {
            ResultSet columns = getTables.getColumns(tables.getString(1));
            while (columns.next()) {
                handlerMapping.registerMapping(
                        RequestMappingInfo.paths("/rest/tables/" + tables.getString(1) + "/" + columns.getString(1).toLowerCase() + "/{value}")
                                .methods(RequestMethod.GET)
                                .options(options)
                                .build(), this, RestifyDbController.class.getMethod("restifyDbHandler",
                                HttpServletRequest.class, String.class));
            }
        }
    }


    public ResponseEntity<Object> restifyDbHandler(HttpServletRequest httpServletRequest,
                                                   @PathVariable String value) throws Exception {
        return new ResponseEntity<>(dataRetrievalService.getRow(httpServletRequest.getServletPath(), value).toList(), HttpStatus.OK);
    }


}