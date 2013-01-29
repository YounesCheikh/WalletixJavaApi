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
 * The Class VerifyPayment.
 * @author <a href="http://cyounes.com/>Cheikh Younes</a>
 * @version 1.0
 */
public class VerifyPayment {
	
	private String[] str;

	/**
	 * Instantiates a new verify payment.
	 *
	 * @param paymentCode the payment code
	 */
	public VerifyPayment(String paymentCode) {
		if (Walletix.vendorInfoSet()) {
			str = Walletix.postVerifyPayment(paymentCode);
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
	 * Gets the result.
	 *
	 * @return the result
	 */
	public int getResult() {
		return Integer.parseInt(str[1]);
	}

}
