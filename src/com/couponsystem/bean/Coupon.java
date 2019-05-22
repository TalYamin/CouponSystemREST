package com.couponsystem.bean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.couponsystem.utils.DateConverterUtil;



/**
 * @author Shay Ben Haroush
 *
 */

/*
 * This class sets Coupon type object.
 */

public class Coupon {

	/* Data members of Coupon */
	private long couponId;
	private String title;
	private LocalDate startDate;
	private LocalDate endDate;
	private int amount;
	private CouponType type;
	private String couponMessage;
	private double price;
	private String image;
	private String customStartDate;
	private String customeEndDate;
	private boolean active;

	/* Empty CTOR Coupon */
	public Coupon() {

	}

	// Full CTOR Coupon: sets the couponId, title, endDate, amount, type,
	// couponMessage, price, image */
	public Coupon(long couponId, String title, String endDate, int amount, CouponType type, String couponMessage,
			double price, String image) {
		setCouponId(couponId);
		setTitle(title);
		setStartDate(LocalDate.now());
		setEndDate(DateConverterUtil.convertStringDate(endDate));
		setAmount(amount);
		setType(type);
		setCouponMessage(couponMessage);
		setPrice(price);
		setImage(image);
		setActive(true);
	}

	/* Getter method to receive the value of coupon id */
	public long getCouponId() {
		return this.couponId;
	}

	/* Setter method to set the value of coupon id */
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	/* Getter method to receive the value of coupon title */
	public String getTitle() {
		return this.title;
	}

	/* Setter method to set the value of coupon title */
	public void setTitle(String title) {
		this.title = title;
	}

	/* Getter method to receive the value of coupon start date */
	public LocalDate getStartDate() {
		return this.startDate;
	}

	/* Setter method to set the value of coupon start date */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	/* Getter method to receive the value of coupon end date */
	public LocalDate getEndDate() {
		return this.endDate;
	}

	/* Setter method to set the value of coupon end date */
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	/* Getter method to receive the value of coupon amount */
	public int getAmount() {
		return this.amount;
	}

	/* Setter method to set the value of coupon amount */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/* Getter method to receive the value of coupon type */
	public CouponType getType() {
		return this.type;
	}

	/* Setter method to set the value of coupon type */
	public void setType(CouponType type) {
		this.type = type;
	}

	/* Getter method to receive the value of coupon message */
	public String getCouponMessage() {
		return this.couponMessage;
	}

	/* Setter method to set the value of coupon message */
	public void setCouponMessage(String couponMessage) {
		this.couponMessage = couponMessage;
	}

	/* Getter method to receive the value of coupon price */
	public double getPrice() {
		return this.price;
	}

	/* Setter method to set the value of coupon price */
	public void setPrice(double price) {
		this.price = price;
	}

	/* Getter method to receive the value of coupon image */
	public String getImage() {
		return this.image;
	}

	/* Setter method to set the value of coupon image */
	public void setImage(String image) {
		this.image = image;
	}

	/* Getter method to receive the value of coupon activation */
	public boolean isActive() {
		return active;
	}

	/* Setter method to set the value of coupon activation */
	public void setActive(boolean active) {
		this.active = active;
	}

	/*
	 * toString method of Coupon - allows pattern to print Using formatter pattern
	 * to custom date view.
	 */
	@Override
	public String toString() {

		customStartDate = DateConverterUtil.DateStringFormat(this.startDate);
		customeEndDate = DateConverterUtil.DateStringFormat(this.endDate);

		return "Coupon [couponId=" + this.getCouponId() + ", title=" + this.getTitle() + ", startDate="
				+ this.customStartDate + ", endDate=" + this.customeEndDate + ", amount=" + this.getAmount() + ", type="
				+ this.getType() + ", couponMessage=" + this.getCouponMessage() + ", price=" + this.getPrice()
				+ ", image=" + this.getImage() + ", active=" + this.isActive() + "]";
	}

}