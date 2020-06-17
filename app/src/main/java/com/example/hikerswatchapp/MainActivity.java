package com.example.hikerswatchapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView accuracyTextView;
    TextView altitudeTextView;
    TextView addressTextView;
    LocationManager locationManager;
    LocationListener locationListener;
    Double lat,lng,alt;
    Float accuracy;
    String address=" ";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        latitudeTextView=findViewById(R.id.latitudeTextView);
        longitudeTextView=findViewById(R.id.longitudeTextView);
        accuracyTextView=findViewById(R.id.accuracyTextView);
        altitudeTextView=findViewById(R.id.altitudeTextView);
        addressTextView=findViewById(R.id.addressTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat=location.getLatitude();
                lng=location.getLongitude();
                alt=location.getAltitude();
                accuracy=location.getAccuracy();

                latitudeTextView.setText("Latitude: " + Double.toString(lat));
                longitudeTextView.setText("Longitude: " + (Double.toString(lng)));
                accuracyTextView.setText("Accuracy: " + Float.toString(accuracy));
                altitudeTextView.setText("Altitude: " + Double.toString(alt) + " m");

                Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> listAddress= geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    address="";
                    if(listAddress.size()>0 && listAddress != null)
                    {
                        if(listAddress.get(0).getFeatureName() != null)
                        {
                            address += listAddress.get(0).getFeatureName() + " ";
                        }
                        if(listAddress.get(0).getThoroughfare() != null)
                        {
                            address += listAddress.get(0).getThoroughfare()+ " ";
                        }
                        if(listAddress.get(0).getLocality() != null)
                        {
                            address += listAddress.get(0).getLocality()+ " ";
                        }
                        if(listAddress.get(0).getSubAdminArea() != null)
                        {
                            address += listAddress.get(0).getSubAdminArea()+ " ";
                        }
                        if(listAddress.get(0).getAdminArea() != null)
                        {
                            address += listAddress.get(0).getAdminArea()+ " ";
                        }
                        if(listAddress.get(0).getPostalCode() != null)
                        {
                            address += listAddress.get(0).getPostalCode()+ " ";
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                addressTextView.setText("Address: " + address);



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,10,locationListener);
        }




    }
}