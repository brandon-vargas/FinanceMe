package apps.brandon.finance;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class CurrentPaySection extends StatelessSection{

    String title;
    List<BillData> billDataList;

    CurrentPaySection(String title, List<BillData> billDataList){
        super(SectionParameters.builder()
                .itemResourceId(R.layout.current_pay_section_body)
                .headerResourceId(R.layout.current_pay_section_header)
                .build());
        this.title = title;
        this.billDataList = billDataList;
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
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

        String name = billDataList.get(position).getName();

        itemViewHolder.name.setText(name);
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

