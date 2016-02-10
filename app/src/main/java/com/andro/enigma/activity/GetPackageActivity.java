package com.andro.enigma.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andro.enigma.R;
import com.andro.enigma.adapter.ListAdapter;
import com.andro.enigma.database.*;
import com.andro.enigma.database.Package;
import com.andro.enigma.helper.Helper;
import com.andro.enigma.parser.JsonParser;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class GetPackageActivity extends Activity {

    private int packageId;
    private String packageTitle;
    private int type;
    private String lang;
    CrosswordDbHelper mDbHelper;
    Button button;
    PayPalPayment thingToBuy;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(Helper.CONFIG_ENVIRONMENT)
            .clientId(Helper.CONFIG_CLIENT_ID)
            .merchantName("Enigma Store")
            .merchantPrivacyPolicyUri(
                    Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_package);

        packageId = getIntent().getIntExtra("id", 0);
        packageTitle = getIntent().getStringExtra("title");
        lang = getIntent().getStringExtra("lang");
        type = getIntent().getIntExtra("type", 0);

        mDbHelper = new CrosswordDbHelper(GetPackageActivity.this);

        TextView title = (TextView) findViewById(R.id.textView_package_title);


        button = (Button) findViewById(R.id.button_get_package);

        title.setText(packageTitle);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                thingToBuy = new PayPalPayment(new BigDecimal("5"), "USD",
                        "Enigma package: " +  packageTitle , PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(GetPackageActivity.this, PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startActivityForResult(intent, Helper.REQUEST_CODE_PAYMENT);

                //new JSONParse().execute();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Helper.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data
                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        System.out.println(confirm.toJSONObject().toString(4));
                        System.out.println(confirm.getPayment().toJSONObject()
                                .toString(4));
                        Toast.makeText(getApplicationContext(), "Order placed",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                System.out.println("The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                System.out
                        .println("An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == Helper.REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject()
                                .toString(4));
                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);
                        sendAuthorizationToServer(auth);
                        Toast.makeText(getApplicationContext(),
                                "Future Payment code received from PayPal",
                                Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample",
                                "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {

    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            button.setEnabled(false);
            button.setTextColor(Color.parseColor("#999999"));
            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textView_package_done)).setText(R.string.download);
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JsonParser jsonParser = new JsonParser();

            JSONObject json = jsonParser.getJSONFromUrl(Helper.HOME_URL + "/service/getenigmasforpackage?seckey=EnIgmAAEIOU&id=" + packageId + "&user=13");

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            ArrayList<Crossword> list = new ArrayList<>();
            Crossword enigma = null;

            JSONArray data = new JSONArray();

            try {
                data = result.getJSONArray("data");
            } catch (JSONException e) {
                Log.d("MYTAG", "" + e.getMessage());
            }

            Package p = new Package(packageId,packageTitle,lang,"",type);

            mDbHelper.addPackage(p);

            for(int i = 0; i < data.length(); i++){
                try {
                    JSONObject obj = data.getJSONObject(i);
                    enigma = new Crossword(
                            Integer.parseInt(obj.getString("id_crw")),
                            i+1,
                            packageId,
                            obj.getString("words"),
                            "NO",
                            "0",
                            "en"
                    );
                }catch (JSONException ex){
                    Log.d("MYTAG", "JSONException " + ex.getMessage());
                }finally {
                    Log.d("MYTAG", "Insert ID " + mDbHelper.addCrossword(enigma));
                }
            }
            ((TextView) findViewById(R.id.textView_package_done)).setText(R.string.download_done);
            findViewById(R.id.progressBar2).setVisibility(View.GONE);
        }
    }

}
