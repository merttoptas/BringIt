package com.merttoptas.bringit.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.merttoptas.bringit.Activity.Model.Offer;
import com.merttoptas.bringit.Activity.Model.Reklam;
import com.merttoptas.bringit.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<Object> list;
    private final static int TIP_Offer =1, TIP_Reklam =2;
    private final int VIEW_TYPE_LOADING = 3;


    private Context context;


    public RecyclerViewAdapter(List<Object> offerlist, Context context) {
        list = offerlist;
        this.context = context;
    }
    public RecyclerViewAdapter(){
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Offer) {
            // Tipler eşit ise true döner
            return TIP_Offer;
        } else if (list.get(position) instanceof Reklam) {
            return TIP_Reklam;
        }
        return -1;    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        int layout = 0;
        RecyclerView.ViewHolder viewHolder;

        switch (viewType) {
            case TIP_Offer:
                layout = R.layout.offer_layout;
                View offerView =
                        LayoutInflater
                                .from(viewGroup.getContext())
                                .inflate(layout,viewGroup, false);

                viewHolder = new OfferViewHolder(offerView);

                break;

            case TIP_Reklam:
                layout = R.layout.reklam_layout;
                View reklamView =
                        LayoutInflater
                                .from(viewGroup.getContext())
                                .inflate(layout,viewGroup,false);
                viewHolder = new ReklamViewHolder(reklamView);

                break;

            default:
                viewHolder = null;
                break;
        }

        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = viewHolder.getItemViewType();
        Object object  = list.get(i);

        switch (viewType){

            case TIP_Offer:
                Offer offer = (Offer) list.get(i);
                ((OfferViewHolder) viewHolder).showDetails(offer);
                break;

            case TIP_Reklam:
                Reklam reklam = (Reklam) list.get(i);
                ((ReklamViewHolder)viewHolder).showDetails(reklam);
                break;

            case VIEW_TYPE_LOADING:
                showLoadingView((LoadingViewHolder) viewHolder, i);
                break;


        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder{
        private TextView tvOfferBaslik,tvAdSoyad, tvIl, tvIlce,tvDate;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAdSoyad = (TextView) itemView.findViewById(R.id.tvAdSoyad);
            tvIl =  (TextView) itemView.findViewById(R.id.tvIl);
            tvIlce =(TextView) itemView.findViewById(R.id.tvIlce);
            tvOfferBaslik = (TextView) itemView.findViewById(R.id.tvOfferBaslik);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

        }
        void showDetails(Offer offer){

            String adSoyad = offer.getEtToIl();
            String baslik = offer.getEtBaslik();
            String il = offer.getEtIl();
            String ilce = offer.getEtIlce();
            String date = offer.getDateTime();

            tvAdSoyad.setText(adSoyad);
            tvOfferBaslik.setText(baslik);
            tvIl.setText(il);
            tvIlce.setText(ilce);
            tvDate.setText(date);
        }


    }

    public class ReklamViewHolder extends RecyclerView.ViewHolder{
        public ReklamViewHolder(View view){
            super(view);
        }
        public void showDetails(Reklam reklam){

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

}
