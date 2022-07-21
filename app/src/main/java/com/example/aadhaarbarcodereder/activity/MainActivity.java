
package com.example.aadhaarbarcodereder.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aadhaarbarcodereder.R;
import com.example.aadhaarbarcodereder.activity.encryption.HashGenerator;
import com.example.aadhaarbarcodereder.activity.encryption.digitalSignature;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


public class MainActivity extends AppCompatActivity {
    TextView tv;
    Button qrbutton;
    String[] arrSplit;
    String[] arr;
    PublicKey publicKey = null;
    byte[] signature=null;
    String sign = null;
    String hash = null;

    private static final String CERTIFICATE_TYPE = "X.509";
    private static final String JCE_PROVIDER = "BC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        qrbutton = findViewById(R.id.qr_button);




        qrbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

//                try {
//
//                   // byte[] readFileBytes = Files.readAllBytes(Paths.get(getCacheDir() + "/public.der"));
//
//                    Path path = Paths.get("\\public.der");
//                    byte[] readFileBytes =Files.readAllBytes(path);
//                    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes);
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    publicKey = keyFactory.generatePublic(publicSpec);
//                    Log.e("publickey_Amit", String.valueOf(publicKey));
//                } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
//                    e.printStackTrace();
//                }


//                try {
//                    InputStream filepaths = MainActivity.this.getAssets().open("public.der");
//                    // BufferedReader br = new BufferedReader(new InputStreamReader(filepaths));
//                   // byte[] targetArray = new byte[filepaths.available()];
//                    byte[] bytes = IOUtils.toByteArray(filepaths);
//                    //   byte[] readFileBytes = Files.readAllBytes(path);
//                    X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(bytes);
//                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//                    publicKey = keyFactory.generatePublic(publicSpec);
//                    Log.e("publickey_Amit", String.valueOf(publicKey));
//                     } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
//                    e.printStackTrace();
//                    }

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setOrientationLocked(false);
                integrator.initiateScan();


//                try {
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.encodeBitmap("content", BarcodeFormat.QR_CODE, 400, 400);
//                    ImageView imageViewQrCode = (ImageView) findViewById(R.id.qrCode);
//                    imageViewQrCode.setImageBitmap(bitmap);
//                } catch(Exception e) {
//
//                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents().toString();

           // tv.setText(result);
            //handle scan result



            try {
                InputStream filepaths = MainActivity.this.getAssets().open("public.der");
                // BufferedReader br = new BufferedReader(new InputStreamReader(filepaths));
                byte[] bytes = IOUtils.toByteArray(filepaths);
                //   byte[] readFileBytes = Files.readAllBytes(path);
                X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(bytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                publicKey = keyFactory.generatePublic(publicSpec);
                Log.e("publickey_Amit", String.valueOf(publicKey));
            } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
            }



