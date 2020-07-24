package com.lei2j.douyu.pojo;

import com.lei2j.douyu.core.pojo.UpdateEntity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by lei2j on 2018/12/12.
 */
@Table(name = "u_user")
public class UserEntity extends UpdateEntity {

    @Id
    private Integer id;

    private String username;

    private String phone;

    private String email;

    private String icon;

    private String password;

    private String passSalt;

    private Integer lastLoginIp;

    private Timestamp lastLoginTime;

    private Integer curLoginIp;

    private Timestamp curLoginTime;

    private String status;

    private Integer registIp;

    private Integer loginFailNum;

    private Timestamp loginLockTime;

    public UserEntity() {
    }

    public UserEntity(Integer id, String username, String phone, String email, String icon, String password, String passSalt, Integer lastLoginIp, Timestamp lastLoginTime, Integer curLoginIp,
                      Timestamp curLoginTime, String status, Integer registIp, Integer loginFailNum, Timestamp loginLockTime) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.icon = icon;
        this.password = password;
        this.passSalt = passSalt;
        this.lastLoginIp = lastLoginIp;
        this.lastLoginTime = lastLoginTime;
        this.curLoginIp = curLoginIp;
        this.curLoginTime = curLoginTime;
        this.status = status;
        this.registIp = registIp;
        this.loginFailNum = loginFailNum;
        this.loginLockTime = loginLockTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public Integer getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(Integer lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getCurLoginIp() {
        return curLoginIp;
    }

    public void setCurLoginIp(Integer curLoginIp) {
        this.curLoginIp = curLoginIp;
    }

    public Timestamp getCurLoginTime() {
        return curLoginTime;
    }

    public void setCurLoginTime(Timestamp curLoginTime) {
        this.curLoginTime = curLoginTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getRegistIp() {
        return registIp;
    }

    public void setRegistIp(Integer registIp) {
        this.registIp = registIp;
    }

    public Integer getLoginFailNum() {
        return loginFailNum;
    }

    public void setLoginFailNum(Integer loginFailNum) {
        this.loginFailNum = loginFailNum;
    }

    public Timestamp getLoginLockTime() {
        return loginLockTime;
    }

    public void setLoginLockTime(Timestamp loginLockTime) {
        this.loginLockTime = loginLockTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("UserEntity{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", icon='").append(icon).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", passSalt='").append(passSalt).append('\'');
        sb.append(", lastLoginIp=").append(lastLoginIp);
        sb.append(", lastLoginTime=").append(lastLoginTime);
        sb.append(", curLoginIp=").append(curLoginIp);
        sb.append(", curLoginTime=").append(curLoginTime);
        sb.append(", status='").append(status).append('\'');
        sb.append(", registIp=").append(registIp);
        sb.append(", loginFailNum=").append(loginFailNum);
        sb.append(", loginLockTime=").append(loginLockTime);
        sb.append('}');
        return sb.toString();
    }
}
