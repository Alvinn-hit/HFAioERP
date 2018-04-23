
package cn.dns4.api.oa;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TZOASoap", targetNamespace = "http://oa.api.dns4.cn/")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TZOASoap {


    @WebMethod(operationName = "GetUserMagicMasterWebID", action = "http://oa.api.dns4.cn/GetUserMagicMasterWebID")
    @WebResult(name = "GetUserMagicMasterWebIDResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String getUserMagicMasterWebID(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName);

    @WebMethod(operationName = "DisablePackage", action = "http://oa.api.dns4.cn/DisablePackage")
    @WebResult(name = "DisablePackageResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String disablePackage(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName,
        @WebParam(name = "agentName", targetNamespace = "http://oa.api.dns4.cn/")
        String agentName);

    @WebMethod(operationName = "RenewPackage", action = "http://oa.api.dns4.cn/RenewPackage")
    @WebResult(name = "RenewPackageResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String renewPackage(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName,
        @WebParam(name = "pacakgeID", targetNamespace = "http://oa.api.dns4.cn/")
        int pacakgeID,
        @WebParam(name = "packageStartDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageStartDate,
        @WebParam(name = "packageEndDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageEndDate,
        @WebParam(name = "agentName", targetNamespace = "http://oa.api.dns4.cn/")
        String agentName);

    @WebMethod(operationName = "GetUserInfo", action = "http://oa.api.dns4.cn/GetUserInfo")
    @WebResult(name = "GetUserInfoResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String getUserInfo(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName);

    @WebMethod(operationName = "SetFunctionalModule", action = "http://oa.api.dns4.cn/SetFunctionalModule")
    @WebResult(name = "SetFunctionalModuleResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String setFunctionalModule(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName,
        @WebParam(name = "fucationID", targetNamespace = "http://oa.api.dns4.cn/")
        String fucationID,
        @WebParam(name = "state", targetNamespace = "http://oa.api.dns4.cn/")
        int state,
        @WebParam(name = "packageStartDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageStartDate,
        @WebParam(name = "packageEndDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageEndDate,
        @WebParam(name = "agentName", targetNamespace = "http://oa.api.dns4.cn/")
        String agentName,
        @WebParam(name = "attributeJson", targetNamespace = "http://oa.api.dns4.cn/")
        String attributeJson);

    @WebMethod(operationName = "BuyPackage", action = "http://oa.api.dns4.cn/BuyPackage")
    @WebResult(name = "BuyPackageResult", targetNamespace = "http://oa.api.dns4.cn/")
    public String buyPackage(
        @WebParam(name = "Token", targetNamespace = "http://oa.api.dns4.cn/")
        String Token,
        @WebParam(name = "loginName", targetNamespace = "http://oa.api.dns4.cn/")
        String loginName,
        @WebParam(name = "pacakgeID", targetNamespace = "http://oa.api.dns4.cn/")
        int pacakgeID,
        @WebParam(name = "packageStartDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageStartDate,
        @WebParam(name = "packageEndDate", targetNamespace = "http://oa.api.dns4.cn/")
        String packageEndDate,
        @WebParam(name = "agentName", targetNamespace = "http://oa.api.dns4.cn/")
        String agentName);

}
