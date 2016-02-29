package org.sakaiproject.sakai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.sakaiproject.adapters.CommentsAdapter;
import org.sakaiproject.api.pojos.wiki.Comment;
import org.sakaiproject.api.pojos.wiki.Wiki;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CommentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        setTitle(getIntent().getStringExtra("title"));

        recyclerView = (RecyclerView) findViewById(R.id.comments_recycle);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CommentsAdapter(this, (List<Comment>) getIntent().getSerializableExtra("comments"));
        recyclerView.setAdapter(adapter);
    }
}
