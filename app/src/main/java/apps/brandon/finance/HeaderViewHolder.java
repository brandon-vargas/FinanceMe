package apps.brandon.finance;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class HeaderViewHolder extends RecyclerView.ViewHolder{

    public TextView header;

    HeaderViewHolder(View view){
        super(view);
        header = view.findViewById(R.id.current_pay_header);
    }
}
