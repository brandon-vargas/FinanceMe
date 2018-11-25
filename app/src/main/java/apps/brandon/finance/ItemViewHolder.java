package apps.brandon.finance;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    TextView name;
    CardView cardView;

    ItemViewHolder(View view){
        super(view);

        this.cardView = view.findViewById(R.id.card_section);
        this.name = view.findViewById(R.id.name_section);
    }
}