            try {
                //Demo string for testing
               // String demostring = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><BarCodeData><PersonalDetails co=\"D/Anil Kumar Gupta\" dist=\"Jaipur\" dob=\"17-09-1995\" email=\"gupta.gargi855@gmail.com\" gender=\"F\" image=\"9j/4AAQSkZJRgABAQEAYABgAAD/4QAiRXhpZgAATU0AKgAAAAgAAQESAAMAAAABAAEAAAAAAAD/2wBDAAIBAQIBAQICAgICAgICAwUDAwMDAwYEBAMFBwYHBwcGBwcICQsJCAgKCAcHCg0KCgsMDAwMBwkODw0MDgsMDAz/2wBDAQICAgMDAwYDAwYMCAcIDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAz/wAARCAAXABcDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwCHxJ8M18WN4g8G2Gt69preE/EdjBrup6PazXLNPFYWizRThEkeFZfLecTuoizczjerxsa9D8U6D4X1RtF1iOw1a6vrWGSU50T+z13K0UgX7RME3kz2lsQZ3O7yg+4ctXyt+0z+0R47/wCCav8AwUV8eeNJvCp1TQfiul9cafZXl4ILTWLaOaS2iuCVDspiuraQbWQM0TtgqsyvXpn7X/7dnh3w3/wT703xd4f8C+JNM8SfHLSl03TrfVJBNpukouI79mQyGPBgEiW7Iu5o3DPtIZK+czCniXWpOlPlV2t9+uuuqsnv+p7mU4qj9WxP1ijzSSdttr2TTd7XutV5vprufBz9lfXLTVtStbvUvGj6ddaidTvNT1ewexbSAI5I1it1lw0szvKqlkBjEYmzJuKKxXVf8Ezv2zdT/bJ/Zq8RPqfhfWra9+Glvpmn6zrskscthqcsxdImDfK4mYQs7rsZVLAlxvRSV6soym7ySOajUpxgt9fM+oP+ChX/AASW03/gpgvwGXWPEkvhPw74C066/t28tLVG1i/S6SxdYYRgwoxeCVjI29YzIcRybjj43/Z8+GHww/4KF/G/4ifso+PrPUPCWh/Bma60z4a6t4fvJJrq6ksZfsl3LN50ezAKq3lyIoZZ2VXygclFXUw8JyjOa1jtq/L+tTio4qpThKEHpJa6LX7/AND7i/4Jnf8ABNGf9gz9lbx18LfEmp6X4iXxP4yn1i11TT0eNruwEFmlt50brmKYNbSFo1aRFLnbI+cgoorcxP/Z\" loc=\"mahesh nagar\" mobile=\"1234567890\" name=\"Gargi Gupta\" pc=\"302015\" po=\"Rambagh Jaipur\" state=\"Rajasthan\" street=\"mahesh nagar\" uid=\"256478954123\" vtc=\"Jaipur\"/></BarCodeData>\n" + "<sign>TVifNzpLy2wVoLA0GVGTfODvefSP9rZ2LNoO6zZk7/BiA9ty2cDgM8zhy2ygQP19GYeNnPsg/lCn1pBc5fFZRAeEKaVJPgeHLN1wm872KY3aaj4g9OdKhnTOUnrWnxih4ENHMCOxi/A6+843vPN9pVH77jXzw08GR6BXhWFNJ5T7QGty+q/uL5KeGLHuVFKO3uFpkszp802G/RYBLV9B240dJVcDGoXBs+gcgh0C/p2GPrD66/hVDkHOfffwIh+evM4V0XH28wuHVl5FHO4glSK/ttFjrrgOKGg2PiXnnSqJJDz4eYFkiL61wf9BTVzphCeSZHwJPhkmLBxf6Hyq1w==";
              //  stringToJSONconvert(demostring);

                stringToJSONconvert(result);
                Log.e("data",result);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (SignatureException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void stringToJSONconvert(String data) throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {

        try{
            arrSplit = data.split("<sign>");
            if(arrSplit != null && arrSplit.length > 0)
            {
                data = arrSplit[0];
                //data = arrSplit[0] + "gargi";
                signature = arrSplit[1].getBytes();
                System.out.println("Plain Text.............:" +data);

                sign = new String(signature, StandardCharsets.UTF_8);
                System.out.println("Signature is :" +sign);

                //this methord signature compair and after data show in Show activity
                checkDigitalSign(data);
            }
            else
            {
                Toast.makeText(MainActivity.this,"This is not Secure QR code ",Toast.LENGTH_LONG).show();
                System.out.println("No Data Found");
            }
        }
        catch (Exception e){
            //Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(),SimpleDataActivity.class);
            i.putExtra("data",data);
            startActivity(i);
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkDigitalSign(String data) throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        hash = hashGenerate(data);

        // verify digital signature
        boolean result = digitalSignature.verifySign(publicKey,hash,sign);

        if(result == true)
        {

            System.out.println("Digital Signature Verified");


            JSONObject jsonObj = null;
            try {

                String EkycXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><authresponse code=\"31981110d240453f9480e292fc3e4b8f\" status=\"200\">    <asaType>DOIT</asaType>    <auth status=\"Y\" txn=\"UKC:DOITRJGOVAUTH202207121702304911113\" uidtoken=\"01000737MrsRhXVei/GCb7KKriC9LJK5FQNcs/XtC06r5k/cJoiL3x1cSbIeQ8X8XnmF//AS\"/>    <UidData UUID=\"444043414999844\" tkn=\"01000737MrsRhXVei/GCb7KKriC9LJK5FQNcs/XtC06r5k/cJoiL3x1cSbIeQ8X8XnmF//AS\">        <pa co=\"S/O Raghuveer Singh Chodhary\" dist=\"Jhunjhunun\" house=\"\" lm=\"\" loc=\"NawalGarh Road\" ms=\"\" pc=\"333042\" state=\"Rajasthan\" street=\"\" vtc=\"Nawalri\"/>        <pht>/9j/4AAQSkZJRgABAgAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCADIAKADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDvKKKSsCxDR1o9aTqaQCilpM0yWVIIXlkYKiDczHoB60DsPBpc8VzereL9P02G5IlLvDEsg2Jv8wltu1eRk5IB9NwrktU+J2oKklrbWVvbznjzjIZNhzztUgAn696LMLnp5NNzjoa8bl8Um9nYQS6lO0YyZm1A2zg+uS5QDrxtrZ0D4htBKtrrNz50bfcnChmQ9g7Jw3bnAP64OUZ6Qx5OKjJyeaoWfiDSdSby7a/tnkIyEEyMf0JHer/BTKkMPUcilZgmKrZOFPI61KR71HFxxipTSHYTtimk80E5GcU0A7sk/hQFh3ak7UHpSdsUDFHNOFMBp2aBFiigUmKskKQUtQXV3bWUJnu7mG3iHBkmkCKPxNIZU17Uzo2hXmpeUJRbqGKGQR7huAPzEHB59Oa8T1HXn1C+ubhJrlre4JHlXE7OYlP8PXBAPT1HUZrR8TeNzfa5LcWjuYoDstklGUXHBcDPVh34IBwPWuRnvmZ2fIUMxYqAAMk5PT+VaxVkRfUsXmrSLbraF2fy33B85x0xj24B/Ks57lioJypPemPueJ5gefSq32gygBxkjgfSgdyxldu4sdw5zUjzP5O8sHGccuMj8OuKajxmYRuu1cjj296dNZk3rpFkovIYd6Oo9SIXLyEDeVA6DdgVo2eq6hp8plhunjZ3DGWGT58j/aBzj+dY0gUS8rj27VOYpdm4RDaOpBptiseteH/ilCYI7fW428/dtNyhAUjsWBxjHA4z616DYanY6pE0lje290q/eMMgfb9cdK+XGcqx6kVc02/nsb1Lm0uGt5k6Opwfp7j2qHBPUd2j6g4ApK5TwP4wHiaxeC5RE1G2RWk2txKp43j05HI9xXV54zWbVmUnca1GaCc8UZ5xSGFLn0oHWjNAFmkpc03NUSGcV4z8StUafxJdWvnfuLZUhWIHJJK7nb2GSqnpnb9a9gu7mGztJrq5fZBBG0sjeiqMn9BXzZrOpvqOs3l7IpEtzM0jLnO3J4XPfAwPwq4LUUim7b2z1I70cbcsoY9BUJkUtweamVZXRpNuUUgE/WtGiUVfNdUaLoc5zUltB5gO0ZYAk5oZAwGwZyec1ZihfEigBCy8c9ahspK7KTqyuWGS2aswzSLgu5UgYz6VJgwoqeWN/fPemzwSSJlkUHtjikpFOLWwnlLdRyswwVX5SB+X51VjbaTG07Kueg5FXYYSkEhYfNtwoz1NV444gygxE4PLFuD+FVclpj2sUxkPuBP3geKpFNrYz0qzc3DLKygqPZelV5JPMCkjBAwT60K4Oxo6Hq1xpOr217bErJDIGGDjcM8qT6EZH419Kaffwapp1vfWxzDOgkXPUZ7H3ByD9K+X4OBnOO4+tev/AAz8QK+iPp8jZeCckZ6hWx/7Nu/OpkrgnbU9IPXNJTEk3CpO1ZWLEAo9fSg8U0mgZaoopKok4z4maotl4XazWXbPfOI1GOqqQzf0H/Aq8RiiTDiViJCfl75r2z4n2i3HhmGXyozJHdIA5HzqrZyF57kL+A9a8QusST70PI64q47EsbNthOxdoPfPPNMEjmU5IAxxjvTJzulJVMA9B1q3D/B5iAheRkVeyDqVwVDDIwfyq3Egj5GWz3zR9iuL24DKNwHb0rq9M8PNOAGXaMYOR0rGrUUUa0qbkyrZaW2pLG2wsY12njt6k1sDw5E6+XvOTkknH4cZ6f571uWmi2lqi4iUyD+PHP5DirzQjblPyrzp1m3oelCkktTzi88MzCUhWZsdCBxWTPpj25CNjaDyRXqEy4zWVd2kUgO6PJrSOJktyZYVPY8wuokGCmQwOCDVYK3oa63WNMG8NFDwDk7TXOy2sqMSyFa7qdVTicFWg4srxg5HTmug8J3L23iC1KMw3PskAOAV9/ocH8KyEiiGPNZgD6dQf8KbbTNbXaSg4ZWBz9DWqZi1ofQ2l6h5h2lwwOQDW4jZFeWeFtRd5XjGQJA1yoLZ2gvgD8q9G0+4aaEFj04qKkeooO2jL56UylBpprE1LZo4xQelN7VQjlPiNJs8HTgKCXniUOSR5fzZLfkCPxrwiWRVm3qByc9ODXvfxCRn8C6kFUkr5TnnGAJFyfw614LczL5rIIgoHTnmtaexEtCKWVZbgMo2r0A9K07SAS4IBwO5rHjjZ5RzgetdHp6uxSNVJHqegpVHZF01zPU6HSrKJVzsBb1rprfaoA9KxbCPylAGK14txxivLqNt3PVpxsi8H/nTzkqxzz2FVvLndf3e0N235xUilwoDMN3fFYtGyRTuAQT61nXBz0NatzE7H39azLi3dBnNA0ZNwAxIJrndSiwxwCa6KaM7s5rJv13KeORW9LRmVWN0cwy7lI3ZYdAKiji3vnI49TSzgxysMkDPakiSSQqEBIzjAHNeqldHjy0djtvBBlE9wSm5Aqp5p5xjnYOffPSvU9HlyrL6VxWk2f8AZ+nQ2/Rl5f8A3j1ro9KuxFLhjwaqUfdOeMvfOrB4oqvFOHG0dRUwOa5WdaLeaT8aTNLVCGvDDcI0FxGskMgKSI4yGU8EH8K+YNRgeC/lgkUrcRuySqTnDAkNz9Qa+oa8N+KGlCx8XyXaIFjv4xMMDA3j5X/Ekbj/AL1XTepMkcbCGWT1rqtHtpHIZhyfWqHhu0juJHeRA2Oma6GS5a0kMVpCJJMcn+7WdaV/dRvRhb3mbtpa9M9q1ImjXjjjrXAyvqzFmeTaT6SY4rLnlvY5B/pEhYd1myf51yew5up1+25eh6ys8AOTjJ64qcm3aMNkbs15PZXF9E27PB7ZrqrO9mlgG4EkjB96xnS5Xub06nMtjpLm4iweVxjOAa57UdasVV4jMuSCPU1T1S6kVXB4z1ArlJba5ut0gbbHuxuJwM+nufpVUqcZbhVqOPwnQJq9i6gecp5xzVO6ntJGK7wD71iG0WBZHn84xxOEcrHjDHOBzg/wnt2qXytNkAEby7iM/OMV0ujGOupy+3lPQbe6cssRdOSOQR3pvhoKdbtI2UffJ6dwCf6VoW0HlRlQxKntWZYSrZa/G5UsFkPA9wR/WumkzkrRZ6OTyafDIVcGs+K6SeMOjZU1agBZhzXTdWucPK1KzOn0+5Lyrzy3FbamsTSYQq7z1PT2raXgVx1Gm9DrgnYv0fjSUUDK+o3X2HTbu82hjBC8gU/xEAkCvG9bS61i0kmvnkeaPfLGzSF+ccqAeFHA4HHSvX9ZRpdEv0VSzGByFHJbAzivPVSNisYQErExk7hssRn8hWVSbi0duGpxnGVzkvCy7BP9a3buJzGWiHJ9BWToKiKa4jxja2Py4rpoPnOAOlTWlrdBRikrMwdO0WG/M6ajdNHIUKxc8K3Yn1pdL8LtaahDc6tJaPZW+cJH83mDrggDoSep+ldI+mLOdwJU0i6JArB5mLEdABUe3aVka+wi3dmUllbGSZ7aMi25IRyW2+mMZx/wI9qtWUq20w5wO1Xrxm8lU2qkafdRRgfU+pqCyiBl3YHGc1i2mbpGNrkpmuDgnBPpTtJjaOWK4GJBADtj2A7Se45HPv1p+tRjzsAYAqvp7skqlGww/Wqi7R0HJe8O1uy0fU9TkvpYblJ5DulWFxsY/ioIPr/k1R1LZqDxokPlrGoVMdVUdBXQuiScugyfaq8sCLnaoB9qPbMn2Ed7GEkbRR7W5IrDu4it+zA4PUYrpLhAprnr8/8AEwAHeuqg7o468UpI62wiEVnEijAwD+ldFpNuJrlUPTrXP2jYtYcj+AfyrotDmVLnLHHGBXVr7M4J2dVs6qCFYlAUYqwKjQ5HWnA8VyXOhI0DQKWkrUzDJyK8/wBStzpOtXUKKuyRMxbhgFTkgD6cr/wGvQK5/wAWWP2nTRcqBvtiWbPdD94fyP4VE48yNqFRwkecyRpHrDTIpQXAyy4xhv8A6/8APNa1rIFamXMTzFGcK3lchgADUClkcnt3rBvmjY6rWnfudJAwIAxnPrVxIAwJGPxrBtZGZgd3TjrW1DL8m3PTmuaTOlamPqm4XAjQZJ71Y0+3IQDGTVXWp1s5VmIOwrjI5Oc1HpXiUY3GBkKnjzUIz+dUlJxuO6TI9WgzIwI9qw7ZPLvlVuFY8GtDXvEdurF/lLuckL6+wrD+2m4eERK3ms6kDb0561dOErainKN9HqddJEFiyc8Cs+4bYuM5q9LcI1vz16ECsa8kyTWEU72NW7Io3L5kGKwmU3OpN/stgVqvndn9Kr6JaSvePMyMEznJFenRjpoeXXknK7OiA2qFA4AxWppSeZcRITwWH86zSK2dCjzexHHQ5rqnpE82GsrnZJ92lB9qExig+1cJ2o1KSjPNITW5iHemsAylWUFSMEEZBo3c0Z70hnO3XhbT1V2hWZR1WISnYPw649s4rjp18uZlxggkGvUH+auE8TWX2W+E6g+XL146GplG60NIVGpK7M2CXaeKuLf+UMnJGeayFfax5qOZZZiFjfBPAJ6VxyjrqejGWmhtyXayHP6HvUVzbpLYS+bGxViM/KQPz7VizWt4h2fbiE6fImD+eTU9tp8wUNHqjxuT1Kc/oRT5VumUrsfLo9gkHmQ26s4OBIQT/OqgkMbYY4p1/YXEiebNqfnEDhdhAA/EmsV0uFYKlwSM9CM1XJdasG+XVI12vQDjdz9arzzZGe9VWt9gUlyx+mKbJIWIHYURproROo+pPaoZLpM+ua2TVDTIflMp78Cr54NejRjaJ42InzSDGa6DQE/0ndjoprCjUs1dPoMeC7Y4xinV+EmktToF6Uh56ClHFIa4jtNAnnNIWoNNrcxFzzRSZopAKeeK5LxHrGmT289gkwluQODGNyqfdumfbrVvxLqEywyWFuuHkiJZg3zMOcqo69ByfevNrdmmSJoMLFDCjSsemW4Cj1PfjspqraXEnd2RKZSr7W4Pb3qeFj2pGt1uY+evr6VBG0lpIEm5U9GrllaWh3xvE0gWmwtKdHuJhxOUB/u9aLeZQwPUVd/tARnCHkdaw1izpWqMi40uW3HzXLNkdGFZs0Zj5Nb15ci5+Z2wQOgrGuHQDrzVRlJvUbikilMx29xUMBhMw86UImeSahv70J8i4JNUHdiqdSSa7KcO559efY7mLZ5a+WQVxxg8U/PNZmg2V6libif5bcnAZuBn61opy3rXXGSeiPNlFrVl62QHFdjp8IitUXGOMn6muWsIjJMiDuRXYx8CuetLodFGJJxjikopufWuc6DRNIa5+PxfaPaQ3L2t3FHKSFZomZeMg8qCOoIwPSqN54qN2kkenCVZQp274nRM5/idgGH0UZ6D1x0WbOfmR1oBboM/Sqc+radbPsuL+1jbONrTKDn0xnOa497S5vbSWa5JvpFGQ1zgKCP4Y16D6nJ9TVLTLOJfENtFbW5RLeETzmWKFf3jKMKoVAeN3XNUoCc+xuXVuuqQXEoDJPK++OVlAePB+Xb6Y/XvXm6PNFqDWUilBbysWXGCznAyfwAx7fWvWSDk7jk1yPizSil5b6pEmUYeRP6qR9w+4IyM+wpVNEVQd52ZStqfcQLLGQRkGorY4wDVthlT3rzpS1PWSMKcXVmCYsug7dxVFtadAcxkGujO1shqpz2NvKDuUH3xWkakftIzcJfZZgPrrEkFTVWfVZJE+UYPTNas+kQZOFNV10mNm6nFbRnT7GUo1drmNHG80mTknPJrU0y0W51q1s2wBIcDPrVwWkcIwi/iaqyK8cyyxsVkRgyMOqkcg1Sqpsh0WlqdxdwR6ZZLNHul0e2uGM1qT3GVLL3A6EjOOSRiq6xFJiCgUEZUKOMVq+HZ21XTEmnUfPmOQA8KQMMfxA/Wue8txod9ZYBv9GlILsNwdAcgkH1UH8quk3c56y6HYaNbcGY/QVujgV55oniC9QlTJJMnVY8KSOnALEAg57kEH1zWhP4ymiUMbafcxJAWON1A7DKyHNTUhJyCDSR2maaT2rkrbxRfaiNllbxiVV3MtwQm7/d2lufrineH/El3rFtcyyrbq0IUhVVhkNnHOevBqXCSKU0zQ09Il0LS4oWyqW4YEDHLAEn8TmpizecJpY45HXpIUG4fjTSYli06WGSNoriyhfajcoSvQjtwBUoG7j1rfqc8i5bf6VdRmX5g3XP0rmvD8Tyalrl27Myx3pso2POVj3Dk+u3y/wAq6nSoJTfwIsbNk5GB2xXNeEir6FqFy5GEvxLI2ScrJGoU+nLDFWu5JvnYqGWZxHEOrH+QqlJLca0k+mWlnAwlTMIkhDuHXkNnOAf8Op6U0rLqM5eRxFDH0J6KP8aWW7S2Ty7HKHOfMzgsR/Sk1cIvl1Ocl0uUWQvYraSMIWS4tzy1vIvDA9yM8g+hBqskuODVi88U6dp3ieNE+0XbXK7NQiRv4wAEI3HG4dCOmMdxRc29vIPtenyiW1ZtrDo0Tf3WB5FcVehy+8j08NiOb3ZFd1VucUxmULTWV1FRuxx0rlTudlivMwJIHNRABRnGKnKFm6UkkLBQTxV3sKxWKlznFQyQA1fjiIFOMIzuxRzWBxudT4FgVNFuJtgJS5K5IyAdoJP6iqcgs7b4jX8V3PLDFe2QYqqbsyBRgnpxhW/Emtfwxsg8MXUssnkxJOTvJAH8OTz9MfhXn9541s5vGX299MSeyTdCCSd5j2su7PPdi3T05rvo6xPNxDSlYjjhl0+4MQY/IQUZTjI7VautjR+ZFwCfmGMYz7dvw457dKZcbI5gqMXhEfmI/dkxnOPoCajsHiurl0MoEZjJLHoo7tz6DJ/Ct91c5L2difTjIuoWgjba7zIgb0ywB/StPw//AKB4mawK/LMXgA9xyp/IH86w21CC1vogQ5eCTLFehI7D2960/Ed7Ims2eqWsHkWkwSVnUkkbSN3ze4omm7BDqjZ0W0hk8PWP2i7kW4CMEZF3bcOwB/QVoxardZ8swxMy/KZUQ4b3x2oopSitxrexq6TZ6re3hBnaMGM/M8uwdR2Fee+CwtlHJM9/GtpIlv8AaoScAKxKiQ/7hYH6FqKKIbMDu71Ut5XtI33ojYD/AN/3rmfEesLo2lyXAINxKDHbqfXufw6/lRRVxRD3OJ8PWtyur295dRs5mlAYueW3HGT+ddRNa3Fh4ulgLLFOriLMLBhMpHyq4PXqOeaKKie5cNmarRRvGZkwFHDoc5U9CKg8iN+2KKK8vEwjCfunsYSbnD3h4tYgMgVBcRZAAXiiisHodDRW8picVYhtIvs09zcXEcccChmTdlyM4Hy+5wBnvRRW1GKlKzMMRNwhdHKa34iu7+KPT4siGM/JEn3V9/c+5/SrHh/Rli0nVWuIEklmt8RbxyhHzcfUhR9KKK9SS5VZHjptyuy74cd7mCO4hiQPbuYhv5UrjOP/AB78qr32lDSLwXPkObZm3QEsCrf3kOOnUj6UUU4/HYUtiC6uIDqCMtvHsSFRGB6c7c+p27R+Fal/eT6r4MVJGJFhKMKBgBWOP5H9KKK3cVyXMVJ89j//2Q==</pht>        <pi dob=\"06-08-1991\" email=\"\" gender=\"M\" ms=\"\" name=\"Amit Chodhary\" phone=\"\"/>        <LData co=\"S/O  रघुवीर सिंह चोधरी\" country=\" \" dist=\"झुंझुनूं\" house=\"\" lang=\"06\" lm=\"\" loc=\"नवलगढ़ रोड\" name=\"अमित चोधरी\" pc=\"333042\" po=\"\" state=\"राजस्थान\" street=\"\" subdist=\"\" vtc=\"नवलडी\"/>    </UidData>    <uidToken>01000737MrsRhXVei/GCb7KKriC9LJK5FQNcs/XtC06r5k/cJoiL3x1cSbIeQ8X8XnmF//AS</uidToken></authresponse>";
                jsonObj = XML.toJSONObject(EkycXML);

                //   Log.e("scanresult0081",jsonObj.toString());
                //  tv.setText(jsonObj.toString());
                //  JSONObject jsonObject = jsonObj.getJSONObject("authresponse");
                JSONObject jsonObject1 = jsonObj.getJSONObject("authresponse");
                String asatype = jsonObject1.getString("asaType");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("auth");
                String status = jsonObject2.getString("status");
                String _txn = jsonObject2.getString("txn");
                String _uidtoken = jsonObject2.getString("uidtoken");

                JSONObject jsonObject3 = jsonObject1.getJSONObject("UidData");
                String pht = jsonObject3.getString("pht");
                byte[] imageAsBytes = Base64.decode(pht.getBytes(), Base64.DEFAULT);
                String _UUID = jsonObject3.getString("UUID");
                String _tkn = jsonObject3.getString("tkn");

                JSONObject jsonObject_pa = jsonObject3.getJSONObject("pa");
                String _co = jsonObject_pa.getString("co");
                String _dist = jsonObject_pa.getString("dist");
                String _house = jsonObject_pa.getString("house");
                String _lm = jsonObject_pa.getString("lm");
                String _loc = jsonObject_pa.getString("loc");
                String _ms = jsonObject_pa.getString("ms");
                String _pc = jsonObject_pa.getString("pc");
                String _state = jsonObject_pa.getString("state");
                String _street = jsonObject_pa.getString("street");
                String _vtc = jsonObject_pa.getString("vtc");



                JSONObject jsonObject_pi = jsonObject3.getJSONObject("pi");
                String _dob = jsonObject_pi.getString("dob");
                String _gender = jsonObject_pi.getString("gender");
                String _name = jsonObject_pi.getString("name");


                Intent i = new Intent(MainActivity.this, EkycData_Activity.class);
                i.putExtra("strTransactionid",_txn);
                i.putExtra("imageBYte", imageAsBytes);
                i.putExtra("UID", "UID");
                i.putExtra("UUID",_UUID);
                i.putExtra("vtc", _vtc);
                i.putExtra("street", _street);
                i.putExtra("state", _state);
                i.putExtra("pc", _pc);
                i.putExtra("ms", _ms);
                i.putExtra("loc", _loc);
                i.putExtra("lm", _lm);
                i.putExtra("house", _house);
                i.putExtra("dist", _dist);
                i.putExtra("co", _co);
                i.putExtra("phone", "phone");
                i.putExtra("name", _name);
                i.putExtra("mss", "_mss");
                i.putExtra("gender", _gender);
                i.putExtra("email", "email");
                i.putExtra("dob", _dob);
                i.putExtra("photo", pht);
                //    i.putExtra("PDFpic",PDFpic);
                startActivity(i);

                // intentcall();





            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Digital Signature Verification Failed");
        }

    }

    //Hash Genrator
    public static String hashGenerate(String str)
    {
        String hash = null;
        try
        {
            // generate hash of plain text i.e XML string
            byte[] hashInBytes = HashGenerator.getSHA(str);
            hash = HashGenerator.toHexString(hashInBytes);
            System.out.println("Hash of XML is : " + hash);
        }
        catch (NoSuchAlgorithmException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hash;
    }






    //send data one activity to another activity
    public void intentcall(){
//        Intent i = new Intent(MainActivity.this, EkycData_Activity.class);
//        i.putExtra("strTransactionid",Transactionid);
//        i.putExtra("imageBYte", imageAsBytes);
//        i.putExtra("UID", UID);
//        i.putExtra("UUID",UUID);
//        i.putExtra("vtc", VTC);
//        i.putExtra("street", street);
//        i.putExtra("state", state);
//        i.putExtra("pc", pc);
//        i.putExtra("ms", ms);
//        i.putExtra("loc", loc);
//        i.putExtra("lm", lm);
//        i.putExtra("house", house);
//        i.putExtra("dist", dist);
//        i.putExtra("co", co);
//        i.putExtra("phone", phone);
//        i.putExtra("name", name);
//        i.putExtra("mss", mss);
//        i.putExtra("gender", gender);
//        i.putExtra("email", email);
//        i.putExtra("dob", dob);
//        i.putExtra("photo", photo);
//        startActivity(i);
    }


}