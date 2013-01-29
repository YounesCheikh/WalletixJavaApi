/*
 * Walletix Java API 
 *
 * Copyright (C) 2013 
 * Author: Cheikh Younes  
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.payment.wltx;

/**
 * The Class GeneratePaymentCode.
 * @author <a href="http://cyounes.com/">Cheikh Younes</a>
 */
public class GeneratePaymentCode {

	private String[] str;
	
	/**
	 * Instantiates a new generate payment code.
	 *
	 * @param purchaseID the purchase id
	 * @param amount the amount
	 * @param callbackUrl the callback url
	 */
	public GeneratePaymentCode(String purchaseID, String amount,
			String callbackUrl){
		if (Walletix.vendorInfoSet()) {
			str = Walletix.postPaymentCode(purchaseID, amount, callbackUrl);
		} else {
			System.err.println("Please set the VendorID and the API KEY");
		}
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public int getStatus() {
		return Integer.parseInt(str[0]);
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return str[1];
	}

}
