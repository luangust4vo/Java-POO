package model;

// Annotations
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.enums.TransactionType;

import java.util.Date;

@Entity // Specifies that this class is an entity of database
@Table(name = "transaction") // Specifies a standard name for the table (´cause different OS can give names in different patterns )
public class Transaction {
	@Id // Specifies that this column will be the PK of table
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Specifies that the generation of id´s will be auto_increment / serial
	private Long id;
	
	@Column(name = "value") // Same thing of table, but for columns
	private Double value;
	@Column(name = "date")
	@Temporal(TemporalType.TIMESTAMP) // Specifies the date pattern (date, time_stamp, date_time)
	private Date transactionDate;
	@Column(name = "description", length = 150) // length specifies the VARCHAR length
	private String description;
	@Column(name = "type")
	private TransactionType transactionType;
	
	@ManyToOne
	@JoinColumn(name = "account_id")
	private Account account;
	
	public Transaction() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getValue() {
		return value;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
