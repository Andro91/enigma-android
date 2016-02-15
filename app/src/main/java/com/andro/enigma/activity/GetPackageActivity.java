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
    private double price;
    private int count;
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
        type = getIntent().getIntExtra("type", 1);
        price = getIntent().getDoubleExtra("price", 1.0);
        count = getIntent().getIntExtra("count", 10);

        mDbHelper = new CrosswordDbHelper(GetPackageActivity.this);

        TextView title = (TextView) findViewById(R.id.textView_package_title);
        TextView txtPrice = (TextView) findViewById(R.id.textView_package_price);
        TextView txtEnigmaNumber = (TextView) findViewById(R.id.textView_package_number);

        button = (Button) findViewById(R.id.button_get_package);

        title.setText(packageTitle);
        txtPrice.setText(String.format("%s %s", price, "\u20ac"));
        txtEnigmaNumber.setText(String.format("%d %s", count, getResources().getString(R.string.enigmas_text)));

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thingToBuy = new PayPalPayment(new BigDecimal("" + price), "EUR",
                        "Enigma package: " + packageTitle, PayPalPayment.PAYMENT_INTENT_SALE);

                Intent intent = new Intent(GetPackageActivity.this, PaymentActivity.class);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                startActivityForResult(intent, Helper.REQUEST_CODE_PAYMENT);
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
                        new JSONParse().execute();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_success), Toast.LENGTH_SHORT).show();
                    Log.d("MYTAG", confirm.getProofOfPayment().getPaymentId());
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_cancel), Toast.LENGTH_LONG).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_error), Toast.LENGTH_LONG).show();
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
            return jsonParser.getJSONFromUrl(Helper.HOME_URL + "/service/getenigmasforpackage?seckey=EnIgmAAEIOU&package_id=" + packageId + "&user_id=1");
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
                            lang
                    );
                }catch (JSONException ex){
                    Log.d("MYTAG", "JSONException " + ex.getMessage());
                }finally {
                    Log.d("MYTAG", "Insert ID " + mDbHelper.addCrossword(enigma));
                }
            }
            ((TextView) findViewById(R.id.textView_package_done)).setText(R.string.download_done);
            findViewById(R.id.progressBar2).setVisibility(View.GONE);

            mDbHelper.close();
        }
    }

}
