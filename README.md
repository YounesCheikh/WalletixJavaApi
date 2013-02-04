WalletixJavaApi
===============

Walletix API for Java applications 

You can use this api for both Desktop/Web Java application.

### How to?

Include the [WalletixJavaAPI.jar](https://github.com/cyounes/WalletixJavaApi/blob/master/WalletixJavaAPI.jar?raw=true)  file into your project then import the package 
```
import org.payment.wltx.* 
``` 

#### Constructors: 
if you want to make payment on sandbox to test [Walletix](https://www.walletix.com) : 
```
Walletix w = new Walletix(VENDOR_ID, API_KEY , true); 
```
else:

```
Walletix w = new Walletix(VENDOR_ID, API_KEY, false); 
// OR
Walletix w = new Walletix(VENDOR_ID, API_KEY) ;
```

#### Generate Payement:
```
// Generate 
GeneratePaymentCode gpc = new GeneratePaymentCode(PURCHASE_ID, AMOUNT,CALL_BACK_URL);
// Get Result :
gpc.getStatus();
gpc.getCode(); // return the generated code
```

#### Verify Payment:
```
VerifyPayment vp = new VerifyPayment(gpc.getCode());
vp.getStatus();
vp.getResult();

```

#### Delete Payment:
```
DeletePayment dp = new DeletePayment(gpc.getCode());
dp.getStatus();
dp.getResult();
```

### Useful links:

+ [WALLETIX API DOCUMETATION (FR)](https://www.walletix.com/documentation-api)
+ [Walletix Java Api Documentation](http://cyounes.github.com/WalletixJavaApi/) 

+ [Get Your API Key](https://www.walletix.com/api-key) 



	Please, Don't hesitate to post suggestions or [bug reports](https://github.com/cyounes/WalletixJavaApi/issues).



