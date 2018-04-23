
package cn.dns4.api.oa;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetUserMagicMasterWebIDResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getUserMagicMasterWebIDResult"
})
@XmlRootElement(name = "GetUserMagicMasterWebIDResponse")
public class GetUserMagicMasterWebIDResponse {

    @XmlElement(name = "GetUserMagicMasterWebIDResult")
    protected String getUserMagicMasterWebIDResult;

    /**
     * Gets the value of the getUserMagicMasterWebIDResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetUserMagicMasterWebIDResult() {
        return getUserMagicMasterWebIDResult;
    }

    /**
     * Sets the value of the getUserMagicMasterWebIDResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetUserMagicMasterWebIDResult(String value) {
        this.getUserMagicMasterWebIDResult = value;
    }

}
