package org.sakaiproject.sakai;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.sakaiproject.helpers.ActionsHelper;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private EditText chatMessage;
    private ImageView chatSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatRecyclerView = (RecyclerView) findViewById(R.id.chat_recycle_view);
        layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);

        chatMessage = (EditText) findViewById(R.id.chat_message);
        chatSendButton = (ImageView) findViewById(R.id.chat_send);
        chatSendButton.setImageDrawable(ActionsHelper.setCustomDrawableColor(this, R.mipmap.ic_send, Color.parseColor("#43C84E")));

        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatMessage.setText("");
                Toast.makeText(getApplicationContext(), "Message send", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
