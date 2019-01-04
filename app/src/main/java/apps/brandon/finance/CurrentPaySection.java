package apps.brandon.finance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class CurrentPaySection extends StatelessSection{

    String title;
    List<BillData> billDataList;
    private Context mContext;

    //This constructor will be used to pass data from MainActivity.java to this adapter
    CurrentPaySection(Context mContext, String title, List<BillData> billDataList){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.current_pay_section_body)
                .headerResourceId(R.layout.current_pay_section_header)
                .build());
        this.title = title;
        this.billDataList = billDataList;
        this.mContext = mContext;
    }

    @Override
    public int getContentItemsTotal() {
        return billDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        final int adapterPosition = itemViewHolder.getAdapterPosition()-1;
        itemViewHolder.name.setText(billDataList.get(position).getName());
//        itemViewHolder.amount.setText(billDataList.get(position).getAmount());
        itemViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(mContext, DetailedActivity.class);
                intent.putExtra("action","update");
                intent.putExtra("title", billDataList.get(adapterPosition).getName());
                intent.putExtra("day", billDataList.get(adapterPosition).getDay());

                intent.putExtra("description", billDataList.get(adapterPosition).getDescription());
                intent.putExtra("amount", billDataList.get(adapterPosition).getAmount());
                intent.putExtra("id", billDataList.get(adapterPosition).getId());
                intent.putExtra("size",billDataList.size());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view){
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder){
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

        headerViewHolder.header.setText(this.title);
    }
}

