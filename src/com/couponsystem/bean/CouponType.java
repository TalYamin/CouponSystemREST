package com.couponsystem.bean;


/**
 * @author Shay Ben Haroush
 *
 */

/*
 * The following Enum sets a new type called "CouponType". it contains the coupon
 * types that can be used in order to add a new coupon. In order to add a new
 * Coupon Type it must be updated within this Enum.
 * There is override method of toString in order to use another pattern of print.
 */ 


public enum CouponType {

	RESTAURANTS
	{
		@Override
	    public String toString() {
	      return "Restaurants";
		}
	},	
	
	HEALTH
	{
		@Override
	    public String toString() {
	      return "Health";
		}
	},
	
	SPORTS
	{
		@Override
	    public String toString() {
	      return "Sports";
		}
	},
	
	TRAVELING
	{
		@Override
	    public String toString() {
	      return "Traveling";
		}
	}
	
}
