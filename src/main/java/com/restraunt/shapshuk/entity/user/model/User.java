package com.restraunt.shapshuk.entity.user.model;

import com.restraunt.shapshuk.core.dao.IdentifiedRow;
import com.restraunt.shapshuk.entity.loyalty.model.Loyalty;
import com.restraunt.shapshuk.entity.role.model.UserRole;
import com.restraunt.shapshuk.entity.useraddress.model.UserAddress;
import com.restraunt.shapshuk.entity.wallet.model.Wallet;

import com.restraunt.shapshuk.validation.Email;
import com.restraunt.shapshuk.validation.MaxLength;
import com.restraunt.shapshuk.validation.MinLength;
import com.restraunt.shapshuk.validation.NotEmpty;
import com.restraunt.shapshuk.validation.Password;
import com.restraunt.shapshuk.validation.PhoneNumber;
import com.restraunt.shapshuk.validation.ValidBean;
import com.restraunt.shapshuk.core.constants.CommonAppConstants;

import java.util.List;

@ValidBean("user")
public class User implements IdentifiedRow {

    private Long id;
    @MinLength(5)
    @MaxLength(30)
    private String name;
    @NotEmpty
    @Password(regex = CommonAppConstants.PASSWORD_PATTERN)
    private String password;
    @NotEmpty
    @Email(regex = CommonAppConstants.EMAIL_PATTERN)
    private String email;
    private boolean isActive;
    @NotEmpty
    @PhoneNumber(regex = CommonAppConstants.PHONE_NUMBER_PATTERN)
    private String phoneNumber;

    private List<UserRole> roles;
    private Loyalty loyalty;
    private Wallet wallet;
    private UserAddress userAddress;

    public User() {
        this.loyalty = new Loyalty();
        this.wallet = new Wallet();
        this.userAddress = new UserAddress();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public Loyalty getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Loyalty loyalty) {
        this.loyalty = loyalty;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", isActive=" + isActive +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", roles=" + roles +
                ", loyalty=" + loyalty +
                ", wallet=" + wallet +
                ", userAddress=" + userAddress +
                '}';
    }
}
