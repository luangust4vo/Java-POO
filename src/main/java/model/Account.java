package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.types.AccountType;

@Entity
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "open_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date openDate;
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private AccountType type;
	@Column(name = "is_active")
	private boolean isActive;
	@Column(name = "approved_limit")
	private Double approvedLimit;
	@Column(name = "password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name = "account_holder_id")
	private AccountHolder accountHolder;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getOpenDate() {
		return openDate;
	}
	
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	
	public AccountType getType() {
		return type;
	}
	
	public void setType(AccountType type) {
		this.type = type;
	}
	
	public AccountHolder getAccountHolder() {
		return accountHolder;
	}
	
	public void setAccountHolder(AccountHolder accountHolder) {
		this.accountHolder = accountHolder;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Double getApprovedLimit() {
		return approvedLimit;
	}

	public void setApprovedLimit(Double approvedLimit) {
		this.approvedLimit = approvedLimit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    @Override
    public String toString() {
        return "Account [id=" + id + ", openDate=" + openDate + ", type=" + type + ", isActive=" + isActive
                + ", approvedLimit=" + approvedLimit + ", accountHolder=" + accountHolder + ", getId()=" + getId()
                + ", getOpenDate()=" + getOpenDate() + ", getType()=" + getType() + ", getAccountHolder()="
                + getAccountHolder() + ", isActive()=" + isActive() + ", getApprovedLimit()=" + getApprovedLimit()
                + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
                + "]";
    }	
}
