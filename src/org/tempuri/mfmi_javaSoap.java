
package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.datatype.XMLGregorianCalendar;

@WebService(name = "mfmi_javaSoap", targetNamespace = "http://tempuri.org/")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface mfmi_javaSoap {


    @WebMethod(operationName = "GetYuBeiAnDomainList_XML", action = "http://tempuri.org/GetYuBeiAnDomainList_XML")
    @WebResult(name = "GetYuBeiAnDomainList_XMLResult", targetNamespace = "http://tempuri.org/")
    public String getYuBeiAnDomainList_XML(
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "OpenWebSite_XML", action = "http://tempuri.org/OpenWebSite_XML")
    @WebResult(name = "OpenWebSite_XMLResult", targetNamespace = "http://tempuri.org/")
    public String openWebSite_XML(
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "OpenMobileWechatApp", action = "http://tempuri.org/OpenMobileWechatApp")
    @WebResult(name = "OpenMobileWechatAppResult", targetNamespace = "http://tempuri.org/")
    public String openMobileWechatApp(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "CloseApp", action = "http://tempuri.org/CloseApp")
    @WebResult(name = "CloseAppResult", targetNamespace = "http://tempuri.org/")
    public String closeApp(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "GetWebsiteServerLine", action = "http://tempuri.org/GetWebsiteServerLine")
    @WebResult(name = "GetWebsiteServerLineResult", targetNamespace = "http://tempuri.org/")
    public String getWebsiteServerLine(
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiSingleSuperWebSite", action = "http://tempuri.org/XuFeiSingleSuperWebSite")
    @WebResult(name = "XuFeiSingleSuperWebSiteResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiSingleSuperWebSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "eDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar eDate,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "GetSetMealName", action = "http://tempuri.org/GetSetMealName")
    @WebResult(name = "GetSetMealNameResult", targetNamespace = "http://tempuri.org/")
    public String getSetMealName();

    @WebMethod(operationName = "ChangeSetMeal", action = "http://tempuri.org/ChangeSetMeal")
    @WebResult(name = "ChangeSetMealResult", targetNamespace = "http://tempuri.org/")
    public String changeSetMeal(
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed,
        @WebParam(name = "mbType", targetNamespace = "http://tempuri.org/")
        int mbType,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "bsID", targetNamespace = "http://tempuri.org/")
        String bsID);

    @WebMethod(operationName = "XuFeiApp", action = "http://tempuri.org/XuFeiApp")
    @WebResult(name = "XuFeiAppResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiApp(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "Auto_Open_XuFei_App", action = "http://tempuri.org/Auto_Open_XuFei_App")
    @WebResult(name = "Auto_Open_XuFei_AppResult", targetNamespace = "http://tempuri.org/")
    public String auto_Open_XuFei_App(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "ResetPassWord", action = "http://tempuri.org/ResetPassWord")
    @WebResult(name = "ResetPassWordResult", targetNamespace = "http://tempuri.org/")
    public String resetPassWord(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "NewPassword", targetNamespace = "http://tempuri.org/")
        String NewPassword,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "ChangeSiteServer", action = "http://tempuri.org/ChangeSiteServer")
    @WebResult(name = "ChangeSiteServerResult", targetNamespace = "http://tempuri.org/")
    public String changeSiteServer(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "ServerName", targetNamespace = "http://tempuri.org/")
        String ServerName,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiMobileSite", action = "http://tempuri.org/XuFeiMobileSite")
    @WebResult(name = "XuFeiMobileSiteResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiMobileSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "CloseMobileSite", action = "http://tempuri.org/CloseMobileSite")
    @WebResult(name = "CloseMobileSiteResult", targetNamespace = "http://tempuri.org/")
    public String closeMobileSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "Auto_Open_XuFei_MobileSite", action = "http://tempuri.org/Auto_Open_XuFei_MobileSite")
    @WebResult(name = "Auto_Open_XuFei_MobileSiteResult", targetNamespace = "http://tempuri.org/")
    public String auto_Open_XuFei_MobileSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "CloseWechatSite", action = "http://tempuri.org/CloseWechatSite")
    @WebResult(name = "CloseWechatSiteResult", targetNamespace = "http://tempuri.org/")
    public String closeWechatSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "OpenApp", action = "http://tempuri.org/OpenApp")
    @WebResult(name = "OpenAppResult", targetNamespace = "http://tempuri.org/")
    public String openApp(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiSuperWebsite", action = "http://tempuri.org/XuFeiSuperWebsite")
    @WebResult(name = "XuFeiSuperWebsiteResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiSuperWebsite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiWechatSite", action = "http://tempuri.org/XuFeiWechatSite")
    @WebResult(name = "XuFeiWechatSiteResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiWechatSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiMobileWechatApp", action = "http://tempuri.org/XuFeiMobileWechatApp")
    @WebResult(name = "XuFeiMobileWechatAppResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiMobileWechatApp(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "ReNewSite", action = "http://tempuri.org/ReNewSite")
    @WebResult(name = "ReNewSiteResult", targetNamespace = "http://tempuri.org/")
    public String reNewSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "eDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar eDate,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "ChangeSiteServer2", action = "http://tempuri.org/ChangeSiteServer2")
    @WebResult(name = "ChangeSiteServer2Result", targetNamespace = "http://tempuri.org/")
    public String changeSiteServer2(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "ServerName", targetNamespace = "http://tempuri.org/")
        String ServerName,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "GetResetPassWord", action = "http://tempuri.org/GetResetPassWord")
    @WebResult(name = "GetResetPassWordResult", targetNamespace = "http://tempuri.org/")
    public String getResetPassWord(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "NewPassword", targetNamespace = "http://tempuri.org/")
        String NewPassword,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "OpenMobileSite", action = "http://tempuri.org/OpenMobileSite")
    @WebResult(name = "OpenMobileSiteResult", targetNamespace = "http://tempuri.org/")
    public String openMobileSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "OpenWechatSite", action = "http://tempuri.org/OpenWechatSite")
    @WebResult(name = "OpenWechatSiteResult", targetNamespace = "http://tempuri.org/")
    public String openWechatSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "GetModelList_XML", action = "http://tempuri.org/GetModelList_XML")
    @WebResult(name = "GetModelList_XMLResult", targetNamespace = "http://tempuri.org/")
    public String getModelList_XML(
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "CloseSuperWebsite", action = "http://tempuri.org/CloseSuperWebsite")
    @WebResult(name = "CloseSuperWebsiteResult", targetNamespace = "http://tempuri.org/")
    public String closeSuperWebsite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "NewSite2_XML", action = "http://tempuri.org/NewSite2_XML")
    @WebResult(name = "NewSite2_XMLResult", targetNamespace = "http://tempuri.org/")
    public String newSite2_XML(
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "Auto_Open_XuFei_WechatSite", action = "http://tempuri.org/Auto_Open_XuFei_WechatSite")
    @WebResult(name = "Auto_Open_XuFei_WechatSiteResult", targetNamespace = "http://tempuri.org/")
    public String auto_Open_XuFei_WechatSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "CPName", targetNamespace = "http://tempuri.org/")
        String CPName,
        @WebParam(name = "Domain", targetNamespace = "http://tempuri.org/")
        String Domain,
        @WebParam(name = "Model", targetNamespace = "http://tempuri.org/")
        int Model,
        @WebParam(name = "Kind", targetNamespace = "http://tempuri.org/")
        int Kind,
        @WebParam(name = "SiteServer", targetNamespace = "http://tempuri.org/")
        String SiteServer,
        @WebParam(name = "EndDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar EndDate,
        @WebParam(name = "AgtName", targetNamespace = "http://tempuri.org/")
        String AgtName,
        @WebParam(name = "OpUsID", targetNamespace = "http://tempuri.org/")
        int OpUsID,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

    @WebMethod(operationName = "XuFeiWebSite", action = "http://tempuri.org/XuFeiWebSite")
    @WebResult(name = "XuFeiWebSiteResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiWebSite(
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "eDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar eDate,
        @WebParam(name = "Signed", targetNamespace = "http://tempuri.org/")
        String Signed);

}
