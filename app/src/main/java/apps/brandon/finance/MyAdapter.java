package apps.brandon.finance;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.BillViewHolder>{

    private Context mContext;
    private List<BillData> mBillList;

    //area to effect card within recyclerview
    public static class BillViewHolder extends RecyclerView.ViewHolder{

        TextView nameView;
//        ImageView imageView;
        CardView cardView;
        TextView amountView;

        public BillViewHolder(View itemView){
            super(itemView);

            nameView = itemView.findViewById(R.id.name);
            amountView = itemView.findViewById(R.id.amount);
//            imageView = itemView.findViewById(R.id.image);
            cardView = itemView.findViewById(R.id.card);
        }
    }

    //This constructor will be used to pass data from MainActivity.java to this adapter
    MyAdapter(Context mContext, List<BillData> mBillList ){
        this.mContext = mContext;
        this.mBillList = mBillList;
    }

    @Override
    public MyAdapter.BillViewHolder onCreateViewHolder( ViewGroup parent, int i) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row_item, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BillViewHolder viewHolder, final int position) {
//        viewHolder.imageView.setImageResource(mBillList.get(position).getBillImage());
        viewHolder.nameView.setText(mBillList.get(position).getName());
        viewHolder.amountView.setText(mBillList.get(position).getAmount());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(mContext, DetailedActivity.class);
                intent.putExtra("title", mBillList.get(viewHolder.getAdapterPosition()).getName());
                intent.putExtra("day", mBillList.get(viewHolder.getAdapterPosition()).getDay());
                intent.putExtra("description", mBillList.get(viewHolder.getAdapterPosition()).getDescription());
                intent.putExtra("amount", mBillList.get(viewHolder.getAdapterPosition()).getAmount());
//                intent.putExtra("image", mBillList.get(viewHolder.getAdapterPosition()).getBillImage());
                intent.putExtra("size",mBillList.size());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBillList.size();
    }
}