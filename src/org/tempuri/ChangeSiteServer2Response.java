
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
 *         &lt;element name="ChangeSiteServer2Result" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "changeSiteServer2Result"
})
@XmlRootElement(name = "ChangeSiteServer2Response")
public class ChangeSiteServer2Response {

    @XmlElement(name = "ChangeSiteServer2Result")
    protected String changeSiteServer2Result;

    /**
     * Gets the value of the changeSiteServer2Result property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChangeSiteServer2Result() {
        return changeSiteServer2Result;
    }

    /**
     * Sets the value of the changeSiteServer2Result property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChangeSiteServer2Result(String value) {
        this.changeSiteServer2Result = value;
    }

}
