
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="wsID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CPName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Domain" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Kind" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SiteServer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="AgtName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OpUsID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "wsID",
    "cpName",
    "domain",
    "model",
    "kind",
    "siteServer",
    "endDate",
    "agtName",
    "opUsID",
    "signed"
})
@XmlRootElement(name = "XuFeiSuperWebsite")
public class XuFeiSuperWebsite {

    protected int wsID;
    @XmlElement(name = "CPName")
    protected String cpName;
    @XmlElement(name = "Domain")
    protected String domain;
    @XmlElement(name = "Model")
    protected int model;
    @XmlElement(name = "Kind")
    protected int kind;
    @XmlElement(name = "SiteServer")
    protected String siteServer;
    @XmlElement(name = "EndDate", required = true)
    protected XMLGregorianCalendar endDate;
    @XmlElement(name = "AgtName")
    protected String agtName;
    @XmlElement(name = "OpUsID")
    protected int opUsID;
    @XmlElement(name = "Signed")
    protected String signed;

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
     * Gets the value of the cpName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPName() {
        return cpName;
    }

    /**
     * Sets the value of the cpName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPName(String value) {
        this.cpName = value;
    }

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomain(String value) {
        this.domain = value;
    }

    /**
     * Gets the value of the model property.
     * 
     */
    public int getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     */
    public void setModel(int value) {
        this.model = value;
    }

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
     * Gets the value of the siteServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteServer() {
        return siteServer;
    }

    /**
     * Sets the value of the siteServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteServer(String value) {
        this.siteServer = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEndDate(XMLGregorianCalendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the agtName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgtName() {
        return agtName;
    }

    /**
     * Sets the value of the agtName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgtName(String value) {
        this.agtName = value;
    }

    /**
     * Gets the value of the opUsID property.
     * 
     */
    public int getOpUsID() {
        return opUsID;
    }

    /**
     * Sets the value of the opUsID property.
     * 
     */
    public void setOpUsID(int value) {
        this.opUsID = value;
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
