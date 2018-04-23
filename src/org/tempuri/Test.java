
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="keys" type="{http://tempuri.org/}ArrayOfString" minOccurs="0"/>
 *         &lt;element name="wsID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "keys",
    "wsID"
})
@XmlRootElement(name = "Test")
public class Test {

    protected ArrayOfString keys;
    protected int wsID;

    /**
     * Gets the value of the keys property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfString }
     *     
     */
    public ArrayOfString getKeys() {
        return keys;
    }

    /**
     * Sets the value of the keys property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfString }
     *     
     */
    public void setKeys(ArrayOfString value) {
        this.keys = value;
    }

    /**
     * Gets the value of the wsID property.
     * 
     */
    public int getWsID() {
        return wsID;
    }

    /**
     * Sets the value of the wsID property.
     * 
     */
    public void setWsID(int value) {
        this.wsID = value;
    }

}
