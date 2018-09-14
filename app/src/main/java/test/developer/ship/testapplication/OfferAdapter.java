package test.developer.ship.testapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import test.developer.ship.testapplication.entity.Offer;

/**
 * Created by Shiplayer on 12.09.18.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private List<Offer> offerList;
    private Context mContext;
    private MainActivity.BrowserCaller mCaller;

    public OfferAdapter(MainActivity.BrowserCaller caller){
        offerList = new ArrayList<>();
        mCaller = caller;
    }
    public OfferAdapter(List<Offer> list){
        offerList = list;
    }

    public void setOfferList(List<Offer> offerList) {
        this.offerList = offerList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Offer offer = offerList.get(position);
        holder.title.setText(offer.getName());
        holder.pbLoadLogo.setVisibility(View.VISIBLE);
        Picasso.get().load(offer.getLogo()).fit().centerCrop().into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.pbLoadLogo.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                holder.pbLoadLogo.setVisibility(View.GONE);
                //Picasso.get().load(R.drawable.ic_error_black_24dp).fit().centerCrop().into(holder.imageView);
                holder.imageView.setImageResource(R.drawable.ic_error_black_24dp);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Произошла ошибка при загрузке логотипа", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
        holder.description.setText(offer.getDes());
        if(offer.getBtn() != null && !offer.getBtn().isEmpty()) {
            holder.button.setText(offer.getBtn());
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCaller.callBrowser(offer.getUrl(), offer.isBrowser());
                }
            });
        } else
            holder.button.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCaller.startOfferActivity(offer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView imageView;
        private TextView title;
        private TextView description;
        private Button button;
        private ProgressBar pbLoadLogo;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView_item);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.tv_title);
            description = itemView.findViewById(R.id.tv_description);
            button = itemView.findViewById(R.id.cv_button);
            pbLoadLogo = itemView.findViewById(R.id.pb_load_logo);
        }
    }
}
