
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
 *         &lt;element name="IsSetMealExpireResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="statusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "isSetMealExpireResult",
    "statusCode"
})
@XmlRootElement(name = "IsSetMealExpireResponse")
public class IsSetMealExpireResponse {

    @XmlElement(name = "IsSetMealExpireResult")
    protected boolean isSetMealExpireResult;
    protected String statusCode;

    /**
     * Gets the value of the isSetMealExpireResult property.
     * 
     */
    public boolean isIsSetMealExpireResult() {
        return isSetMealExpireResult;
    }

    /**
     * Sets the value of the isSetMealExpireResult property.
     * 
     */
    public void setIsSetMealExpireResult(boolean value) {
        this.isSetMealExpireResult = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusCode(String value) {
        this.statusCode = value;
    }

}
