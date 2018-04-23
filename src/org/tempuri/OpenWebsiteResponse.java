
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
 *         &lt;element name="OpenWebsiteResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ManagePath" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "openWebsiteResult",
    "managePath"
})
@XmlRootElement(name = "OpenWebsiteResponse")
public class OpenWebsiteResponse {

    @XmlElement(name = "OpenWebsiteResult")
    protected String openWebsiteResult;
    @XmlElement(name = "ManagePath")
    protected String managePath;

    /**
     * Gets the value of the openWebsiteResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpenWebsiteResult() {
        return openWebsiteResult;
    }

    /**
     * Sets the value of the openWebsiteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpenWebsiteResult(String value) {
        this.openWebsiteResult = value;
    }

    /**
     * Gets the value of the managePath property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagePath() {
        return managePath;
    }

    /**
     * Sets the value of the managePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagePath(String value) {
        this.managePath = value;
    }

}
