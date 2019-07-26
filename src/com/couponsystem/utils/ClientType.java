package com.couponsystem.utils;

/**
 * @author Tal Yamin
 *
 */

/*
 * The following Enum sets a new type called "ClientType" and contains the
 * system client types : Administrator, Company and Customer. When used, each
 * type will return a different facade.
 */

public enum ClientType {

	ADMIN {
		@Override
		public String toString() {
			return "Admin";
		}
	},
	COMPANY {
		@Override
		public String toString() {
			return "Company";
		}
	},
	CUSTOMER {
		@Override
		public String toString() {
			return "Customer";
		}
	};

}
