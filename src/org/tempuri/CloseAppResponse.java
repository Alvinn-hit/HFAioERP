
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
 *         &lt;element name="CloseAppResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "closeAppResult"
})
@XmlRootElement(name = "CloseAppResponse")
public class CloseAppResponse {

    @XmlElement(name = "CloseAppResult")
    protected String closeAppResult;

    /**
     * Gets the value of the closeAppResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCloseAppResult() {
        return closeAppResult;
    }

    /**
     * Sets the value of the closeAppResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCloseAppResult(String value) {
        this.closeAppResult = value;
    }

}
