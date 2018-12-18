package com.vu.hp.sunshine.app.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vu.hp.sunshine.app.Common.Common;
import com.vu.hp.sunshine.app.Model.WeatherForceCastResult;
import com.vu.hp.sunshine.app.R;

public class WeatherForceCastAdapter extends RecyclerView.Adapter<WeatherForceCastAdapter.ViewHolder> {
    Context context;
    WeatherForceCastResult weatherForceCastResult;

    public WeatherForceCastAdapter(Context context, WeatherForceCastResult weatherForceCastResult) {
        this.context = context;
        this.weatherForceCastResult = weatherForceCastResult;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_forcecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // load icon
        Picasso.get().load(new StringBuilder(Common.HOME_ICON).append(weatherForceCastResult.list.get(position).weather.get(0).getIcon()).append(".png").toString()).into(holder.img_fc_icon);
        holder.tv_fc_temp.setText(new StringBuilder(String.valueOf(weatherForceCastResult.list.get(position).main.getTemp())).append("°F"));
        holder.tv_fc_date.setText(new StringBuilder(Common.convertUnixToDate(weatherForceCastResult.list.get(position).dt)));
        holder.tv_fc_description.setText(new StringBuilder(weatherForceCastResult.list.get(position).weather.get(0).getDescription()));
    }

    @Override
    public int getItemCount() {
        return weatherForceCastResult.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_fc_date, tv_fc_description, tv_fc_temp;
        ImageView img_fc_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_fc_date = (TextView) itemView.findViewById(R.id.tv_fc_date);
            tv_fc_description = (TextView) itemView.findViewById(R.id.tv_fc_description);
            tv_fc_temp = (TextView) itemView.findViewById(R.id.tv_fc_temp);
            img_fc_icon = (ImageView) itemView.findViewById(R.id.img_fc_icon);
        }
    }
}
