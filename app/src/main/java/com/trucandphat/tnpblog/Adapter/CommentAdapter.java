package com.trucandphat.tnpblog.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trucandphat.tnpblog.Model.Blog;
import com.trucandphat.tnpblog.Model.Comment;
import com.trucandphat.tnpblog.R;


import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    private Context context;
    private int resource;
    private ArrayList<Comment> commentList;
    public CommentAdapter(Context context, int resource,ArrayList<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.commentList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentAdapter.ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
            viewHolder = new CommentAdapter.ViewHolder();
            viewHolder.tvAuthor = convertView.findViewById(R.id.usercomment);
            viewHolder.tvContent = convertView.findViewById(R.id.content_comment);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (CommentAdapter.ViewHolder)convertView.getTag();
        }
        Comment comment = commentList.get(position);
        viewHolder.tvAuthor.setText(comment.getAuthorName());
        viewHolder.tvContent.setText(comment.getContent());
        return convertView;
    }
    public class ViewHolder {
        TextView tvAuthor,tvContent;
    }
    public String getTime() {
        return "";
    }
}
