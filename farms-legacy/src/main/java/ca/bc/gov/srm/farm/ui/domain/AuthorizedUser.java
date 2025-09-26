package ca.bc.gov.srm.farm.ui.domain;

import java.util.Date;


/**
 * Used on screen 340.
 */
public class AuthorizedUser {

  private Integer agristabilityRepresntveId;

  private Integer clientSubscriptionId;

  private String userGuid;

  private String userid;

  private Date activatedDate;

  private String userName;

  private String userPhoneNumber;

  private Integer revisionCount; // for the subscription


  /**
   * @return  the activatedDate
   */
  public Date getActivatedDate() {
    return activatedDate;
  }

  /**
   * @param  activatedDate  the activatedDate to set
   */
  public void setActivatedDate(Date activatedDate) {
    this.activatedDate = activatedDate;
  }

  /**
   * @return  the agristabilityRepresntveId
   */
  public Integer getAgristabilityRepresntveId() {
    return agristabilityRepresntveId;
  }

  /**
   * @param  agristabilityRepresntveId  the agristabilityRepresntveId to set
   */
  public void setAgristabilityRepresntveId(Integer agristabilityRepresntveId) {
    this.agristabilityRepresntveId = agristabilityRepresntveId;
  }

  /**
   * @return  the clientSubscriptionId
   */
  public Integer getClientSubscriptionId() {
    return clientSubscriptionId;
  }

  /**
   * @param  clientSubscriptionId  the clientSubscriptionId to set
   */
  public void setClientSubscriptionId(Integer clientSubscriptionId) {
    this.clientSubscriptionId = clientSubscriptionId;
  }

  /**
   * @return  the userGuid
   */
  public String getUserGuid() {
    return userGuid;
  }

  /**
   * @param  userGuid  the userGuid to set
   */
  public void setUserGuid(String userGuid) {
    this.userGuid = userGuid;
  }

  /**
   * @return  the userid
   */
  public String getUserid() {
    return userid;
  }

  /**
   * @param  userid  the userid to set
   */
  public void setUserid(String userid) {
    this.userid = userid;
  }

  /**
   * @return  the userName
   */
  public String getUserName() {
    return userName;
  }

  /**
   * @param  userName  the userName to set
   */
  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * @return  the userPhoneNumber
   */
  public String getUserPhoneNumber() {
    return userPhoneNumber;
  }

  /**
   * @param  userPhoneNumber  the userPhoneNumber to set
   */
  public void setUserPhoneNumber(String userPhoneNumber) {
    this.userPhoneNumber = userPhoneNumber;
  }

  /**
   * @return  the revisionCount
   */
  public Integer getRevisionCount() {
    return revisionCount;
  }

  /**
   * @param  revisionCount  the revisionCount to set
   */
  public void setRevisionCount(Integer revisionCount) {
    this.revisionCount = revisionCount;
  }

}
