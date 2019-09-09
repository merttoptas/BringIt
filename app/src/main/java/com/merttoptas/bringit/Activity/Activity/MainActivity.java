package com.merttoptas.bringit.Activity.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.merttoptas.bringit.Activity.Fragment.AccountFragment;
import com.merttoptas.bringit.Activity.Fragment.MapsFragment;
import com.merttoptas.bringit.Activity.Fragment.MessageFragment;
import com.merttoptas.bringit.Activity.Fragment.OfferFragment;
import com.merttoptas.bringit.R;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BubbleNavigationChangeListener{

    BubbleNavigationConstraintView bubbleNavigation;
    AccountFragment accountFragment = new AccountFragment();
    MapsFragment mapsFragment = new MapsFragment();
    MessageFragment messageFragment = new MessageFragment();
    OfferFragment offerFragment = new OfferFragment();
    public double latitude;
    public double longitude;
    AutoCompleteTextView etIl, etIlce, etToİl, etToIlce;
    TextView etEsyaSekli, etKatSayisi, etKat;
    EditText etBaslik, etdateTime, etAciklama;
    Button btnOfferSave;
    private final static int navigation_maps =0, navigation_offer =1,navigation_message =2, navigation_account =3;
    Typeface typeface;



    @Override
    protected void onStart() {
        super.onStart();

        bubbleNavigation.setCurrentActiveItem(0);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, mapsFragment).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();


        bubbleNavigation = findViewById(R.id.bottom_navigation_view_linear);
        bubbleNavigation.setNavigationChangeListener(this);
        bubbleNavigation.setCurrentActiveItem(navigation_maps);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/rubik.ttf");
        bubbleNavigation.setTypeface(typeface);

        //Resource of Items
        etBaslik = findViewById(R.id.etBaslik);
        etEsyaSekli = findViewById(R.id.etEsyaSekli);
        etKatSayisi = findViewById(R.id.etKatSayisi);
        etIl = findViewById(R.id.etIl);
        etIlce = findViewById(R.id.etIlce);
        etToİl = findViewById(R.id.etToİl);
        etKat = findViewById(R.id.etKat);
        etToIlce = findViewById(R.id.etToIlce);
        btnOfferSave = findViewById(R.id.btnSave);
        etAciklama = findViewById(R.id.etAciklama);

    }

    @Override
    public void onNavigationChanged(View view, int position) {
        bubbleNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                switch (position) {
                    case navigation_account:

                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, accountFragment).commit();
                        bubbleNavigation.getCurrentActiveItemPosition();

                        break;
                    case navigation_offer:

                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, offerFragment).commit();
                        bubbleNavigation.getCurrentActiveItemPosition();

                        break;
                    case navigation_maps:

                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, mapsFragment).commit();
                        bubbleNavigation.getCurrentActiveItemPosition();
                        break;
                    case navigation_message:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, messageFragment).commit();
                        bubbleNavigation.getCurrentActiveItemPosition();
                        break;
                }


            }
        });


    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragments;



        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }



}
