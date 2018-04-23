
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
 *         &lt;element name="NewSite2_XMLResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "newSite2XMLResult"
})
@XmlRootElement(name = "NewSite2_XMLResponse")
public class NewSite2XMLResponse {

    @XmlElement(name = "NewSite2_XMLResult")
    protected String newSite2XMLResult;

    /**
     * Gets the value of the newSite2XMLResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewSite2XMLResult() {
        return newSite2XMLResult;
    }

    /**
     * Sets the value of the newSite2XMLResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewSite2XMLResult(String value) {
        this.newSite2XMLResult = value;
    }

}
