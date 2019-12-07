package com.trucandphat.tnpblog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BlogAdapter extends ArrayAdapter<Blog> {
    private Context context;
    private int resource;
    private ArrayList<Blog> blogList;
    private Calendar currentCal = Calendar.getInstance();
    public final static String[] month = {"January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    public BlogAdapter(Context context, int resource,ArrayList<Blog> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.blogList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_blog,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvDate = convertView.findViewById(R.id.tv_itemDate);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_itemTime);
            viewHolder.tvAuthor = convertView.findViewById(R.id.tv_itemAuthor);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_itemTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Blog blog = blogList.get(position);
        viewHolder.tvDate.setText(getDateDifference(blog.getDateCreated()));
        viewHolder.tvTime.setText(getTimeToShow(blog.getDateCreated()));
        viewHolder.tvAuthor.setText(blog.getAuthorName());
        viewHolder.tvTitle.setText(blog.getTitle());
        return convertView;
    }
    private String getDateDifference(Date date) {
        String datediff = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date currentDate = itemCal.getTime();
        itemCal.setTime(date);
        itemCal.set(Calendar.HOUR_OF_DAY, 1);
        itemCal.set(Calendar.MINUTE, 1);
        itemCal.set(Calendar.SECOND, 1);
        Date thatdate = itemCal.getTime();
        long timediff = currentDate.getTime() - thatdate.getTime();
        float daydiff = Math.round(((float) timediff / (1000 * 60 * 60 * 24)) * 10) / 10;
        if (daydiff == 0.0) {
            datediff += "Today";
        } else if (daydiff > 0 && daydiff < 7) {
            datediff += (int) Math.ceil(daydiff) + " days ago";
        } else {
            datediff += month[itemCal.get(Calendar.MONTH)] + " " + itemCal.get(Calendar.DATE);
            if (!(currentCal.get(Calendar.YEAR) == itemCal.get(Calendar.YEAR))) {
                datediff += " " + itemCal.get(Calendar.YEAR);
            }
        }
        return datediff;
    }
    private String getTimeToShow(Date date) {
        String time = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.setTime(date);
        time += itemCal.get(Calendar.HOUR) + ":" + itemCal.get(Calendar.MINUTE);
        if (itemCal.get(Calendar.AM_PM) == Calendar.AM) {
            time += "\nA.M";
        } else {
            time += "\nP.M";
        }
        return time;
    }
    public class ViewHolder {
        TextView tvDate,tvAuthor,tvTitle, tvTime;
    }
    public String getTime() {
        return "";
    }
}
