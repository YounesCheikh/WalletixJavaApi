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
 * @author <a href="http://cyounes.com/">Cheikh Younes</a>
 * @version 1.0
 */
public class Walletix {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		// Identifier (Vendor id et l'api key) 
		new Walletix("", "");
		
		// générer un code de paiement
		GeneratePaymentCode gpc = new GeneratePaymentCode("11", "100",
				"http://cyounes.com/");
		// si le code a bien été généré sans problème 
		if (gpc.getStatus() == 1) {
			// Afficher le code de paiement 
			System.out.println("Le code généré est: " + gpc.getCode());
			
			// vérifier le paiement du code généré
			VerifyPayment vp = new VerifyPayment(gpc.getCode());
			if (vp.getResult() == 1) {
				System.out.println("Le paiement "+gpc.getCode()+" a bien été effectué");
			} else {
				System.out.println("Le paiement "+gpc.getCode()+" n'a pas été effectué");
			}

			// supprimer le code généré 
			DeletePayment dp = new DeletePayment(gpc.getCode());
			if (dp.getResult() == 1) {
				System.out.println("Le code " + gpc.getCode()
						+ " a bien été supprimé");
			}
		}
		else {
			System.out.println("Erreur numero : "+gpc.getStatus());
		}
	}
	

	/** The Constant API_PATH. */
	private final static String API_PATH = "https://www.walletix.com/api/";
	
	/** The Constant GENERATE_PAYMENT_CODE. */
	private final static String GENERATE_PAYMENT_CODE = "paymentcode";
	
	/** The Constant DELETE_PAYMENT. */
	private final static String DELETE_PAYMENT = "deletepayment";
	
	/** The Constant VERIFY_PAYMENT. */
	private final static String VERIFY_PAYMENT = "paymentverification";
	
	/** The vendor id. */
	private static String VENDOR_ID = null;
	
	/** The api key. */
	private static String API_KEY = null;

	/**
	 * Instantiates a new walletix.
	 *
	 * @param VENDOR_ID the vendor id
	 * @param API_KEY the api key
	 */
	public Walletix(String VENDOR_ID, String API_KEY) {
		Walletix.VENDOR_ID = VENDOR_ID;
		Walletix.API_KEY = API_KEY;
	}

	/**
	 * Vendor info set.
	 *
	 * @return true, if successful
	 */
	public static boolean vendorInfoSet() {
		return (Walletix.VENDOR_ID != null && Walletix.API_KEY != null);
	}


	/**
	 * Treat xml.
	 *
	 * @param is the is
	 * @param code the code
	 * @return the string[]
	 */
	private static String[] treatXml(InputStream is, int code) {
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

	/**
	 * Request from walletix server
	 *
	 * @param targetURL the target url
	 * @param urlParameters the url parameters
	 * @return the string[]
	 */
	private static String[] post(String targetURL, String urlParameters) {
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

	/**
	 * Post payment code.
	 *
	 * @param purchaseID the purchase id
	 * @param amount the amount
	 * @param callbackUrl the callback url
	 * @return the string[]
	 */
	@SuppressWarnings("finally")
	protected static String[] postPaymentCode(String purchaseID, String amount,
			String callbackUrl) {
		String urlParameters = "";
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(Walletix.VENDOR_ID, "UTF-8")
					+ "&apiKey=" + URLEncoder.encode(Walletix.API_KEY, "UTF-8")
					+ "&purchaseID=" + URLEncoder.encode(purchaseID, "UTF-8")
					+ "&amount=" + URLEncoder.encode(amount, "UTF-8")
					+ "&format=" + URLEncoder.encode("'xml'", "UTF-8")
					+ "&callbackurl=" + URLEncoder.encode(callbackUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			final String[] str = post(Walletix.API_PATH
					+ Walletix.GENERATE_PAYMENT_CODE, urlParameters);
			// System.out.println(str[0] + ":" + str[1]);
			return str;
		}
	}


	/**
	 * Post verify payment.
	 *
	 * @param paiementCode the paiement code
	 * @return the string[]
	 */
	@SuppressWarnings("finally")
	protected static String[] postVerifyPayment(String paiementCode) {
		String urlParameters = "";
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(Walletix.VENDOR_ID, "UTF-8")
					+ "&apiKey=" + URLEncoder.encode(Walletix.API_KEY, "UTF-8")
					+ "&paiementCode="
					+ URLEncoder.encode(paiementCode, "UTF-8") + "&format="
					+ URLEncoder.encode("'xml'", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			final String[] str = post(Walletix.API_PATH + VERIFY_PAYMENT,
					urlParameters);
			// System.out.println(str[0] + ":" + str[1]);
			return str;
		}
		// return 0;
	}

	/**
	 * Post delete payment.
	 *
	 * @param paiementCode the paiement code
	 * @return the string[]
	 */
	@SuppressWarnings("finally")
	protected static String[] postDeletePayment(String paiementCode) {
		String urlParameters = "";
		try {
			urlParameters = "vendorID="
					+ URLEncoder.encode(Walletix.VENDOR_ID, "UTF-8")
					+ "&apiKey=" + URLEncoder.encode(Walletix.API_KEY, "UTF-8")
					+ "&paiementCode="
					+ URLEncoder.encode(paiementCode, "UTF-8") + "&format="
					+ URLEncoder.encode("'xml'", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println(e.getMessage());
		} finally {
			final String[] str = post(Walletix.API_PATH + DELETE_PAYMENT,
					urlParameters);
			// System.out.println(str[0] + ":" + str[1]);
			return str;
		}
		// return 0;
	}
}
