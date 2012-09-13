/**
 * Copyright (C) 2012 KRM Associates, Inc. healtheme@krminc.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.krminc.phr.api.service.clinical;

import com.krminc.phr.api.converter.clinical.CcrVitalSignTestsConverter;
import com.krminc.phr.api.service.Api;
import com.krminc.phr.core.VitalConfig;
import com.krminc.phr.dao.PersistenceService;
import com.krminc.phr.domain.clinical.CcrVitalSignTest;
import java.util.Collection;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.sun.jersey.api.core.ResourceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.EntityManager;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cmccall
 */

public class CcrVitalSignTestsResource {
    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;

    protected Long healthRecordId;

    protected String vitalType;

    final Logger logger = LoggerFactory.getLogger(CcrVitalSignResultsResource.class);
  
    /** Creates a new instance of CcrVitalSignTestsResource */
    public CcrVitalSignTestsResource() {
    }
    
    public void setHealthRecordId(Long healthRecordId) {
        this.healthRecordId = healthRecordId;
    }

    /**
     * Setter method for limiting Vital Sign results to a specific vital type -- forced to UPPERCASE
     */
    public void setVitalType(String vitalType) {
        this.vitalType = vitalType.toUpperCase();
    }

    /**
     * Get method for retrieving a collection of CcrVitalSignTest instance in XML format.
     *
     * @return an instance of CcrVitalSignTestsConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public CcrVitalSignTestsConverter get(
        @QueryParam("start")
        @DefaultValue("0")
        int start,
        @QueryParam("max")
        @DefaultValue("10")
        int max,
        @QueryParam("orderBy")
        @DefaultValue("dateadded")
        String orderBy,
        @QueryParam("desc")
        @DefaultValue("1")
        int desc
    ) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new CcrVitalSignTestsConverter(getEntities(start, max, orderBy, desc), uriInfo.getAbsolutePath(), Api.DEFAULT_EXPAND_LEVEL);
        } finally {
            persistenceSvc.commitTx();
            persistenceSvc.close();
        }
    }

    @Path("count/")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public JSONObject getCount() {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        Long result = 0L;
        StringBuilder query = new StringBuilder("CcrVitalSignTest.");

        if ((vitalType != null) && (!vitalType.isEmpty()) && (VitalConfig.isValidVitalType(vitalType))) {
            query.append("countByHealthRecordIdAndVitalType");
            result = (Long) em.createNamedQuery(query.toString())
                    .setParameter("healthRecordId" , this.healthRecordId)
                    .setParameter("vitalType", vitalType)
                    .getSingleResult();

        //invalid vitalType is being queried -- pull non-conforming vitals count
        } else if ((vitalType != null) && (!vitalType.isEmpty()) && (!VitalConfig.isValidVitalType(vitalType))) {

            ArrayList<String> vitals = VitalConfig.getVitalTypes();
            ListIterator vitalsIterator = vitals.listIterator();
            query = new StringBuilder("SELECT COUNT(x) FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId");

            while (vitalsIterator.hasNext()){
                query.append(" AND x.descriptionText <> '").append(vitalsIterator.next().toString()).append("'");
            }

            result = (Long) em.createQuery(query.toString())
                .setParameter("healthRecordId", healthRecordId)
                .getSingleResult();

        //no vitalType -- pull count of all vitals
        } else {
            query.append("countByHealthRecordId");
            result = (Long) em.createNamedQuery(query.toString())
                    .setParameter("healthRecordId" , this.healthRecordId)
                    .getSingleResult();
        }
        JSONObject jsonResult = new JSONObject();
        try {
            jsonResult.put("count", result);
        }
        catch (JSONException ex) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return jsonResult;
    }

    /**
     * Returns a dynamic instance of CcrVitalSignTestResource used for entity navigation.
     *
     * @return an instance of CcrVitalSignTestResource
     */
    @Path("{id}/")
    public CcrVitalSignTestResource getCcrVitalSignTestResource(@PathParam("id")
    Long id) {
        CcrVitalSignTestResource resource = resourceContext.getResource(CcrVitalSignTestResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of CcrVitalSignTest instances
     */
    protected Collection<CcrVitalSignTest> getEntities(int start, int max, String orderBy, int desc) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        //  orderBy :
        //    severity
        //    dateadded
        //    description
        //    observeddate
        //  desc:
        //    0 -- ascending (default)
        //    >0 -- descending
        if(start<0) start=0;
        if(max<1) max=1;
        if(max>20) max=20;
        StringBuilder query;

        //logger.info("vitalType: {}", vitalType);


        if ((vitalType != null) && (!vitalType.isEmpty()) && (VitalConfig.isValidVitalType(vitalType))) {
            //valid vitalType is being queried
            Boolean wasOrdered = false;

            if(orderBy.equalsIgnoreCase("dateadded")){
                query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType ORDER BY c.addedDateTime");
                wasOrdered = true;
            } else if(orderBy.equalsIgnoreCase("observeddate")){
                
                //query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrResult r LEFT JOIN r.dateTime d LEFT JOIN x.dateTime y JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType ORDER BY y.exactDateTime, d.exactDateTime");
                //wasOrdered = true;

                query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType");
                List<CcrVitalSignTest> entities = em.createQuery(query.toString())
                    .setParameter("healthRecordId", healthRecordId)
                    .setParameter("vitalType", vitalType)
                    .setFirstResult(start)
                    .setMaxResults(max)
                    .getResultList();
                
                Collections.sort(entities, new Comparator() {
                  public int compare (Object o1, Object o2) {
                    return ((CcrVitalSignTest)o1).getExactDateTime().compareTo(((CcrVitalSignTest)o2).getExactDateTime());
                  }
                });
                if (desc>0) {
                    Collections.reverse(entities);
                }
                return entities;

            } else if(orderBy.equalsIgnoreCase("value")){
                query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType ORDER BY x.testResultValue");
                wasOrdered = true;
            } else if(orderBy.equalsIgnoreCase("description")){
                query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType ORDER BY x.descriptionText");
                wasOrdered = true;
            } else {
                query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId AND x.descriptionText = :vitalType");
            }

            if (wasOrdered) {
                if (desc>0) {
                    query.append(" DESC");
                } else {
                    query.append(" ASC");
                }
            }

            //logger.info(query.toString());

            return em.createQuery(query.toString())
                .setParameter("healthRecordId", healthRecordId)
                .setParameter("vitalType", vitalType)
                .setFirstResult(start)
                .setMaxResults(max)
                .getResultList();
        } else if ((vitalType != null) && (!vitalType.isEmpty()) && (!VitalConfig.isValidVitalType(vitalType))) {
            //invalid vitalType is being queried -- pull non-conforming vitals
            ArrayList<String> vitals = VitalConfig.getVitalTypes();
            ListIterator vitalsIterator = vitals.listIterator();
            query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId");

            while (vitalsIterator.hasNext()){
                query.append(" AND x.descriptionText <> '").append(vitalsIterator.next().toString()).append("'");
            }

            //generic bucket sorting
            Boolean wasOrdered = false;

            if(orderBy.equalsIgnoreCase("dateadded")){
                query.append(" ORDER BY c.addedDateTime");
                wasOrdered = true;
            } else if(orderBy.equalsIgnoreCase("observeddate")){

                List<CcrVitalSignTest> entities = em.createQuery(query.toString())
                    .setParameter("healthRecordId", healthRecordId)
                    .setFirstResult(start)
                    .setMaxResults(max)
                    .getResultList();

                Collections.sort(entities, new Comparator() {
                  public int compare (Object o1, Object o2) {
                    return ((CcrVitalSignTest)o1).getExactDateTime().compareTo(((CcrVitalSignTest)o2).getExactDateTime());
                  }
                });
                if (desc>0) {
                    Collections.reverse(entities);
                }
                return entities;

            } else if(orderBy.equalsIgnoreCase("value")){
                query.append(" ORDER BY x.testResultValue");
                wasOrdered = true;
            } else if(orderBy.equalsIgnoreCase("description")){
                query.append(" ORDER BY x.descriptionText");
                wasOrdered = true;
            }

            if (wasOrdered) {
                if (desc>0) {
                    query.append(" DESC");
                } else {
                    query.append(" ASC");
                }
            }


        } else {
            //no vital type is being queried -- pull all vitals
            query = new StringBuilder("SELECT x FROM CcrVitalSignTest x JOIN x.ccrDocument c JOIN c.healthRecords h WHERE h.id = :healthRecordId");
        }

        //logger.info(query.toString());

        return em.createQuery(query.toString())
            .setParameter("healthRecordId", healthRecordId)
            .setFirstResult(start)
            .setMaxResults(max)
            .getResultList();
    }
}
