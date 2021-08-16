package com.rivierasoft.historicalturkishseries;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.AdapterViewHolder> {

    private ArrayList<Episode> episodes;
    private int layout;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public EpisodeAdapter(ArrayList<Episode> episodes, int layout, Context context, OnItemClickListener onItemClickListener) {
        this.episodes = episodes;
        this.layout = layout;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        AdapterViewHolder adapterViewHolder = new AdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        Episode episode = episodes.get(position);

        Glide.with(context)
                .asBitmap()
                .load(episode.getPhoto())
                .into(holder.imageView);
        holder.titleTextView.setText(episode.getTitle());
        holder.durationTextView.setText(episode.getDuration());
        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
        Calendar c = Calendar.getInstance(tz);
        int day = Integer.parseInt(String.format("%02d" , c.get(Calendar.DAY_OF_YEAR)));
        int year = Integer.parseInt(String.format("%02d" , c.get(Calendar.YEAR)));
        int hour = Integer.parseInt(String.format("%02d" , c.get(Calendar.HOUR_OF_DAY)));
        int minute = Integer.parseInt(String.format("%02d" , c.get(Calendar.MINUTE)));
        String date = String.format("%02d" , c.get(Calendar.DAY_OF_YEAR))+"/"+
                String.format("%02d" , c.get(Calendar.YEAR));
        String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE));
        String dt = String.format("%02d" , c.get(Calendar.DAY_OF_MONTH))+"/"+
                String.format("%02d" , c.get(Calendar.MONTH)+1)+"/"+
                String.format("%02d" , c.get(Calendar.YEAR))+"-"+
                String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
                String.format("%02d" , c.get(Calendar.MINUTE)); //+":"+
