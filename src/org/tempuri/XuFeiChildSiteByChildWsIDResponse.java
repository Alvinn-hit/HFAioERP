
package org.tempuri;

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
 *         &lt;element name="XuFeiChildSiteByChildWsIDResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "xuFeiChildSiteByChildWsIDResult"
})
@XmlRootElement(name = "XuFeiChildSiteByChildWsIDResponse")
public class XuFeiChildSiteByChildWsIDResponse {

    @XmlElement(name = "XuFeiChildSiteByChildWsIDResult")
    protected String xuFeiChildSiteByChildWsIDResult;

    /**
     * Gets the value of the xuFeiChildSiteByChildWsIDResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXuFeiChildSiteByChildWsIDResult() {
        return xuFeiChildSiteByChildWsIDResult;
    }

    /**
     * Sets the value of the xuFeiChildSiteByChildWsIDResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXuFeiChildSiteByChildWsIDResult(String value) {
        this.xuFeiChildSiteByChildWsIDResult = value;
    }

}
