
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
 *         &lt;element name="_key" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mWsID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="siteIndex" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="month" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "key",
    "mWsID",
    "siteIndex",
    "month"
})
@XmlRootElement(name = "XuFeiChildSiteByMonth")
public class XuFeiChildSiteByMonth {

    @XmlElement(name = "_key")
    protected String key;
    protected int mWsID;
    protected int siteIndex;
    protected int month;

    /**
     * Gets the value of the key property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the value of the key property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKey(String value) {
        this.key = value;
    }

    /**
     * Gets the value of the mWsID property.
     * 
     */
    public int getMWsID() {
        return mWsID;
    }

    /**
     * Sets the value of the mWsID property.
     * 
     */
    public void setMWsID(int value) {
        this.mWsID = value;
    }

    /**
     * Gets the value of the siteIndex property.
     * 
     */
    public int getSiteIndex() {
        return siteIndex;
    }

    /**
     * Sets the value of the siteIndex property.
     * 
     */
    public void setSiteIndex(int value) {
        this.siteIndex = value;
    }

    /**
     * Gets the value of the month property.
     * 
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the value of the month property.
     * 
     */
    public void setMonth(int value) {
        this.month = value;
    }

}
