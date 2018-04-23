
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
 *         &lt;element name="GetModelList_XMLResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getModelListXMLResult"
})
@XmlRootElement(name = "GetModelList_XMLResponse")
public class GetModelListXMLResponse {

    @XmlElement(name = "GetModelList_XMLResult")
    protected String getModelListXMLResult;

    /**
     * Gets the value of the getModelListXMLResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetModelListXMLResult() {
        return getModelListXMLResult;
    }

    /**
     * Sets the value of the getModelListXMLResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetModelListXMLResult(String value) {
        this.getModelListXMLResult = value;
    }

}
