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

public class WalletixException extends Exception {

	private static final long serialVersionUID = 1L;

	public WalletixException() {

	}

	public WalletixException(String message) {
		super(message);
	}

	public WalletixException(Throwable cause) {
		super(cause);
	}

	public WalletixException(String message, Throwable cause) {
		super(message, cause);
	}

}
