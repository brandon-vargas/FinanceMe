package apps.brandon.finance;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{

    public TextView name;
//    public TextView amount;
    public CardView cardView;
    public ItemLongClickListener itemLongClickListener;

    ItemViewHolder(View view){
        super(view);

//        this.amount = view.findViewById(R.id.name_section_not);
        this.cardView = view.findViewById(R.id.card_section);
        this.name = view.findViewById(R.id.name_section);
    }

    public void setItemLongClickListener(ItemLongClickListener ic){
        itemLongClickListener = ic;
    }

    @Override
    public boolean onLongClick(View v){
        this.itemLongClickListener.onItemLongClick(v, getLayoutPosition());
        return false;
    }
}
