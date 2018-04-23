
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
 *         &lt;element name="XuFeiChildSiteByIndexResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "xuFeiChildSiteByIndexResult"
})
@XmlRootElement(name = "XuFeiChildSiteByIndexResponse")
public class XuFeiChildSiteByIndexResponse {

    @XmlElement(name = "XuFeiChildSiteByIndexResult")
    protected String xuFeiChildSiteByIndexResult;

    /**
     * Gets the value of the xuFeiChildSiteByIndexResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXuFeiChildSiteByIndexResult() {
        return xuFeiChildSiteByIndexResult;
    }

    /**
     * Sets the value of the xuFeiChildSiteByIndexResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXuFeiChildSiteByIndexResult(String value) {
        this.xuFeiChildSiteByIndexResult = value;
    }

}
