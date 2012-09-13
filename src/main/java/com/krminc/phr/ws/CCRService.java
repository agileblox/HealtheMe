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
package com.krminc.phr.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "CCRService", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://192.168.200.189/ccrService/CCRService.asmx?wsdl")
public class CCRService
    extends Service
{

    private final static URL CCRSERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.krminc.phr.ws.CCRService.class.getName());

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = com.krminc.phr.ws.CCRService.class.getResource(".");
            url = new URL(baseUrl, "http://192.168.200.189/ccrService/CCRService.asmx?wsdl");
        } catch (MalformedURLException e) {
            logger.warning("Failed to create URL for the wsdl Location: 'http://192.168.200.189/ccrService/CCRService.asmx?wsdl', retrying as a local file");
            logger.warning(e.getMessage());
        }
        CCRSERVICE_WSDL_LOCATION = url;
    }

    public CCRService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CCRService() {
        super(CCRSERVICE_WSDL_LOCATION, new QName("http://tempuri.org/", "CCRService"));
    }

    /**
     * 
     * @return
     *     returns CCRServiceSoap
     */
    @WebEndpoint(name = "CCRServiceSoap")
    public CCRServiceSoap getCCRServiceSoap() {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceSoap"), CCRServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CCRServiceSoap
     */
    @WebEndpoint(name = "CCRServiceSoap")
    public CCRServiceSoap getCCRServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceSoap"), CCRServiceSoap.class, features);
    }

    /**
     * 
     * @return
     *     returns CCRServiceSoap12
     */
    @WebEndpoint(name = "CCRServiceSoap12")
    public CCRServiceSoap12 getCCRServiceSoap12() {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceSoap12"), CCRServiceSoap12.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CCRServiceSoap12
     */
    @WebEndpoint(name = "CCRServiceSoap12")
    public CCRServiceSoap12 getCCRServiceSoap12(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceSoap12"), CCRServiceSoap12.class, features);
    }

    /**
     * 
     * @return
     *     returns CCRServiceHttpGet
     */
    @WebEndpoint(name = "CCRServiceHttpGet")
    public CCRServiceHttpGet getCCRServiceHttpGet() {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceHttpGet"), CCRServiceHttpGet.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CCRServiceHttpGet
     */
    @WebEndpoint(name = "CCRServiceHttpGet")
    public CCRServiceHttpGet getCCRServiceHttpGet(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceHttpGet"), CCRServiceHttpGet.class, features);
    }

    /**
     * 
     * @return
     *     returns CCRServiceHttpPost
     */
    @WebEndpoint(name = "CCRServiceHttpPost")
    public CCRServiceHttpPost getCCRServiceHttpPost() {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceHttpPost"), CCRServiceHttpPost.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CCRServiceHttpPost
     */
    @WebEndpoint(name = "CCRServiceHttpPost")
    public CCRServiceHttpPost getCCRServiceHttpPost(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "CCRServiceHttpPost"), CCRServiceHttpPost.class, features);
    }

}
