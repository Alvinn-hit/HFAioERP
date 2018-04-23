
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
 *         &lt;element name="Auto_Open_XuFei_MobileSiteResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "autoOpenXuFeiMobileSiteResult"
})
@XmlRootElement(name = "Auto_Open_XuFei_MobileSiteResponse")
public class AutoOpenXuFeiMobileSiteResponse {

    @XmlElement(name = "Auto_Open_XuFei_MobileSiteResult")
    protected String autoOpenXuFeiMobileSiteResult;

    /**
     * Gets the value of the autoOpenXuFeiMobileSiteResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoOpenXuFeiMobileSiteResult() {
        return autoOpenXuFeiMobileSiteResult;
    }

    /**
     * Sets the value of the autoOpenXuFeiMobileSiteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoOpenXuFeiMobileSiteResult(String value) {
        this.autoOpenXuFeiMobileSiteResult = value;
    }

}
