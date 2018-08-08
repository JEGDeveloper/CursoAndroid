package test.yespinoza.androidproject.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import test.yespinoza.androidproject.Model.Entity.CardView;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.R;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private List<CardView> items;
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView image;
        public TextView name;
        public TextView description;
        public CardViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            description = (TextView) v.findViewById(R.id.description);
        }
    }
    public CardViewAdapter(List<CardView> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        return new CardViewHolder(v);
    }
    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int i) {
        viewHolder.image.setImageBitmap(Helper.fromBase64ToBitmap_Scalad(items.get(i).getImage(), 100, 100));
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.description.setText(items.get(i).getDescritpion());
    }
}