//                   String.format("%02d" , c.get(Calendar.SECOND))+":"+
//               String.format("%03d" , c.get(Calendar.MILLISECOND));
        String[] e = episode.getTime().split("-");
        String eDate = e[0]; String eTime = e[1];
        String[] d = eDate.split("/");
        int eDay = Integer.parseInt(d[0]); int eYear = Integer.parseInt(d[1]);
        String[] t = eTime.split(":");
        int eHour = Integer.parseInt(t[0]); int eMinute = Integer.parseInt(t[1]);
        if (date.equals(eDate)) {
            if (time.equals(eTime))
                holder.timeTextView.setText("الآن");
            else {
                if (hour == eHour) {
                    int minutes = minute - eMinute;
                    if (minutes == 1)
                        holder.timeTextView.setText("منذ دقيقة واحدة");
                    else if (minutes == 2)
                        holder.timeTextView.setText("منذ دقيقتين");
                    else if (minutes >= 3 && minutes <= 10)
                        holder.timeTextView.setText("منذ "+ minutes +" دقائق");
                    else holder.timeTextView.setText("منذ "+ minutes +" دقيقة");
                } else {
                    int hours = hour - eHour;
                    if (hours == 1)
                        holder.timeTextView.setText("منذ ساعة واحدة");
                    else if (hours == 2)
                        holder.timeTextView.setText("منذ ساعتين");
                    else if (hours >= 3 && hours <= 10)
                        holder.timeTextView.setText("منذ "+ hours +" ساعات");
                    else holder.timeTextView.setText("منذ "+ hours +" ساعة");
                }
            }
        } else {
            if (year == eYear) {
                int days = day - eDay;
                if (days == 1)
                    holder.timeTextView.setText("منذ يوم واحد");
                else if (days == 2)
                    holder.timeTextView.setText("منذ يومين");
                else if (days < 7)
                    holder.timeTextView.setText("منذ "+ days +" أيام");
                else if (days < 14)
                    holder.timeTextView.setText("منذ أسبوع واحد");
                else if (days < 21)
                    holder.timeTextView.setText("منذ أسبوعين");
                else if (days < 28)
                    holder.timeTextView.setText("منذ 3 أسابيع");
                else if (days < 30)
                    holder.timeTextView.setText("منذ 4 أسابيع");
                else if (days < 60)
                    holder.timeTextView.setText("منذ شهر واحد");
                else if (days < 90)
                    holder.timeTextView.setText("منذ شهرين");
                else if (days < 120)
                    holder.timeTextView.setText("منذ 3 أشهر");
                else if (days < 150)
                    holder.timeTextView.setText("منذ 4 أشهر");
                else if (days < 180)
                    holder.timeTextView.setText("منذ 5 أشهر");
                else if (days < 210)
                    holder.timeTextView.setText("منذ 6 أشهر");
                else if (days < 240)
                    holder.timeTextView.setText("منذ 7 أشهر");
                else if (days < 270)
                    holder.timeTextView.setText("منذ 8 أشهر");
                else if (days < 300)
                    holder.timeTextView.setText("منذ 9 أشهر");
                else if (days < 330)
                    holder.timeTextView.setText("منذ 10 أشهر");
                else if (days < 366)
                    holder.timeTextView.setText("منذ 11 شهر");
            } else {
                int years = year - eYear;
                int year_days;
                if (eYear %4 == 0)
                    year_days = 366;
                else year_days = 365;
                int days = (year_days - eDay) + day + 365 * (years-1);
                if (days == 1)
                    holder.timeTextView.setText("منذ يوم واحد");
                else if (days == 2)
                    holder.timeTextView.setText("منذ يومين");
                else if (days < 7)
                    holder.timeTextView.setText("منذ "+ days +" أيام");
                else if (days < 14)
                    holder.timeTextView.setText("منذ أسبوع واحد");
                else if (days < 21)
                    holder.timeTextView.setText("منذ أسبوعين");
                else if (days < 28)
                    holder.timeTextView.setText("منذ 3 أسابيع");
                else if (days < 30)
                    holder.timeTextView.setText("منذ 4 أسابيع");
                else if (days < 60)
                    holder.timeTextView.setText("منذ شهر واحد");
                else if (days < 90)
                    holder.timeTextView.setText("منذ شهرين");
                else if (days < 120)
                    holder.timeTextView.setText("منذ 3 أشهر");
                else if (days < 150)
                    holder.timeTextView.setText("منذ 4 أشهر");
                else if (days < 180)
                    holder.timeTextView.setText("منذ 5 أشهر");
                else if (days < 210)
                    holder.timeTextView.setText("منذ 6 أشهر");
                else if (days < 240)
                    holder.timeTextView.setText("منذ 7 أشهر");
                else if (days < 270)
                    holder.timeTextView.setText("منذ 8 أشهر");
                else if (days < 300)
                    holder.timeTextView.setText("منذ 9 أشهر");
                else if (days < 330)
                    holder.timeTextView.setText("منذ 10 أشهر");
                else if (days < 365)
                    holder.timeTextView.setText("منذ 11 شهر");
                else if (days < 730)
                    holder.timeTextView.setText("منذ سنة واحدة");
                else if (days < 1095)
                    holder.timeTextView.setText("منذ سنتين");
                else if (days < 1460)
                    holder.timeTextView.setText("منذ 3 سنوات");
                else if (days < 1825)
                    holder.timeTextView.setText("منذ 4 سنوات");
                else if (days < 2190)
                    holder.timeTextView.setText("منذ 5 سنوات");
                else if (days < 2555)
                    holder.timeTextView.setText("منذ 6 سنوات");
                else if (days < 2920)
                    holder.timeTextView.setText("منذ 7 سنوات");
                else if (days < 3285)
                    holder.timeTextView.setText("منذ 8 سنوات");
                else if (days < 3650)
                    holder.timeTextView.setText("منذ 9 سنوات");
                else holder.timeTextView.setText("منذ 10 سنوات");
            }
        }
