
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

public class mfmi_javaClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public mfmi_javaClient() {
        create0();
        Endpoint mfmi_javaSoapLocalEndpointEP = service0 .addEndpoint(new QName("http://tempuri.org/", "mfmi_javaSoapLocalEndpoint"), new QName("http://tempuri.org/", "mfmi_javaSoapLocalBinding"), "xfire.local://mfmi_java");
        endpoints.put(new QName("http://tempuri.org/", "mfmi_javaSoapLocalEndpoint"), mfmi_javaSoapLocalEndpointEP);
        Endpoint mfmi_javaSoapEP = service0 .addEndpoint(new QName("http://tempuri.org/", "mfmi_javaSoap"), new QName("http://tempuri.org/", "mfmi_javaSoap"), "http://c8mff.m2.magic2008.cn/manage/mfmi_java.asmx");
        endpoints.put(new QName("http://tempuri.org/", "mfmi_javaSoap"), mfmi_javaSoapEP);
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
        service0 = asf.create((org.tempuri.mfmi_javaSoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "mfmi_javaSoapLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://tempuri.org/", "mfmi_javaSoap"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public mfmi_javaSoap getmfmi_javaSoapLocalEndpoint() {
        return ((mfmi_javaSoap)(this).getEndpoint(new QName("http://tempuri.org/", "mfmi_javaSoapLocalEndpoint")));
    }

    public mfmi_javaSoap getmfmi_javaSoapLocalEndpoint(String url) {
        mfmi_javaSoap var = getmfmi_javaSoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public mfmi_javaSoap getmfmi_javaSoap() {
        return ((mfmi_javaSoap)(this).getEndpoint(new QName("http://tempuri.org/", "mfmi_javaSoap")));
    }

    public mfmi_javaSoap getmfmi_javaSoap(String url) {
        mfmi_javaSoap var = getmfmi_javaSoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
