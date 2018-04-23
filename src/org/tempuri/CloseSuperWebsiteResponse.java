
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
 *         &lt;element name="CloseSuperWebsiteResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "closeSuperWebsiteResult"
})
@XmlRootElement(name = "CloseSuperWebsiteResponse")
public class CloseSuperWebsiteResponse {

    @XmlElement(name = "CloseSuperWebsiteResult")
    protected String closeSuperWebsiteResult;

    /**
     * Gets the value of the closeSuperWebsiteResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloseSuperWebsiteResult() {
        return closeSuperWebsiteResult;
    }

    /**
     * Sets the value of the closeSuperWebsiteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloseSuperWebsiteResult(String value) {
        this.closeSuperWebsiteResult = value;
    }

}
