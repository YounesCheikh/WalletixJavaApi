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

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class Walletix.
 * 
 * @author <a href="http://cyounes.com/">Cheikh Younes</a>
 * @version 1.1
 */
public class Walletix {

	// Uncomment the following lines to test

//	public static void main(String[] args) {
//
//		// Identification
//		Walletix w = new Walletix("", "",
//				true);
//		String code;
//		try {
//			// Génération d'un nouveau code
//			code = w.generatePaymentCode("11", "11", "http:/google.com");
//			// Affichage
//			System.out.println("Code: " + code);
//
//			// Verification
//			System.out.println("Paiement " + code + " effectué? "
//					+ w.verifyPayment(code));
//
//			// Supprission
//			System.out.println("Le code de paiment " + code + " supprimé? "
//					+ w.deletePayment(code));
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//
//	}

	/**
	 * Instantiates a new walletix.
	 * 
	 * @param VENDOR_ID
	 *            the vendor id
	 * @param API_KEY
	 *            the api key
	 */
	public Walletix(String VENDOR_ID, String API_KEY) {
		this.VENDOR_ID = VENDOR_ID;
		this.API_KEY = API_KEY;
		this.API_PATH = "https://www.walletix.com/api/";
	}

	/**
	 * Instantiates a new walletix.
	 * 
	 * @param VENDOR_ID
	 *            the vendor id
	 * @param API_KEY
	 *            the api key
	 * @param sandbox
	 *            true if you want to test walletix sandbox
	 */
	public Walletix(String VENDOR_ID, String API_KEY, boolean sandbox) {
		this.VENDOR_ID = VENDOR_ID;
		this.API_KEY = API_KEY;
		this.API_PATH = "https://www.walletix.com/";
		API_PATH += (sandbox) ? "sandbox/api/" : "api/";
	}

	/**
	 * Post payment code.
	 * 
	 * @param purchaseID
	 *            the purchase id
	 * @param amount
	 *            the amount
	 * @param callbackUrl
	 *            the callback url
	 * @return the string[]
	 * @throws Exception
	 */
	public String generatePaymentCode(String purchaseID, String amount,
			String callbackUrl) throws Exception {
		String[] str;
		String urlParameters = "";
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(this.VENDOR_ID, "UTF-8") + "&apiKey="
					+ URLEncoder.encode(this.API_KEY, "UTF-8") + "&purchaseID="
					+ URLEncoder.encode(purchaseID, "UTF-8") + "&amount="
					+ URLEncoder.encode(amount, "UTF-8") + "&format="
					+ URLEncoder.encode("'xml'", "UTF-8") + "&callbackurl="
					+ URLEncoder.encode(callbackUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			str = post(this.API_PATH + this.GENERATE_PAYMENT_CODE,
					urlParameters);
		}
		if (str[0].equals("1")) {
			return str[1];
		} else
			throw new Exception(walletixErrorMessage(str[1]));
	}

	/**
	 * Post verify payment.
	 * 
	 * @param paiementCode
	 *            the paiement code
	 * @return the string[]
	 * @throws Exception
	 */
	public boolean verifyPayment(String paiementCode) throws Exception {
		String urlParameters = "";
		String[] str;
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(this.VENDOR_ID, "UTF-8") + "&apiKey="
					+ URLEncoder.encode(this.API_KEY, "UTF-8")
					+ "&paiementCode="
					+ URLEncoder.encode(paiementCode, "UTF-8") + "&format="
					+ URLEncoder.encode("'xml'", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			str = post(this.API_PATH + this.VERIFY_PAYMENT, urlParameters);
		}
		if (str[0].equals("1")) {
			return str[1].equals("1");
		} else
			throw new Exception(walletixErrorMessage(str[1]));
	}

	/**
	 * Post delete payment.
	 * 
	 * @param paiementCode
	 *            the paiement code
	 * @return the string[]
	 * @throws Exception
	 */
	public boolean deletePayment(String paiementCode) throws Exception {
		String[] str;
		String urlParameters = "";
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(this.VENDOR_ID, "UTF-8") + "&apiKey="
					+ URLEncoder.encode(this.API_KEY, "UTF-8")
					+ "&paiementCode="
					+ URLEncoder.encode(paiementCode, "UTF-8") + "&format="
					+ URLEncoder.encode("'xml'", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			str = post(this.API_PATH + DELETE_PAYMENT, urlParameters);
		}
		if (str[0].equals("1")) {
			return str[1].equals("1");
		} else
			throw new Exception(walletixErrorMessage(str[1]));
	}

	/*
	 * Request from walletix server.
	 */
	private String[] post(String targetURL, String urlParameters) {
		URL url;
		HttpsURLConnection connection = null;
		try {
			// Create connection
			url = new URL(targetURL);
			connection = (HttpsURLConnection) url.openConnection();
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length",
					"" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// Get Response
			InputStream is = connection.getInputStream();
			int code = targetURL.endsWith(GENERATE_PAYMENT_CODE) ? 1 : 2;
			return treatXml(is, code);

		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	private String[] treatXml(InputStream is, int code) {
		String ret[] = { "", "" };
		String result = (code == 1) ? "code" : "result";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("response");
			Node fstNode = nodeLst.item(0);

			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {

				Element fstElmnt = (Element) fstNode;
				NodeList fstElmntLst = fstElmnt.getElementsByTagName("status");
				Element fstNodeElmnt = (Element) fstElmntLst.item(0);
				NodeList fstN = fstNodeElmnt.getChildNodes();
				ret[0] = ((Node) fstN.item(0)).getNodeValue();
				// System.out.println("Status : " + ((Node)
				// fstNm.item(0)).getNodeValue());
				NodeList lstNodeElmntLst = fstElmnt
						.getElementsByTagName(result);
				Element lstNodeElmnt = (Element) lstNodeElmntLst.item(0);
				NodeList lstN = lstNodeElmnt.getChildNodes();
				// System.out.println("Code : " + ((Node)
				// lstNm.item(0)).getNodeValue());
				ret[1] = ((Node) lstN.item(0)).getNodeValue();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage() + "\nCause: " + e.getCause());
		}
		return ret;
	}

	private String walletixErrorMessage(String error) {
		String retMessage = new String();
		if (error.equals("0"))
			retMessage = "erreur lors de la génération du code de paiement";
		else if (error.equals("-1"))
			retMessage = "le N° de commande ou le montant ne sont pas numérique";
		else if (error.equals("-2"))
			retMessage = "ID utilisateur non numérique";
		else if (error.equals("-3"))
			retMessage = "problème d’authentification (vendorID et/ou apiKey incorrect)";
		return retMessage;
	}

	/** The Constant API_PATH. */
	private String API_PATH;

	/** The Constant GENERATE_PAYMENT_CODE. */
	private final String GENERATE_PAYMENT_CODE = "paymentcode";

	/** The Constant DELETE_PAYMENT. */
	private final String DELETE_PAYMENT = "deletepayment";

	/** The Constant VERIFY_PAYMENT. */
	private final String VERIFY_PAYMENT = "paymentverification";

	/** The vendor id. */
	private String VENDOR_ID = null;

	/** The Api key. */
	private String API_KEY = null;
}
