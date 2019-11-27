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

public class BlogAdapter extends ArrayAdapter<Blog> {
    private Context context;
    private int resource;
    private ArrayList<Blog> blogList;
    public BlogAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Blog> objects) {
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
            viewHolder.tvAuthor = convertView.findViewById(R.id.tv_itemAuthor);
            viewHolder.tvTitle = convertView.findViewById(R.id.tv_itemTitle);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Blog blog = blogList.get(position);
        viewHolder.tvAuthor.setText(blog.getAuthorId());
        viewHolder.tvTitle.setText(blog.getTitle());
        return convertView;
    }
    public class ViewHolder {
        TextView tvDate,tvAuthor,tvTitle;
    }
}
