
package org.tempuri;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class super_website_javaClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public super_website_javaClient() {
        create0();
        Endpoint super_website_javaSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://tempuri.org/", "super_website_javaSoapLocalEndpoint"), new QName("http://tempuri.org/", "super_website_javaSoapLocalBinding"), "xfire.local://super_website_java");
        endpoints.put(new QName("http://tempuri.org/", "super_website_javaSoapLocalEndpoint"), super_website_javaSoapLocalEndpointEP);
        Endpoint super_website_javaSoapEP = service0 .addEndpoint(new QName("http://tempuri.org/", "super_website_javaSoap"), new QName("http://tempuri.org/", "super_website_javaSoap"), "http://c8mff.m2.magic2008.cn/manage/services/super_website_java.asmx");
        endpoints.put(new QName("http://tempuri.org/", "super_website_javaSoap"), super_website_javaSoapEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((org.tempuri.super_website_javaSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "super_website_javaSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "super_website_javaSoapLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public super_website_javaSoap getsuper_website_javaSoapLocalEndpoint() {
        return ((super_website_javaSoap)(this).getEndpoint(new QName("http://tempuri.org/", "super_website_javaSoapLocalEndpoint")));
    }

    public super_website_javaSoap getsuper_website_javaSoapLocalEndpoint(String url) {
        super_website_javaSoap var = getsuper_website_javaSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public super_website_javaSoap getsuper_website_javaSoap() {
        return ((super_website_javaSoap)(this).getEndpoint(new QName("http://tempuri.org/", "super_website_javaSoap")));
    }

    public super_website_javaSoap getsuper_website_javaSoap(String url) {
        super_website_javaSoap var = getsuper_website_javaSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
