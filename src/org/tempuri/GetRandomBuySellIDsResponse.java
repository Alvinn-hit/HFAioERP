
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
 *         &lt;element name="GetRandomBuySellIDsResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getRandomBuySellIDsResult"
})
@XmlRootElement(name = "GetRandomBuySellIDsResponse")
public class GetRandomBuySellIDsResponse {

    @XmlElement(name = "GetRandomBuySellIDsResult")
    protected String getRandomBuySellIDsResult;

    /**
     * Gets the value of the getRandomBuySellIDsResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetRandomBuySellIDsResult() {
        return getRandomBuySellIDsResult;
    }

    /**
     * Sets the value of the getRandomBuySellIDsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetRandomBuySellIDsResult(String value) {
        this.getRandomBuySellIDsResult = value;
    }

}
