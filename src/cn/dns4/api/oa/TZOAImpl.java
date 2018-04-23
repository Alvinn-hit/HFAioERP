
package cn.dns4.api.oa;

import javax.jws.WebService;

@WebService(serviceName = "TZOA", targetNamespace = "http://oa.api.dns4.cn/", endpointInterface = "cn.dns4.api.oa.TZOASoap")
public class TZOAImpl
    implements TZOASoap
{


    public String getUserMagicMasterWebID(String Token, String loginName) {
        throw new UnsupportedOperationException();
    }

    public String disablePackage(String Token, String loginName, String agentName) {
        throw new UnsupportedOperationException();
    }

    public String renewPackage(String Token, String loginName, int pacakgeID, String packageStartDate, String packageEndDate, String agentName) {
        throw new UnsupportedOperationException();
    }

    public String getUserInfo(String Token, String loginName) {
        throw new UnsupportedOperationException();
    }

    public String setFunctionalModule(String Token, String loginName, String fucationID, int state, String packageStartDate, String packageEndDate, String agentName, String attributeJson) {
        throw new UnsupportedOperationException();
    }

    public String buyPackage(String Token, String loginName, int pacakgeID, String packageStartDate, String packageEndDate, String agentName) {
        throw new UnsupportedOperationException();
    }

}
