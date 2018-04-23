
package cn.dns4.api.oa;

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

public class TZOAClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public TZOAClient() {
        create0();
        Endpoint TZOASoapEP = service0 .addEndpoint(new QName("http://oa.api.dns4.cn/", "TZOASoap"), new QName("http://oa.api.dns4.cn/", "TZOASoap"), "http://oa.api.dns4.cn/TZOA.asmx");
        endpoints.put(new QName("http://oa.api.dns4.cn/", "TZOASoap"), TZOASoapEP);
        Endpoint TZOASoapLocalEndpointEP = service0 .addEndpoint(new QName("http://oa.api.dns4.cn/", "TZOASoapLocalEndpoint"), new QName("http://oa.api.dns4.cn/", "TZOASoapLocalBinding"), "xfire.local://TZOA");
        endpoints.put(new QName("http://oa.api.dns4.cn/", "TZOASoapLocalEndpoint"), TZOASoapLocalEndpointEP);
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
        service0 = asf.create((cn.dns4.api.oa.TZOASoap.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://oa.api.dns4.cn/", "TZOASoap"), "http://schemas.xmlsoap.org/soap/http");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://oa.api.dns4.cn/", "TZOASoapLocalBinding"), "urn:xfire:transport:local");
        }
    }

    public TZOASoap getTZOASoap() {
        return ((TZOASoap)(this).getEndpoint(new QName("http://oa.api.dns4.cn/", "TZOASoap")));
    }

    public TZOASoap getTZOASoap(String url) {
        TZOASoap var = getTZOASoap();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public TZOASoap getTZOASoapLocalEndpoint() {
        return ((TZOASoap)(this).getEndpoint(new QName("http://oa.api.dns4.cn/", "TZOASoapLocalEndpoint")));
    }

    public TZOASoap getTZOASoapLocalEndpoint(String url) {
        TZOASoap var = getTZOASoapLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

}
