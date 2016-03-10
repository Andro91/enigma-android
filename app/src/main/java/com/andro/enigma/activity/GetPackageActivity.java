//package com.andro.enigma.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.PorterDuff;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.app.Activity;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.andro.enigma.R;
//import com.andro.enigma.adapter.ListAdapter;
//import com.andro.enigma.database.*;
//import com.andro.enigma.database.Package;
//import com.andro.enigma.helper.Helper;
//import com.andro.enigma.parser.JsonParser;
//import com.andro.enigma.settings.MySettings;
//import com.paypal.android.sdk.payments.PayPalAuthorization;
//import com.paypal.android.sdk.payments.PayPalConfiguration;
//import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
//import com.paypal.android.sdk.payments.PayPalPayment;
//import com.paypal.android.sdk.payments.PayPalService;
//import com.paypal.android.sdk.payments.PaymentActivity;
//import com.paypal.android.sdk.payments.PaymentConfirmation;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Text;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//
//public class GetPackageActivity extends AppCompatActivity {
//
//    private int packageId;
//    private String packageTitle;
//    private int type;
//    private String lang;
//    private double price;
//    private int count;
//    private int purchased;
//    CrosswordDbHelper mDbHelper;
//    Button button;
//    ImageButton imageButton;
//    PayPalPayment thingToBuy;
//
//    private static PayPalConfiguration config = new PayPalConfiguration()
//            .environment(Helper.CONFIG_ENVIRONMENT)
//            .clientId(Helper.CONFIG_CLIENT_ID)
//            .merchantName("Enigma Store")
//            .merchantPrivacyPolicyUri(
//                    Uri.parse("https://www.example.com/privacy"))
//            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_get_package);
//
//        SharedPreferences sharedpreferences = getSharedPreferences("Enigma user", Context.MODE_PRIVATE);
//
//        Log.d("USERNAME","" + sharedpreferences.getString("userId","NEMA"));
//        if(sharedpreferences.getString("userId",null) == null){
//            Intent i = new Intent(GetPackageActivity.this, LoginActivity.class);
//            i.putExtras(getIntent().getExtras());
//
//            startActivity(i);
//            finish();
//        }
//
//        packageId = getIntent().getIntExtra("id", 0);
//        packageTitle = getIntent().getStringExtra("title");
//        lang = getIntent().getStringExtra("lang");
//        type = getIntent().getIntExtra("type", 1);
//        price = getIntent().getDoubleExtra("price", 1.0);
//        count = getIntent().getIntExtra("count", 10);
//        purchased = getIntent().getIntExtra("purchased", 0);
//
//        mDbHelper = new CrosswordDbHelper(GetPackageActivity.this);
//
//        TextView title = (TextView) findViewById(R.id.textView_package_title);
//        TextView txtPrice = (TextView) findViewById(R.id.textView_package_price);
//        TextView txtEnigmaNumber = (TextView) findViewById(R.id.textView_package_number);
//
//        button = (Button) findViewById(R.id.button_get_package);
//        imageButton = (ImageButton) findViewById(R.id.image_button_get_package);
//
//        title.setText(packageTitle);
//        txtPrice.setText(String.format("%s %s", price, "\u20ac"));
//        txtEnigmaNumber.setText(String.format("%d %s", count, getResources().getString(R.string.enigmas_text)));
//
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
//        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#5087E1"), PorterDuff.Mode.MULTIPLY);
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                thingToBuy = new PayPalPayment(new BigDecimal("" + price), "EUR",
//                        "Enigma package: " + packageTitle, PayPalPayment.PAYMENT_INTENT_SALE);
//
//                Intent intent = new Intent(GetPackageActivity.this, PaymentActivity.class);
//
//                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                startActivityForResult(intent, Helper.REQUEST_CODE_PAYMENT);
//            }
//        });
//
//        imageButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                thingToBuy = new PayPalPayment(new BigDecimal("" + price), "EUR",
//                        "Enigma package: " + packageTitle, PayPalPayment.PAYMENT_INTENT_SALE);
//
//                Intent intent = new Intent(GetPackageActivity.this, PaymentActivity.class);
//
//                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//                startActivityForResult(intent, Helper.REQUEST_CODE_PAYMENT);
//            }
//        });
//
//        Helper.inicActionBarUp(this,getResources().getString(R.string.title_activity_get_package));
//
//        if (purchased == 1){
//            button.setEnabled(false);
//            button.setTextColor(Color.parseColor("#999999"));
//            imageButton.setEnabled(false);
//            imageButton.setVisibility(View.GONE);
//            TextView txtDownload = (TextView) findViewById(R.id.text_download);
//            Button butDownload = (Button) findViewById(R.id.button_download);
//
//            txtDownload.setVisibility(View.VISIBLE);
//            butDownload.setVisibility(View.VISIBLE);
//
//            butDownload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new JSONParse().execute("download");
//                }
//            });
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == Helper.REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                PaymentConfirmation confirm = data
//                        .getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
//                if (confirm != null) {
//                        new JSONParse().execute("buy",confirm.getProofOfPayment().getPaymentId());
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_success), Toast.LENGTH_SHORT).show();
//                    Log.d("MYTAG", confirm.getProofOfPayment().getPaymentId());
//                }
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_cancel), Toast.LENGTH_LONG).show();
//            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Toast.makeText(getApplicationContext(), getResources().getString(R.string.purchase_error), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
//
//    }
//
//    @Override
//    public void onDestroy() {
//        // Stop service when done
//        stopService(new Intent(this, PayPalService.class));
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if(id == android.R.id.home) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private class JSONParse extends AsyncTask<String, String, JSONObject> {
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            //button.setEnabled(false);
//            imageButton.setEnabled(false);
//            imageButton.setVisibility(View.GONE);
//            //button.setTextColor(Color.parseColor("#999999"));
//            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.textView_package_done)).setText(R.string.download);
//        }
//
//        @Override
//        protected JSONObject doInBackground(String... args) {
//            JsonParser jsonParser = new JsonParser();
//            int userId = 1;
//            if (args[0].equalsIgnoreCase("buy")) {
//                JSONObject JSONmessage = jsonParser.getJSONFromUrl(Helper.HOME_URL +
//                        "/service/savepaypaltransaction?seckey=EnIgmAAEIOU&package_id="
//                        + packageId + "&user_id="
//                        + userId + "&tran_id="
//                        + args[1]);
//                try {
//                    if (JSONmessage.getString("message").equalsIgnoreCase("Success")) {
//                        return jsonParser.getJSONFromUrl(Helper.HOME_URL +
//                                "/service/getenigmasforpackage?seckey=EnIgmAAEIOU" +
//                                "&package_id=" + packageId +
//                                "&user_id=" + userId);
//                    }
//                } catch (JSONException jex) {
//                    jex.printStackTrace();
//                }
//            }else if (args[0].equalsIgnoreCase("download")){
//                return jsonParser.getJSONFromUrl(Helper.HOME_URL +
//                        "/service/getenigmasforpackage?seckey=EnIgmAAEIOU" +
//                        "&package_id=" + packageId +
//                        "&user_id=" + userId);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject result) {
//            super.onPostExecute(result);
//
//            if (result == null){
//                Log.d("MYTAG","Purchase unsuccessful");
//                return;
//            }
//
//            ArrayList<Crossword> list = new ArrayList<>();
//            Crossword enigma = null;
//
//            JSONArray data = new JSONArray();
//
//            try {
//                data = result.getJSONArray("data");
//            } catch (JSONException e) {
//                Log.d("MYTAG", "" + e.getMessage());
//            }
//
//            int counter;
//            for(counter = 0; counter < data.length(); counter++){
//                try {
//                    JSONObject obj = data.getJSONObject(counter);
//                    enigma = new Crossword(
//                            Integer.parseInt(obj.getString("id_crw")),
//                            counter+1,
//                            packageId,
//                            obj.getString("words"),
//                            "NO",
//                            "0",
//                            lang
//                    );
//                }catch (JSONException ex){
//                    Log.d("MYTAG", "JSONException " + ex.getMessage());
//                }finally {
//                    Log.d("MYTAG", "Insert ID " + mDbHelper.addCrossword(enigma));
//                }
//            }
//            Log.d("COUNT","counter = " + counter);
//            Package p = new Package(packageId,packageTitle,lang,"",type,counter);
//
//            mDbHelper.addPackage(p);
//
//            findViewById(R.id.textView_package_done).setVisibility(View.VISIBLE);
//            ((TextView) findViewById(R.id.textView_package_done)).setText(R.string.download_done);
//            findViewById(R.id.progressBar2).setVisibility(View.GONE);
//
//            mDbHelper.close();
//        }
//    }
//
//}
