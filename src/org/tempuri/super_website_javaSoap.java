
package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

@WebService(name = "super_website_javaSoap", targetNamespace = "http://tempuri.org/")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface super_website_javaSoap {


    @WebMethod(operationName = "GetMenuState", action = "http://tempuri.org/GetMenuState")
    @WebResult(name = "GetMenuStateResult", targetNamespace = "http://tempuri.org/")
    public String getMenuState(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID);

    @WebMethod(operationName = "GetSuperWebsiteFirstDomain", action = "http://tempuri.org/GetSuperWebsiteFirstDomain")
    @WebResult(name = "GetSuperWebsiteFirstDomainResult", targetNamespace = "http://tempuri.org/")
    public String getSuperWebsiteFirstDomain(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mainWsID", targetNamespace = "http://tempuri.org/")
        int mainWsID);

    @WebMethod(operationName = "PauseAllChildSite", action = "http://tempuri.org/PauseAllChildSite")
    @WebResult(name = "PauseAllChildSiteResult", targetNamespace = "http://tempuri.org/")
    public String pauseAllChildSite(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mWsID", targetNamespace = "http://tempuri.org/")
        int mWsID);

    @WebMethod(operationName = "GetSuperWebsiteListByLastModifyTime", action = "http://tempuri.org/GetSuperWebsiteListByLastModifyTime")
    @WebResult(name = "GetSuperWebsiteListByLastModifyTimeResult", targetNamespace = "http://tempuri.org/")
    public String getSuperWebsiteListByLastModifyTime(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "lastModifyTime", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar lastModifyTime);

    @WebMethod(operationName = "XuFeiChildSiteByMonth", action = "http://tempuri.org/XuFeiChildSiteByMonth")
    @WebResult(name = "XuFeiChildSiteByMonthResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiChildSiteByMonth(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mWsID", targetNamespace = "http://tempuri.org/")
        int mWsID,
        @WebParam(name = "siteIndex", targetNamespace = "http://tempuri.org/")
        int siteIndex,
        @WebParam(name = "month", targetNamespace = "http://tempuri.org/")
        int month);

    @WebMethod(operationName = "XuFeiChildSiteByChildWsID", action = "http://tempuri.org/XuFeiChildSiteByChildWsID")
    @WebResult(name = "XuFeiChildSiteByChildWsIDResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiChildSiteByChildWsID(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "childWsID", targetNamespace = "http://tempuri.org/")
        int childWsID,
        @WebParam(name = "eDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar eDate);

    @WebMethod(operationName = "GetSuperWebsiteGroup", action = "http://tempuri.org/GetSuperWebsiteGroup")
    @WebResult(name = "GetSuperWebsiteGroupResult", targetNamespace = "http://tempuri.org/")
    public String getSuperWebsiteGroup(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID);

    @WebMethod(operationName = "GetRandomBuySellIDs", action = "http://tempuri.org/GetRandomBuySellIDs")
    @WebResult(name = "GetRandomBuySellIDsResult", targetNamespace = "http://tempuri.org/")
    public String getRandomBuySellIDs(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "record", targetNamespace = "http://tempuri.org/")
        int record);

    @WebMethod(operationName = "ModifyMenuState", action = "http://tempuri.org/ModifyMenuState")
    @WebResult(name = "ModifyMenuStateResult", targetNamespace = "http://tempuri.org/")
    public String modifyMenuState(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID,
        @WebParam(name = "menuType", targetNamespace = "http://tempuri.org/")
        int menuType,
        @WebParam(name = "isShow", targetNamespace = "http://tempuri.org/")
        boolean isShow);

    @WebMethod(operationName = "GetAllMethodDescription", action = "http://tempuri.org/GetAllMethodDescription")
    @WebResult(name = "GetAllMethodDescriptionResult", targetNamespace = "http://tempuri.org/")
    public String getAllMethodDescription(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key);

    @WebMethod(operationName = "OpenWebsite", action = "http://tempuri.org/OpenWebsite")
    @WebResult(name = "OpenWebsiteResult", targetNamespace = "http://tempuri.org/")
    public String openWebsite(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
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
        @WebParam(name = "ManagePath", targetNamespace = "http://tempuri.org/", mode = WebParam.Mode.OUT)
        Holder<String> ManagePath);

    @WebMethod(operationName = "GetAllSuperWebsite", action = "http://tempuri.org/GetAllSuperWebsite")
    @WebResult(name = "GetAllSuperWebsiteResult", targetNamespace = "http://tempuri.org/")
    public String getAllSuperWebsite(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key);

    @WebMethod(operationName = "GetFirstDomain_PrecedenceMobile", action = "http://tempuri.org/GetFirstDomain_PrecedenceMobile")
    @WebResult(name = "GetFirstDomain_PrecedenceMobileResult", targetNamespace = "http://tempuri.org/")
    public String getFirstDomain_PrecedenceMobile(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mainWsID", targetNamespace = "http://tempuri.org/")
        int mainWsID);

    @WebMethod(operationName = "GetWebsiteDomain", action = "http://tempuri.org/GetWebsiteDomain")
    @WebResult(name = "GetWebsiteDomainResult", targetNamespace = "http://tempuri.org/")
    public String getWebsiteDomain(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "wsID", targetNamespace = "http://tempuri.org/")
        int wsID);

    @WebMethod(operationName = "GetFirstDomain_PrecedencePC", action = "http://tempuri.org/GetFirstDomain_PrecedencePC")
    @WebResult(name = "GetFirstDomain_PrecedencePCResult", targetNamespace = "http://tempuri.org/")
    public String getFirstDomain_PrecedencePC(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mainWsID", targetNamespace = "http://tempuri.org/")
        int mainWsID);

    @WebMethod(operationName = "XuFeiChildSiteByIndex", action = "http://tempuri.org/XuFeiChildSiteByIndex")
    @WebResult(name = "XuFeiChildSiteByIndexResult", targetNamespace = "http://tempuri.org/")
    public String xuFeiChildSiteByIndex(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mWsID", targetNamespace = "http://tempuri.org/")
        int mWsID,
        @WebParam(name = "siteIndex", targetNamespace = "http://tempuri.org/")
        int siteIndex,
        @WebParam(name = "eDate", targetNamespace = "http://tempuri.org/")
        XMLGregorianCalendar eDate);

    @WebMethod(operationName = "ResetAllChildSite", action = "http://tempuri.org/ResetAllChildSite")
    @WebResult(name = "ResetAllChildSiteResult", targetNamespace = "http://tempuri.org/")
    public String resetAllChildSite(
        @WebParam(name = "_key", targetNamespace = "http://tempuri.org/")
        String _key,
        @WebParam(name = "mWsID", targetNamespace = "http://tempuri.org/")
        int mWsID);

}
