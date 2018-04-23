
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
 *         &lt;element name="wsID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="menuType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="isShow" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "wsID",
    "menuType",
    "isShow"
})
@XmlRootElement(name = "ModifyMenuState")
public class ModifyMenuState {

    @XmlElement(name = "_key")
    protected String key;
    protected int wsID;
    protected int menuType;
    protected boolean isShow;

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

    /**
     * Gets the value of the menuType property.
     * 
     */
    public int getMenuType() {
        return menuType;
    }

    /**
     * Sets the value of the menuType property.
     * 
     */
    public void setMenuType(int value) {
        this.menuType = value;
    }

    /**
     * Gets the value of the isShow property.
     * 
     */
    public boolean isIsShow() {
        return isShow;
    }

    /**
     * Sets the value of the isShow property.
     * 
     */
    public void setIsShow(boolean value) {
        this.isShow = value;
    }

}
