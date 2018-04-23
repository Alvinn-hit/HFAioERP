
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
 *         &lt;element name="Kind" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Signed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "kind",
    "signed"
})
@XmlRootElement(name = "GetYuBeiAnDomainList_XML")
public class GetYuBeiAnDomainListXML {

    @XmlElement(name = "Kind")
    protected int kind;
    @XmlElement(name = "Signed")
    protected String signed;

    /**
     * Gets the value of the kind property.
     * 
     */
    public int getKind() {
        return kind;
    }

    /**
     * Sets the value of the kind property.
     * 
     */
    public void setKind(int value) {
        this.kind = value;
    }

    /**
     * Gets the value of the signed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigned() {
        return signed;
    }

    /**
     * Sets the value of the signed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigned(String value) {
        this.signed = value;
    }

}
