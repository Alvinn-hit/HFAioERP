
package org.tempuri;

import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

@WebService(serviceName = "super_website_java", targetNamespace = "http://tempuri.org/", endpointInterface = "org.tempuri.super_website_javaSoap")
public class super_website_javaImpl
    implements super_website_javaSoap
{


    public String getMenuState(String _key, int wsID) {
        throw new UnsupportedOperationException();
    }

    public String getSuperWebsiteFirstDomain(String _key, int mainWsID) {
        throw new UnsupportedOperationException();
    }

    public String pauseAllChildSite(String _key, int mWsID) {
        throw new UnsupportedOperationException();
    }

    public String getSuperWebsiteListByLastModifyTime(String _key, XMLGregorianCalendar lastModifyTime) {
        throw new UnsupportedOperationException();
    }

    public String xuFeiChildSiteByMonth(String _key, int mWsID, int siteIndex, int month) {
        throw new UnsupportedOperationException();
    }

    public String xuFeiChildSiteByChildWsID(String _key, int childWsID, XMLGregorianCalendar eDate) {
        throw new UnsupportedOperationException();
    }

    public String getSuperWebsiteGroup(String _key, int wsID) {
        throw new UnsupportedOperationException();
    }

    public String getRandomBuySellIDs(String _key, int wsID, int record) {
        throw new UnsupportedOperationException();
    }

    public String modifyMenuState(String _key, int wsID, int menuType, boolean isShow) {
        throw new UnsupportedOperationException();
    }

    public String getAllMethodDescription(String _key) {
        throw new UnsupportedOperationException();
    }

    public String openWebsite(String _key, String CPName, String Domain, int Model, int Kind, String SiteServer, XMLGregorianCalendar EndDate, String AgtName, int OpUsID, Holder<String> ManagePath) {
        throw new UnsupportedOperationException();
    }

    public String getAllSuperWebsite(String _key) {
        throw new UnsupportedOperationException();
    }

    public String getFirstDomain_PrecedenceMobile(String _key, int mainWsID) {
        throw new UnsupportedOperationException();
    }

    public String getWebsiteDomain(String _key, int wsID) {
        throw new UnsupportedOperationException();
    }

    public String getFirstDomain_PrecedencePC(String _key, int mainWsID) {
        throw new UnsupportedOperationException();
    }

    public String xuFeiChildSiteByIndex(String _key, int mWsID, int siteIndex, XMLGregorianCalendar eDate) {
        throw new UnsupportedOperationException();
    }

    public String resetAllChildSite(String _key, int mWsID) {
        throw new UnsupportedOperationException();
    }

}