//        TimeZone tz = TimeZone.getTimeZone("GMT+02:00");
//        Calendar c = Calendar.getInstance(tz);
//        int day = Integer.parseInt(String.format("%02d" , c.get(Calendar.DAY_OF_MONTH)));
//        int month = Integer.parseInt(String.format("%02d" , c.get(Calendar.MONTH)+1));
//        int year = Integer.parseInt(String.format("%02d" , c.get(Calendar.YEAR)));
//        int hour = Integer.parseInt(String.format("%02d" , c.get(Calendar.HOUR_OF_DAY)));
//        int minute = Integer.parseInt(String.format("%02d" , c.get(Calendar.MINUTE)));
//        String date = String.format("%02d" , c.get(Calendar.DAY_OF_MONTH))+"/"+
//                String.format("%02d" , c.get(Calendar.MONTH)+1)+"/"+
//                String.format("%02d" , c.get(Calendar.YEAR));
//        String time = String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
//                String.format("%02d" , c.get(Calendar.MINUTE));
//        String dt = String.format("%02d" , c.get(Calendar.DAY_OF_MONTH))+"/"+
//                String.format("%02d" , c.get(Calendar.MONTH)+1)+"/"+
//                String.format("%02d" , c.get(Calendar.YEAR))+"-"+
//                String.format("%02d" , c.get(Calendar.HOUR_OF_DAY))+":"+
//                String.format("%02d" , c.get(Calendar.MINUTE)); //+":"+
////                   String.format("%02d" , c.get(Calendar.SECOND))+":"+
////               String.format("%03d" , c.get(Calendar.MILLISECOND));
//        String[] e = episode.getTime().split("-");
//        String eDate = e[0]; String eTime = e[1];
//        String[] d = eDate.split("/");
//        int eDay = Integer.parseInt(d[0]); int eMonth = Integer.parseInt(d[1]); int eYear = Integer.parseInt(d[2]);
//        String[] t = eTime.split(":");
//        int eHour = Integer.parseInt(t[0]); int eMinute = Integer.parseInt(t[1]);
//        if (date.equals(eDate)) {
//            if (time.equals(eTime))
//                holder.timeTextView.setText("الآن");
//            else {
//                if (hour == eHour) {
//                    int minutes = minute - eMinute;
//                    if (minutes == 1)
//                        holder.timeTextView.setText("منذ دقيقة واحدة");
//                    else if (minutes == 2)
//                        holder.timeTextView.setText("منذ دقيقتين");
//                    else if (minutes >= 3 && minutes <= 10)
//                        holder.timeTextView.setText("منذ "+ minutes +" دقائق");
//                    else holder.timeTextView.setText("منذ "+ minutes +" دقيقة");
//                } else {
//                    int hours = hour - eHour;
//                    if (hours == 1)
//                        holder.timeTextView.setText("منذ ساعة واحدة");
//                    else if (hours == 2)
//                        holder.timeTextView.setText("منذ ساعتين");
//                    else if (hours >= 3 && hours <= 10)
//                        holder.timeTextView.setText("منذ "+ hours +" ساعات");
//                    else holder.timeTextView.setText("منذ "+ hours +" ساعة");
//                }
//            }
//        } else {
//            if (year == eYear && month == eMonth) {
//                int days = day - eDay;
//                if (days == 1)
//                    holder.timeTextView.setText("منذ يوم واحد");
//                else if (days == 2)
//                    holder.timeTextView.setText("منذ يومين");
//                else if (days >= 3 && days <= 6)
//                    holder.timeTextView.setText("منذ "+ days +" أيام");
//                else if (days >= 7 && days <= 13)
//                    holder.timeTextView.setText("منذ أسبوع واحد");
//                else if (days >= 14 && days <= 20)
//                    holder.timeTextView.setText("منذ أسبوعين");
//                else if (days >= 21 && days <= 27)
//                    holder.timeTextView.setText("منذ 3 أسابيع");
//                else holder.timeTextView.setText("منذ 4 أسابيع");
//            } else if (year == eYear){
//                int months = month - eMonth;
//                if (months == 1)
//                    holder.timeTextView.setText("منذ شهر واحد");
//                else if (months == 2)
//                    holder.timeTextView.setText("منذ شهرين");
//                else if (months >= 3 && months <= 10)
//                    holder.timeTextView.setText("منذ "+ months +" أشهر");
//                else holder.timeTextView.setText("منذ "+ months +" شهر");
//            } else {
//                //holder.timeTextView.setText(eDate);
//                int years = year - eYear;
//                if (years == 1)
//                    holder.timeTextView.setText("منذ سنة واحدة");
//                else if (years == 2)
//                    holder.timeTextView.setText("منذ سنتين");
//                else if (years >= 3 && years <= 10)
//                    holder.timeTextView.setText("منذ "+ years +" سنوات");
//                else holder.timeTextView.setText("منذ "+ years +" سنة");
//            }
//        }
        if (episode.getViews() == 0)
            holder.viewsTextView.setText("لا مشاهدات");
        else if (episode.getViews() == 1)
            holder.viewsTextView.setText("مشاهدة واحدة");
        else if (episode.getViews() == 2)
            holder.viewsTextView.setText("مشاهدتان");
        else if (episode.getViews() >= 3 && episode.getViews() <= 10)
            holder.viewsTextView.setText(episode.getViews()+ " مشاهدات");
        else holder.viewsTextView.setText(episode.getViews()+ " مشاهدة");
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView titleTextView, durationTextView, timeTextView, viewsTextView;
        ConstraintLayout constraintLayout;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            viewsTextView = itemView.findViewById(R.id.viewsTextView);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            constraintLayout.setOnClickListener(v -> onItemClickListener.OnClick(getAdapterPosition()));
        }
    }
}
